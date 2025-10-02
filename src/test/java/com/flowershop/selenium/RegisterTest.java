package com.flowershop.selenium;

import com.flowershop.selenium.pages.RegisterPage;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import static org.junit.jupiter.api.Assertions.*;

public class RegisterTest extends BaseTest {

    @Test
    public void testSuccessfulRegistration() {
        driver.get(baseUrl + "/register");

        RegisterPage registerPage = new RegisterPage(driver);
        // Generate unique username/email for each test run
        String timestamp = String.valueOf(System.currentTimeMillis());
        String uniqueUsername = "seleniumuser" + timestamp;
        String uniqueEmail = "selenium" + timestamp + "@example.com";

        registerPage.enterFullName("Selenium Test User");
        registerPage.enterUsername(uniqueUsername);
        registerPage.enterEmail(uniqueEmail);
        registerPage.enterPassword("password123");
        registerPage.clickRegister();

        // Wait for redirect to login page
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.urlContains("/login"));
        // Verify redirect to login page
        assertTrue(driver.getCurrentUrl().contains("/login"),
                "Should redirect to login page after successful registration");
        System.out.println("Registration successful - redirected to login page: " + driver.getCurrentUrl());
    }
}