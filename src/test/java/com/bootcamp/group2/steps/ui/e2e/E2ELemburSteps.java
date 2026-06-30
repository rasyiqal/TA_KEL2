package com.bootcamp.group2.steps.ui.e2e;

import com.bootcamp.group2.pages.mobile.dashboard.MobileDashboardPage;
import com.bootcamp.group2.pages.mobile.lembur.MobileLemburPage;
import com.bootcamp.group2.pages.mobile.login.MobileLoginPage;
import com.bootcamp.group2.pages.web.dashboard.WebDashboardPage;
import com.bootcamp.group2.pages.web.laporan.WebLaporanLemburPage;
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


public class E2ELemburSteps {

    private static final Logger log = LoggerFactory.getLogger(E2ELemburSteps.class);

    private MobileLemburPage      lemburPage;
    private MobileDashboardPage   mobileDashboard;
    private WebDashboardPage      webDashboard;
    private WebLaporanLemburPage  laporanLemburPage;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd MM yyyy");
    private static String today() { return LocalDate.now().format(DATE_FMT); }



    private void switchToMobileView() {
        DriverFactory.getDriver().manage().window().setSize(new Dimension(390, 844));
    }

    private void switchToDesktopView() {
        DriverFactory.getDriver().manage().window().maximize();
    }


    @Given("a regular user submits a new lembur request on mobile")
    public void aRegularUserSubmitsANewLemburRequestOnMobile() {
        switchToMobileView();

        mobileDashboard = new MobileLoginPage()
            .open()
            .enterEmail(ConfigManager.getValidEmail())
            .enterPassword(ConfigManager.getValidPassword())
            .clickLogin();

        mobileDashboard.isDashboardLoaded();
        lemburPage = mobileDashboard.clickLembur();

        Assertions.assertThat(lemburPage.isPageLoaded())
            .as("Lembur page should be loaded before submitting")
            .isTrue();

        lemburPage.clickAjukanLembur();
        lemburPage.enterJamMasuk(today() + ", 08:00");
        lemburPage.enterJamKeluar(today() + ", 17:00");
        lemburPage.enterCatatan("E2E automated lembur submission");
        lemburPage.clickAjukan();

        log.debug("Drawer text after submit: {}", lemburPage.getDrawerText());

        if (lemburPage.isDuplicateSubmissionAlertVisible()) {
            log.info("Duplicate lembur detected for today — existing PENDING submission will be used for E2E flow");
            lemburPage.dismissDuplicateAlert();
        } else {
            Assertions.assertThat(lemburPage.isDrawerClosed())
                .as("Lembur submission drawer should close after successful submission")
                .isTrue();
        }
    }


        mobileDashboard = lemburPage.clickKembali();
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
        laporanLemburPage = webDashboard.clickLaporanLemburSubMenu();

        Assertions.assertThat(laporanLemburPage.isPageLoaded())
            .as("Laporan Lembur page should be loaded after clicking sub-menu")
            .isTrue();
    }


    @Then("the lembur submission appears in the report list with status {string}")
    public void theLemburSubmissionAppearsInTheReportListWithStatus(String expectedStatus) {
        String employeeName = "Hadir SQA Testing 1";
        String actualStatus = laporanLemburPage.getFirstSubmissionStatus(employeeName);
        log.debug("Lembur report status for '{}': {}", employeeName, actualStatus);

        Assertions.assertThat(laporanLemburPage.isSubmissionPresentWithStatus(employeeName, expectedStatus))
            .as("Lembur report should contain a row for '%s' with status '%s'. Found: '%s'",
                employeeName, expectedStatus, actualStatus)
            .isTrue();
    }


    @Then("the lembur submission appears in the list as PENDING before approval")
    public void theLemburSubmissionAppearsInTheListAsPendingBeforeApproval() {
        lemburPage.refresh();
        String statusText = lemburPage.getFirstCardStatusText();
        log.debug("Lembur card status before approval: {}", statusText);

        Assertions.assertThat(lemburPage.isFirstCardStatusPending())
            .as("Newly submitted lembur should show PENDING before admin approval. Found: '%s'", statusText)
            .isTrue();
    }
}
