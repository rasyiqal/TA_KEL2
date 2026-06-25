package com.bootcamp.group2.steps.ui.web.login;

import com.bootcamp.group2.pages.web.dashboard.WebDashboardPage;
import com.bootcamp.group2.pages.web.login.WebLoginPage;
import com.bootcamp.group2.utils.ConfigManager;
import io.cucumber.java.en.*;
import org.assertj.core.api.Assertions;

public class WebLoginSteps {

    private WebLoginPage     loginPage;
    private WebDashboardPage dashboardPage;

    @Given("user opens web login page")
    public void userOpensWebLoginPage() {
        loginPage = new WebLoginPage();
        loginPage.open();
    }

    @When("user enters email {string} and password {string}")
    public void userEntersEmailAndPassword(String email, String password) {
        loginPage.enterEmail(email).enterPassword(password);
    }

    @When("user enters valid credentials")
    public void userEntersValidCredentials() {
        loginPage
            .enterEmail(ConfigManager.getValidEmail())
            .enterPassword(ConfigManager.getValidPassword());
    }

    @When("user clicks Login button")
    public void userClicksLoginButton() {
        dashboardPage = loginPage.clickLogin();
    }

    @When("user clicks Login button expecting failure")
    public void userClicksLoginButtonExpectingFailure() {
        loginPage.clickLoginExpectFail();
    }

    @When("user logs in using valid credentials")
    public void userLogsInUsingValidCredentials() {
        loginPage = new WebLoginPage();
        dashboardPage = loginPage.loginAs(
            ConfigManager.getValidEmail(),
            ConfigManager.getValidPassword()
        );
    }

    @Then("user successfully navigates to web dashboard")
    public void userSuccessfullyNavigatesToWebDashboard() {
        Assertions.assertThat(dashboardPage.isDashboardLoaded())
            .as("After login, URL should not contain 'authentication/login'")
            .isTrue();
    }

    @Then("URL does not contain login page")
    public void urlDoesNotContainLoginPage() {
        Assertions.assertThat(dashboardPage.getCurrentUrl())
            .as("URL after successful login")
            .doesNotContain("authentication/login");
    }

    @Then("error message is displayed")
    public void errorMessageIsDisplayed() {
        Assertions.assertThat(loginPage.isErrorDisplayed())
            .as("Error message should be displayed upon failed login")
            .isTrue();
    }

    @Then("user remains on web login page")
    public void userRemainsOnWebLoginPage() {
        Assertions.assertThat(loginPage.isOnLoginPage())
            .as("User should remain on login page")
            .isTrue();
    }

    @Then("error message contains text {string}")
    public void errorMessageContainsText(String expectedText) {
        Assertions.assertThat(loginPage.getErrorMessage())
            .as("Error message text")
            .containsIgnoringCase(expectedText);
    }
}
