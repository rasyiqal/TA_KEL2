package com.bootcamp.group2.steps.ui.web.approval;

import com.bootcamp.group2.pages.web.approval.WebLemburApprovalPage;
import com.bootcamp.group2.pages.web.dashboard.WebDashboardPage;
import com.bootcamp.group2.pages.web.login.WebLoginPage;
import com.bootcamp.group2.utils.ConfigManager;
import io.cucumber.java.en.*;
import org.assertj.core.api.Assertions;

public class WebLemburApprovalSteps {

    private WebDashboardPage       dashboardPage;
    private WebLemburApprovalPage  approvalPage;




    @Given("admin is on the lembur approval page for {string}")
    public void adminIsOnTheLemburApprovalPageFor(String employeeName) {
        dashboardPage = new WebLoginPage()
            .open()
            .enterEmail(ConfigManager.getAdminEmail())
            .enterPassword(ConfigManager.getAdminPassword())
            .clickLogin();

        dashboardPage.isDashboardLoaded();
        dashboardPage.navigateToPending();
        approvalPage = dashboardPage.clickLemburEmployeeLink(employeeName);
    }



    @When("admin approves the lembur submission for {string}")
    public void adminApprovesTheLemburSubmissionFor(String employeeName) {
        approvalPage.approveLembur(employeeName);
    }

    @When("admin rejects the lembur submission for {string} with notes {string}")
    public void adminRejectsTheLemburSubmissionFor(String employeeName, String rejectNotes) {
        approvalPage.rejectLembur(employeeName, rejectNotes);
    }

    @When("admin clicks the PENDING badge for {string}")
    public void adminClicksThePendingBadgeFor(String employeeName) {
        approvalPage.clickPendingBadgeForEmployee(employeeName);
    }

    @When("admin clicks the Approve button in the modal")
    public void adminClicksTheApproveButtonInTheModal() {
        approvalPage.clickApproveButton();
        approvalPage.confirmModal();
    }

    @When("admin clicks the Reject button in the modal")
    public void adminClicksTheRejectButtonInTheModal() {
        approvalPage.clickRejectButton();
    }

    @When("admin enters reject notes {string}")
    public void adminEntersRejectNotes(String notes) {
        approvalPage.enterRejectNotes(notes);
        approvalPage.confirmModal();
    }

    @When("the lembur approval page is refreshed")
    public void theLemburApprovalPageIsRefreshed() {
        approvalPage.refresh();
    }



    @Then("the lembur approval page is loaded for {string}")
    public void theLemburApprovalPageIsLoadedFor(String employeeName) {
        Assertions.assertThat(approvalPage.isPageLoaded())
            .as("Lembur approval page should be loaded")
            .isTrue();
    }

    @Then("the lembur table shows PENDING items")
    public void theLemburTableShowsPendingItems() {
        Assertions.assertThat(approvalPage.hasPendingItems())
            .as("Lembur table should show at least one PENDING item")
            .isTrue();
        Assertions.assertThat(approvalPage.getPendingCount())
            .as("PENDING count should be greater than 0")
            .isGreaterThan(0);
    }

    @Then("the lembur table shows {int} or more rows")
    public void theLemburTableShowsOrMoreRows(int expectedMin) {
        Assertions.assertThat(approvalPage.getTableRowCount())
            .as("Lembur table should have at least %d rows", expectedMin)
            .isGreaterThanOrEqualTo(expectedMin);
    }

    @Then("the approval modal is displayed")
    public void theApprovalModalIsDisplayed() {
        Assertions.assertThat(approvalPage.isApprovalModalVisible())
            .as("Approval modal should be displayed after clicking PENDING badge")
            .isTrue();
    }

    @Then("the lembur status for {string} changes to Approved")
    public void theLemburStatusForChangesToApproved(String employeeName) {
        approvalPage.waitForStatusChange();
        Assertions.assertThat(approvalPage.isStatusApproved(employeeName))
            .as("Lembur status for '%s' should be Approved", employeeName)
            .isTrue();
    }

    @Then("the lembur status for {string} changes to Rejected")
    public void theLemburStatusForChangesToRejected(String employeeName) {
        approvalPage.waitForStatusChange();
        Assertions.assertThat(approvalPage.isStatusRejected(employeeName))
            .as("Lembur status for '%s' should be Rejected", employeeName)
            .isTrue();
    }

    @Then("the lembur status for {string} remains PENDING")
    public void theLemburStatusForRemainsPending(String employeeName) {
        Assertions.assertThat(approvalPage.isStatusPending(employeeName))
            .as("Lembur status for '%s' should still be PENDING", employeeName)
            .isTrue();
    }
}
