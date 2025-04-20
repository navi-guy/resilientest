package com.tesis.resilientest.scripts;

import com.tesis.resilientest.resilient.ResilientWebDriver;
import io.qameta.allure.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

import java.time.Duration;


public class LoginPageTest {

    private WebDriver driver;

    @Step("Open Login page")
    @BeforeEach
    public void setUp() {
        driver = new ResilientWebDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get("http://bagisto-v1.test/customer/login");// Version 1
   //     driver.get("http://bagisto-v2.test/customer/login");//  Version 2
    }

    @Step("Close Login page")
    @AfterEach
    public void tearDown() {
        driver.quit();
    }

    @Step("Login process")
    @DisplayName("Test Authentication")
    @Description("This test attempts to log into the website using a login and a password. Fails if any error happens.\n\nNote that this test does not test 2-Factor Authentication.")
    @Severity(SeverityLevel.CRITICAL)
    @Owner("Henry Aliaga")
    @Test
    public void login() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.verifyHeader();
        loginPage.fillCredentials("navi@gmail.com", "Prueba123");
        loginPage.submit();
    }
}
