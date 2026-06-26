package com.bootcamp.group2.steps.ui.web.login;

import com.bootcamp.group2.pages.web.dashboard.WebDashboardPage;
import com.bootcamp.group2.pages.web.login.WebLoginPage;
import com.bootcamp.group2.utils.ConfigManager;
import io.cucumber.java.en.*;
import org.assertj.core.api.Assertions;

public class WebLoginSteps {

    private WebLoginPage     loginPage;
    private WebDashboardPage dashboardPage;

    @Given("user is on the web login page")
    public void userIsOnTheWebLoginPage() {
        loginPage = new WebLoginPage();
        loginPage.open();
    }

    @When("user logs in with valid credentials")
    public void userLogsInWithValidCredentials() {
        dashboardPage = loginPage
            .enterEmail(ConfigManager.getValidEmail())
            .enterPassword(ConfigManager.getValidPassword())
            .clickLogin();
    }

    @When("user attempts web login with email {string} and password {string}")
    public void userAttemptsWebLoginWithEmailAndPassword(String email, String password) {
        loginPage.enterEmail(email).enterPassword(password).clickLoginExpectFail();
    }

    @Then("user is redirected to the web dashboard")
    public void userIsRedirectedToTheWebDashboard() {
        Assertions.assertThat(dashboardPage.isDashboardLoaded())
            .as("User should be redirected to web dashboard after successful login")
            .isTrue();
        Assertions.assertThat(dashboardPage.getCurrentUrl())
            .as("URL should not contain login path after successful login")
            .doesNotContain("authentication/login");
    }

    @Then("login fails and an error message is displayed")
    public void loginFailsAndAnErrorMessageIsDisplayed() {
        Assertions.assertThat(loginPage.isErrorDisplayed())
            .as("An error message should be displayed after failed login")
            .isTrue();
    }

    @Then("user remains on the web login page")
    public void userRemainsOnTheWebLoginPage() {
        Assertions.assertThat(loginPage.isOnLoginPage())
            .as("User should remain on the web login page")
            .isTrue();
    }

    @Then("error message contains text {string}")
    public void errorMessageContainsText(String expectedText) {
        Assertions.assertThat(loginPage.getErrorMessage())
            .as("Error message text")
            .containsIgnoringCase(expectedText);
    }
}
