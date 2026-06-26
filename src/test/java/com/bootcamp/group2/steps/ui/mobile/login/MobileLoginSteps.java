package com.bootcamp.group2.steps.ui.mobile.login;

import com.bootcamp.group2.pages.mobile.dashboard.MobileDashboardPage;
import com.bootcamp.group2.pages.mobile.login.MobileLoginPage;
import com.bootcamp.group2.utils.ConfigManager;
import io.cucumber.java.en.*;
import org.assertj.core.api.Assertions;

public class MobileLoginSteps {

    private MobileLoginPage     loginPage;
    private MobileDashboardPage dashboardPage;

    @Given("user is on the mobile login page")
    public void userIsOnTheMobileLoginPage() {
        loginPage = new MobileLoginPage();
        loginPage.open();
    }

    @When("user logs in with valid mobile credentials")
    public void userLogsInWithValidMobileCredentials() {
        dashboardPage = loginPage
            .enterEmail(ConfigManager.getValidEmail())
            .enterPassword(ConfigManager.getValidPassword())
            .clickLogin();
    }

    @When("user attempts mobile login with email {string} and password {string}")
    public void userAttemptsMobileLoginWithEmailAndPassword(String email, String password) {
        loginPage.enterEmail(email).enterPassword(password).clickLoginExpectFail();
    }

    @Then("user is redirected to the mobile home page")
    public void userIsRedirectedToTheMobileHomePage() {
        Assertions.assertThat(dashboardPage.isDashboardLoaded())
            .as("User should be redirected to mobile home page after successful login")
            .isTrue();
        Assertions.assertThat(dashboardPage.getCurrentUrl())
            .as("URL should not contain mobile login path after successful login")
            .doesNotContain("absen/login");
    }

    @Then("the Forgot Password button is visible")
    public void theForgotPasswordButtonIsVisible() {
        Assertions.assertThat(loginPage.isForgotPasswordVisible())
            .as("'Lupa password ?' button should be visible on mobile login page")
            .isTrue();
    }

    @Then("mobile login fails and an error message is displayed")
    public void mobileLoginFailsAndAnErrorMessageIsDisplayed() {
        Assertions.assertThat(loginPage.isErrorDisplayed())
            .as("An error message should be displayed after failed mobile login")
            .isTrue();
    }

    @Then("user remains on the mobile login page")
    public void userRemainsOnTheMobileLoginPage() {
        Assertions.assertThat(loginPage.isOnLoginPage())
            .as("User should remain on the mobile login page")
            .isTrue();
    }

    @Then("mobile error message contains text {string}")
    public void mobileErrorMessageContainsText(String expectedText) {
        Assertions.assertThat(loginPage.getErrorMessage())
            .as("Mobile error message text")
            .containsIgnoringCase(expectedText);
    }
}
