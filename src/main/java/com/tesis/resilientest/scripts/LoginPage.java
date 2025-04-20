package com.tesis.resilientest.scripts;

import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LoginPage {

    private WebDriver driver;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public void verifyHeader() {
        WebElement header = driver.findElement(By.xpath("//*[@id=\"main\"]/div/div[2]/h1"));
        Assertions.assertThat(header.getText().trim()).isEqualTo("Customer Login");
    }

    public void fillCredentials(String email, String password) {
        WebElement inputEmail = driver.findElement(By.name("email"));
        WebElement inputPassword = driver.findElement(By.xpath("//*[@id=\"password\"]"));
        inputEmail.sendKeys(email);
        inputPassword.sendKeys(password);
    }

    public void submit() {
        WebElement submitButton = driver.findElement(By.xpath("//*[@id=\"main\"]/div/div[2]/div/form/div[4]/button"));
        submitButton.click();
    }
}
