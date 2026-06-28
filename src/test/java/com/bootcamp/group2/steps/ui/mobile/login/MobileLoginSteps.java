package com.bootcamp.group2.steps.ui.mobile.login;

import com.bootcamp.group2.pages.mobile.dashboard.MobileDashboardPage;
import com.bootcamp.group2.pages.mobile.login.MobileLoginPage;
import com.bootcamp.group2.utils.ConfigManager;
import io.cucumber.java.en.*;
import org.assertj.core.api.Assertions;

public class MobileLoginSteps {

    private MobileLoginPage     loginPage;
    private MobileDashboardPage dashboardPage;

    @Given("user opens mobile login page")
    public void userOpensMobileLoginPage() {
        loginPage = new MobileLoginPage();
        loginPage.open();
    }

    @When("user enters mobile email {string} and password {string}")
    public void userEntersMobileEmailAndPassword(String email, String password) {
        loginPage.enterEmail(email).enterPassword(password);
    }

    @When("user enters valid mobile user credentials")
    public void userEntersValidMobileUserCredentials() {
        loginPage
            .enterEmail(ConfigManager.getValidEmail())
            .enterPassword(ConfigManager.getValidPassword());
    }

    @When("user enters valid mobile admin credentials")
    public void userEntersValidMobileAdminCredentials() {
        loginPage
            .enterEmail(ConfigManager.getAdminEmail())
            .enterPassword(ConfigManager.getAdminPassword());
    }

    @When("user clicks Mobile Login button")
    public void userClicksMobileLoginButton() {
        dashboardPage = loginPage.clickLogin();
    }

    @When("user clicks Mobile Login button expecting failure")
    public void userClicksMobileLoginButtonExpectingFailure() {
        loginPage.clickLoginExpectFail();
    }

    @When("user logs in using valid mobile credentials")
    public void userLogsInUsingValidMobileCredentials() {
        loginPage = new MobileLoginPage();
        dashboardPage = loginPage.loginAs(
            ConfigManager.getValidEmail(),
            ConfigManager.getValidPassword()
        );
    }

    @When("user clicks Forgot Password button")
    public void userClicksForgotPasswordButton() {
        loginPage.clickForgotPassword();
    }

    @Then("user successfully navigates to mobile dashboard")
    public void userSuccessfullyNavigatesToMobileDashboard() {
        Assertions.assertThat(dashboardPage.isDashboardLoaded())
            .as("After login, URL should not contain 'absen/login'")
            .isTrue();
    }

    @Then("URL does not contain mobile login page")
    public void urlDoesNotContainMobileLoginPage() {
        Assertions.assertThat(dashboardPage.getCurrentUrl())
            .as("URL after successful mobile login")
            .doesNotContain("absen/login");
    }

    @Then("mobile error message is displayed")
    public void mobileErrorMessageIsDisplayed() {
        Assertions.assertThat(loginPage.isErrorDisplayed())
            .as("Error message should be displayed upon failed mobile login")
            .isTrue();
    }

    @Then("user remains on mobile login page")
    public void userRemainsOnMobileLoginPage() {
        Assertions.assertThat(loginPage.isOnLoginPage())
            .as("User should remain on mobile login page")
            .isTrue();
    }

    @Then("Forgot Password button is visible on mobile login page")
    public void forgotPasswordButtonIsVisible() {
        Assertions.assertThat(loginPage.isForgotPasswordVisible())
            .as("'Lupa password ?' button should be visible on mobile login page")
            .isTrue();
    }

    @Then("mobile error message contains text {string}")
    public void mobileErrorMessageContainsText(String expectedText) {
        Assertions.assertThat(loginPage.getErrorMessage())
            .as("Mobile error message text")
            .containsIgnoringCase(expectedText);
    }
}
