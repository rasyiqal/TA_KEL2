package com.bootcamp.group2.pages.web.login;

import com.bootcamp.group2.pages.BasePage;
import com.bootcamp.group2.utils.ConfigManager;
import com.bootcamp.group2.pages.web.dashboard.WebDashboardPage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.WebElement;
import com.bootcamp.group2.utils.WaitUtils;

public class WebLoginPage extends BasePage {

    private final By emailInput    = By.id("email");
    private final By passwordInput = By.id("password");
    private final By submitButton  = By.cssSelector("button[type='submit']");
    private final By errorAlert    = By.cssSelector(".MuiAlert-root[role='alert']");
    private final By errorMessage  = By.cssSelector(".MuiAlert-message p");

    @Step("Open web login page")
    public WebLoginPage open() {
        navigateTo(ConfigManager.getWebBaseUrl());
        return this;
    }

    @Step("Enter email: {email}")
    public WebLoginPage enterEmail(String email) {
        type(emailInput, email);
        return this;
    }

    @Step("Enter password")
    public WebLoginPage enterPassword(String password) {
        type(passwordInput, password);
        return this;
    }

    @Step("Click Login button")
    public WebDashboardPage clickLogin() {
        click(submitButton);
        return new WebDashboardPage();
    }

    @Step("Click Login button (expecting failure)")
    public WebLoginPage clickLoginExpectFail() {
        click(submitButton);
        try {
            WaitUtils.waitForVisible(driver, errorAlert, 5);
        } catch (Exception ignored) {
            // Error alert might not appear if browser does HTML5 validation
        }
        return this;
    }

    @Step("Login as {email}")
    public WebDashboardPage loginAs(String email, String password) {
        return open()
            .enterEmail(email)
            .enterPassword(password)
            .clickLogin();
    }

    public String getErrorMessage() {
        return getText(errorMessage);
    }

    public boolean isErrorDisplayed() {
        if (isDisplayed(errorAlert)) {
            return true;
        }
        try {
            String validationMsg = (String) ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("return document.getElementById('email').validationMessage");
            return validationMsg != null && !validationMsg.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isOnLoginPage() {
        return getCurrentUrl().contains("authentication/login");
    }
}
