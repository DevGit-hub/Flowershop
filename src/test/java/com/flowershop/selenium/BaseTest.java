package com.flowershop.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

public class BaseTest {
    protected WebDriver driver;
    protected String baseUrl = "http://localhost:5173"; // Your React frontend

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-popup-blocking");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(15));

        System.out.println("WebDriver started - testing URL: " + baseUrl);
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            System.out.println("WebDriver closed");
        }
    }
}