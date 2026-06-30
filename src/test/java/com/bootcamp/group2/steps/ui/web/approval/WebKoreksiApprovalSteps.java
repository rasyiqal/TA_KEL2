package com.bootcamp.group2.steps.ui.web.approval;

import com.bootcamp.group2.pages.web.approval.WebKoreksiApprovalPage;
import com.bootcamp.group2.pages.web.dashboard.WebDashboardPage;
import com.bootcamp.group2.pages.web.login.WebLoginPage;
import com.bootcamp.group2.utils.ConfigManager;
import io.cucumber.java.en.*;
import org.assertj.core.api.Assertions;

public class WebKoreksiApprovalSteps {

    private WebDashboardPage        dashboardPage;
    private WebKoreksiApprovalPage  approvalPage;




    @Given("admin is on the koreksi approval page for {string}")
    public void adminIsOnTheKoreksiApprovalPageFor(String employeeName) {
        dashboardPage = new WebLoginPage()
            .open()
            .enterEmail(ConfigManager.getAdminEmail())
            .enterPassword(ConfigManager.getAdminPassword())
            .clickLogin();

        dashboardPage.isDashboardLoaded();
        dashboardPage.navigateToPending();
        approvalPage = dashboardPage.clickKoreksiEmployeeLink(employeeName);
    }



    @When("admin approves the koreksi submission for {string}")
    public void adminApprovesTheKoreksiSubmissionFor(String employeeName) {
        approvalPage.approveKoreksi(employeeName);
    }

    @When("admin rejects the koreksi submission for {string} with notes {string}")
    public void adminRejectsTheKoreksiSubmissionFor(String employeeName, String rejectNotes) {
        approvalPage.rejectKoreksi(employeeName, rejectNotes);
    }

    @When("admin clicks the PENDING koreksi badge for {string}")
    public void adminClicksThePendingKoreksiBadgeFor(String employeeName) {
        approvalPage.clickPendingBadgeForEmployee(employeeName);
    }

    @When("admin approves the koreksi in the modal")
    public void adminApprovesTheKoreksiInTheModal() {
        approvalPage.clickApproveButton();
        approvalPage.confirmModal();
    }

    @When("admin rejects the koreksi in the modal with notes {string}")
    public void adminRejectsTheKoreksiInTheModalWithNotes(String notes) {
        approvalPage.clickRejectButton();
        approvalPage.enterRejectNotes(notes);
        approvalPage.confirmModal();
    }

    @When("the koreksi approval page is refreshed")
    public void theKoreksiApprovalPageIsRefreshed() {
        approvalPage.refresh();
    }



    @Then("the koreksi approval page is loaded for {string}")
    public void theKoreksiApprovalPageIsLoadedFor(String employeeName) {
        Assertions.assertThat(approvalPage.isPageLoaded())
            .as("Koreksi approval page should be loaded")
            .isTrue();
    }

    @Then("the koreksi table shows PENDING items")
    public void theKoreksiTableShowsPendingItems() {
        Assertions.assertThat(approvalPage.hasPendingItems())
            .as("Koreksi table should show at least one PENDING item")
            .isTrue();
        Assertions.assertThat(approvalPage.getPendingCount())
            .as("PENDING count should be greater than 0")
            .isGreaterThan(0);
    }

    @Then("the koreksi table shows {int} or more rows")
    public void theKoreksiTableShowsOrMoreRows(int expectedMin) {
        Assertions.assertThat(approvalPage.getTableRowCount())
            .as("Koreksi table should have at least %d rows", expectedMin)
            .isGreaterThanOrEqualTo(expectedMin);
    }

    @Then("the koreksi approval modal is displayed")
    public void theKoreksiApprovalModalIsDisplayed() {
        Assertions.assertThat(approvalPage.isApprovalModalVisible())
            .as("Approval modal should be displayed after clicking PENDING badge")
            .isTrue();
    }

    @Then("the koreksi status for {string} changes to Approved")
    public void theKoreksiStatusForChangesToApproved(String employeeName) {
        approvalPage.waitForStatusChange();
        Assertions.assertThat(approvalPage.isStatusApproved(employeeName))
            .as("Koreksi status for '%s' should be Approved", employeeName)
            .isTrue();
    }

    @Then("the koreksi status for {string} changes to Rejected")
    public void theKoreksiStatusForChangesToRejected(String employeeName) {
        approvalPage.waitForStatusChange();
        Assertions.assertThat(approvalPage.isStatusRejected(employeeName))
            .as("Koreksi status for '%s' should be Rejected", employeeName)
            .isTrue();
    }

    @Then("the koreksi status for {string} remains PENDING")
    public void theKoreksiStatusForRemainsPending(String employeeName) {
        Assertions.assertThat(approvalPage.isStatusPending(employeeName))
            .as("Koreksi status for '%s' should still be PENDING", employeeName)
            .isTrue();
    }
}
