package com.bootcamp.group2.pages.mobile.login;

import com.bootcamp.group2.pages.BasePage;
import com.bootcamp.group2.utils.ConfigManager;
import com.bootcamp.group2.utils.WaitUtils;
import com.bootcamp.group2.pages.mobile.dashboard.MobileDashboardPage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

public class MobileLoginPage extends BasePage {

    private final By emailInput       = By.id("email");
    private final By passwordInput    = By.id("password");
    private final By submitButton     = By.cssSelector("button[type='submit']");
    private final By errorAlert       = By.cssSelector(".MuiAlert-root[role='alert']");
    private final By errorMessage     = By.cssSelector(".MuiAlert-message p");
    private final By forgotPasswordBtn = By.xpath("//button[normalize-space()='Lupa password ?']");

    @Step("Open mobile login page")
    public MobileLoginPage open() {
        navigateTo(ConfigManager.getMobileBaseUrl());
        return this;
    }

    @Step("Enter email: {email}")
    public MobileLoginPage enterEmail(String email) {
        type(emailInput, email);
        return this;
    }

    @Step("Enter password")
    public MobileLoginPage enterPassword(String password) {
        type(passwordInput, password);
        return this;
    }

    @Step("Click Login button")
    public MobileDashboardPage clickLogin() {
        click(submitButton);
        return new MobileDashboardPage();
    }

    @Step("Click Login button (expecting failure)")
    public MobileLoginPage clickLoginExpectFail() {
        click(submitButton);
        // Wait briefly for UI to respond (error alert or browser validation)
        try {
            WaitUtils.waitForVisible(driver, errorAlert, 5);
        } catch (Exception ignored) {
            // Error alert might not appear if browser does HTML5 validation
        }
        return this;
    }

    @Step("Login as {email}")
    public MobileDashboardPage loginAs(String email, String password) {
        return open()
            .enterEmail(email)
            .enterPassword(password)
            .clickLogin();
    }

    @Step("Click Forgot Password button")
    public MobileLoginPage clickForgotPassword() {
        click(forgotPasswordBtn);
        return this;
    }

    public String getErrorMessage() {
        return getText(errorMessage);
    }

    public boolean isErrorDisplayed() {
        // Check MUI Alert (server error)
        if (isDisplayed(errorAlert)) {
            return true;
        }
        // Check HTML5 browser validation on email field
        try {
            String validationMsg = (String) ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("return document.getElementById('email').validationMessage");
            return validationMsg != null && !validationMsg.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isForgotPasswordVisible() {
        return isDisplayed(forgotPasswordBtn);
    }

    public boolean isOnLoginPage() {
        return getCurrentUrl().contains("absen/login");
    }
}
