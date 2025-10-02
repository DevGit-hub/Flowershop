package com.flowershop.bdd;

import com.flowershop.model.User;
import com.flowershop.repository.UserRepository;
import com.flowershop.service.UserService;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class UserAuthenticationSteps {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User registrationUser;
    private Exception caughtException;
    private String resultMessage;
    private boolean operationSuccess;

    @Before
    public void setUp() {
        userRepository.deleteAll();
        registrationUser = null;
        caughtException = null;
        resultMessage = null;
        operationSuccess = false;
    }
    @Given("the FlowerShop application is running")
    public void the_flowershop_application_is_running() {
        assertNotNull(userService);assertNotNull(userRepository);
    }
    @Given("I am on the authentication page")
    public void i_am_on_the_authentication_page() {
    }

    @When("I provide valid registration details:")
    public void i_provide_valid_registration_details(DataTable dataTable) {
        var userData = dataTable.asMap(String.class, String.class);
        registrationUser = new User();
        registrationUser.setFullName(userData.get("fullName"));
        registrationUser.setUsername(userData.get("username"));
        registrationUser.setEmail(userData.get("email"));
        registrationUser.setPassword(userData.get("password"));
    }

    @When("I submit the registration form")
    public void i_submit_the_registration_form() {
        try {
            User result = userService.registerUser(registrationUser);
            operationSuccess = true;
            resultMessage = "Registration successful";
            registrationUser = result;
        } catch (Exception e) {
            caughtException = e;
            resultMessage = e.getMessage();
            operationSuccess = false;
        }}

    @Then("I should be registered successfully")
    public void i_should_be_registered_successfully() {
        assertTrue(operationSuccess);
        assertNull(caughtException);
        assertNotNull(registrationUser.getId());
    }
    @Then("I should receive a welcome message")
    public void i_should_receive_a_welcome_message() {
        assertEquals("Registration successful", resultMessage);
    }

    @Given("a user with email {string} already exists")
    public void a_user_with_email_already_exists(String email) {
        User existingUser = new User();
        existingUser.setUsername("existinguser");
        existingUser.setEmail(email);
        existingUser.setPassword(passwordEncoder.encode("password123"));
        existingUser.setFullName("Existing User");
        userRepository.save(existingUser);
    }

    @When("I try to register with email {string}")
    public void i_try_to_register_with_email(String email) {
        registrationUser = new User();
        registrationUser.setUsername("newuser");
        registrationUser.setEmail(email);
        registrationUser.setPassword("password123");
        registrationUser.setFullName("Test User");
    }
    @When("I provide other valid details:")
    public void i_provide_other_valid_details(DataTable dataTable) {
        var userData = dataTable.asMap(String.class, String.class);
        if (registrationUser == null) {
            registrationUser = new User();
        }
        registrationUser.setFullName(userData.get("fullName"));
        if (registrationUser.getUsername() == null) {
            registrationUser.setUsername(userData.get("username"));
        }
        if (registrationUser.getPassword() == null) {
            registrationUser.setPassword(userData.get("password"));
        }}
    @Then("I should see an error message {string}")
    public void i_should_see_an_error_message(String expectedMessage) {
        assertFalse(operationSuccess);
        assertNotNull(caughtException);
        assertEquals(expectedMessage, resultMessage);
    }

    @Given("a registered user with:")
    public void a_registered_user_with(DataTable dataTable) {
        var userData = dataTable.asMap(String.class, String.class);
        User user = new User();
        user.setUsername(userData.get("username"));
        user.setPassword(passwordEncoder.encode(userData.get("password")));
        user.setEmail("test@example.com");
        user.setFullName("Test User");
        userRepository.save(user);
    }

    @When("I login with username {string} and password {string}")
    public void i_login_with_username_and_password(String username, String password) {
        try {
            userService.authenticateUser(username, password);
            operationSuccess = true;
            resultMessage = "Login successful";
        } catch (Exception e) {
            caughtException = e;
            resultMessage = e.getMessage();
            operationSuccess = false;
        }
    }

    @Then("I should be logged in successfully")
    public void i_should_be_logged_in_successfully() {
        assertTrue(operationSuccess);
        assertNull(caughtException);
    }

    @Then("I should receive an authentication token")
    public void i_should_receive_an_authentication_token() {
        assertTrue(operationSuccess);
    }
}
