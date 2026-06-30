package com.bootcamp.group2.steps.ui.e2e;

import com.bootcamp.group2.pages.mobile.dashboard.MobileDashboardPage;
import com.bootcamp.group2.pages.mobile.koreksiabsen.MobileKoreksiAbsenPage;
import com.bootcamp.group2.pages.mobile.login.MobileLoginPage;
import com.bootcamp.group2.pages.web.dashboard.WebDashboardPage;
import com.bootcamp.group2.pages.web.laporan.WebLaporanKoreksiPage;
import com.bootcamp.group2.pages.web.login.WebLoginPage;
import com.bootcamp.group2.utils.ConfigManager;
import com.bootcamp.group2.utils.DriverFactory;
import io.cucumber.java.en.*;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.Dimension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class E2EKoreksiSteps {

    private static final Logger log = LoggerFactory.getLogger(E2EKoreksiSteps.class);

    private MobileKoreksiAbsenPage koreksiPage;
    private MobileDashboardPage    mobileDashboard;
    private WebDashboardPage       webDashboard;
    private WebLaporanKoreksiPage  laporanKoreksiPage;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd MM yyyy");
    private static String today() { return LocalDate.now().format(DATE_FMT); }



    private void switchToMobileView() {
        DriverFactory.getDriver().manage().window().setSize(new Dimension(390, 844));
    }

    private void switchToDesktopView() {
        DriverFactory.getDriver().manage().window().maximize();
    }


    @Given("a regular user submits a new koreksi absen request on mobile")
    public void aRegularUserSubmitsANewKoreksiAbsenRequestOnMobile() {
        switchToMobileView();

        mobileDashboard = new MobileLoginPage()
            .open()
            .enterEmail(ConfigManager.getValidEmail())
            .enterPassword(ConfigManager.getValidPassword())
            .clickLogin();

        mobileDashboard.isDashboardLoaded();
        koreksiPage = mobileDashboard.clickKoreksiAbsen();

        Assertions.assertThat(koreksiPage.isPageLoaded())
            .as("Koreksi absen page should be loaded before submitting")
            .isTrue();

        koreksiPage.clickAjukanKoreksi();
        koreksiPage.enterJamMasuk(today() + ", 08:00");
        koreksiPage.enterJamKeluar(today() + ", 17:00");
        koreksiPage.clickAjukan();

        if (koreksiPage.isDuplicateSubmissionAlertVisible()) {
            log.info("Duplicate koreksi detected for today — existing PENDING submission will be used for E2E flow");
            koreksiPage.dismissDuplicateAlert();
        } else {
            Assertions.assertThat(koreksiPage.isDrawerClosed())
                .as("Koreksi submission drawer should close after successful submission")
                .isTrue();
        }
    }


    @When("admin approves the submitted koreksi for {string}")
    public void adminApprovesTheSubmittedKoreksiFor(String employeeName) {
        navigateToLaporanKoreksi();
        laporanKoreksiPage.approveFirstPendingKoreksiFor(employeeName);
    }


    @When("admin rejects the submitted koreksi for {string} with reason {string}")
    public void adminRejectsTheSubmittedKoreksiForWithReason(String employeeName, String reason) {
        navigateToLaporanKoreksi();
        laporanKoreksiPage.rejectFirstPendingKoreksiFor(employeeName, reason);
    }



    @Then("the admin sees the koreksi approval success message")
    public void theAdminSeesTheKoreksiApprovalSuccessMessage() {
        String message = laporanKoreksiPage.getSuccessAlertMessage();
        log.debug("Koreksi approval alert: {}", message);
        Assertions.assertThat(laporanKoreksiPage.isApprovalSuccessVisible())
            .as("Approval success alert 'Berhasil menyetujui koreksi absen' should be visible. Found: '%s'", message)
            .isTrue();
    }

    @Then("the admin sees the koreksi rejection success message")
    public void theAdminSeesTheKoreksiRejectionSuccessMessage() {
        String message = laporanKoreksiPage.getSuccessAlertMessage();
        log.debug("Koreksi rejection alert: {}", message);
        Assertions.assertThat(laporanKoreksiPage.isRejectionSuccessVisible())
            .as("Rejection success alert 'Berhasil menolak permintaan koreksi absen' should be visible. Found: '%s'", message)
            .isTrue();
    }


    @Then("the regular user verifies the koreksi request status changed to {string} on mobile")
    public void theRegularUserVerifiesTheKoreksiRequestStatusChangedTo(String expectedStatus) {

        switchToMobileView();
        mobileDashboard = new MobileLoginPage()
            .open()
            .enterEmail(ConfigManager.getValidEmail())
            .enterPassword(ConfigManager.getValidPassword())
            .clickLogin();

        mobileDashboard.isDashboardLoaded();
        koreksiPage = mobileDashboard.clickKoreksiAbsen();

        String actualStatus = koreksiPage.getFirstCardStatusText();
        log.debug("Koreksi card status after admin action '{}': {}", expectedStatus, actualStatus);

        boolean matches;
        if ("Approved".equalsIgnoreCase(expectedStatus)) {
            matches = koreksiPage.isFirstCardStatusApproved();
        } else if ("Rejected".equalsIgnoreCase(expectedStatus)) {
            matches = koreksiPage.isFirstCardStatusRejected();
        } else {
            matches = actualStatus.toLowerCase().contains(expectedStatus.toLowerCase());
        }

        Assertions.assertThat(matches)
            .as("Koreksi card status should be '%s' after admin action. Found: '%s'", expectedStatus, actualStatus)
            .isTrue();
    }




    private void navigateToLaporanKoreksi() {

        mobileDashboard = koreksiPage.clickKembali();
        mobileDashboard.logout();


        switchToDesktopView();

        webDashboard = new WebLoginPage()
            .open()
            .enterEmail(ConfigManager.getAdminEmail())
            .enterPassword(ConfigManager.getAdminPassword())
            .clickLogin();

        Assertions.assertThat(webDashboard.isDashboardLoaded())
            .as("Admin should land on /dashboards/pending after login")
            .isTrue();


        webDashboard.clickLaporanMenu();
        laporanKoreksiPage = webDashboard.clickLaporanKoreksiSubMenu();

        Assertions.assertThat(laporanKoreksiPage.isPageLoaded())
            .as("Laporan Koreksi page should be loaded after clicking sub-menu")
            .isTrue();
    }
}
