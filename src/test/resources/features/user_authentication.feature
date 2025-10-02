Feature: User Authentication
  As a user
  I want to register and login to the FlowerShop application
  So that I can access personalized features

  Background:
    Given the FlowerShop application is running
    And I am on the authentication page

  Scenario: Successful user registration
    When I provide valid registration details:
      | fullName | John Doe            |
      | username | johndoe             |
      | email    | john@example.com    |
      | password | password123         |
    And I submit the registration form
    Then I should be registered successfully
    And I should receive a welcome message



  Scenario: Registration with existing email
    Given a user with email "existing@example.com" already exists
    When I try to register with email "existing@example.com"
    And I provide other valid details:
      | fullName | Jane Smith          |
      | username | janesmith           |
      | password | password123         |
    And I submit the registration form
    Then I should see an error message "Email already exists"

  Scenario: Successful user login
    Given a registered user with:
      | username | testuser |
      | password | secret123 |
    When I login with username "testuser" and password "secret123"
    Then I should be logged in successfully
    And I should receive an authentication token



  Scenario: Login with invalid password
    Given a registered user with:
      | username | testuser |
      | password | correctpassword |
    When I login with username "testuser" and password "wrongpassword"
    Then I should see an error message "Invalid password"

