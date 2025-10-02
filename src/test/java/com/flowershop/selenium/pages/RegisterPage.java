package com.flowershop.selenium.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class RegisterPage {
    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(id = "fullName")
    private WebElement fullNameInput;

    @FindBy(id = "username")
    private WebElement usernameInput;

    @FindBy(id = "email")
    private WebElement emailInput;

    @FindBy(id = "password")
    private WebElement passwordInput;

    @FindBy(xpath = "//button[contains(text(),'Create Account')]")
    private WebElement registerButton;

    // Error message block
    @FindBy(xpath = "//div[contains(@class, 'bg-red-100')]")
    private WebElement errorMessage;

    public RegisterPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    public void enterFullName(String fullName) {
        wait.until(ExpectedConditions.visibilityOf(fullNameInput));
        fullNameInput.clear();
        fullNameInput.sendKeys(fullName);
    }

    public void enterUsername(String username) {
        wait.until(ExpectedConditions.visibilityOf(usernameInput));
        usernameInput.clear();
        usernameInput.sendKeys(username);
    }

    public void enterEmail(String email) {
        wait.until(ExpectedConditions.visibilityOf(emailInput));
        emailInput.clear();
        emailInput.sendKeys(email);
    }

    public void enterPassword(String password) {
        wait.until(ExpectedConditions.visibilityOf(passwordInput));
        passwordInput.clear();
        passwordInput.sendKeys(password);
    }

    public void clickRegister() {
        wait.until(ExpectedConditions.elementToBeClickable(registerButton));
        registerButton.click();
    }

    public boolean isErrorMessageDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(errorMessage));
            return errorMessage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getErrorMessage() {
        try {
            wait.until(ExpectedConditions.visibilityOf(errorMessage));
            return errorMessage.getText();
        } catch (Exception e) {
            return "No error message found";
        }
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}
