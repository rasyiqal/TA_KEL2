package com.bootcamp.group2.steps.ui.mobile.koreksiabsen;

import com.bootcamp.group2.pages.mobile.dashboard.MobileDashboardPage;
import com.bootcamp.group2.pages.mobile.koreksiabsen.MobileKoreksiAbsenPage;
import com.bootcamp.group2.pages.mobile.login.MobileLoginPage;
import com.bootcamp.group2.utils.ConfigManager;
import io.cucumber.java.en.*;
import org.assertj.core.api.Assertions;

public class MobileKoreksiAbsenSteps {

    private MobileKoreksiAbsenPage koreksiAbsenPage;
    private int totalKoreksiSebelum;

    @Given("user is logged in and on the koreksi absen page")
    public void userIsLoggedInAndOnTheKoreksiAbsenPage() {
        MobileDashboardPage dashboardPage = new MobileLoginPage()
            .open()
            .enterEmail(ConfigManager.getValidEmail())
            .enterPassword(ConfigManager.getValidPassword())
            .clickLogin();

        dashboardPage.isDashboardLoaded();
        koreksiAbsenPage = dashboardPage.clickKoreksiAbsen();
    }

    @Given("user notes the current total koreksi")
    public void userNotesTheCurrentTotalKoreksi() {
        totalKoreksiSebelum = koreksiAbsenPage.getTotalKoreksiCount();
    }

    @When("user opens the koreksi submission form")
    public void userOpensTheKoreksiSubmissionForm() {
        koreksiAbsenPage.clickAjukanKoreksi();
    }

    @When("user submits a koreksi with Jam masuk {string}")
    public void userSubmitsKoreksiWithJamMasuk(String jamMasuk) {
        koreksiAbsenPage.clickAjukanKoreksi();
        koreksiAbsenPage.enterJamMasuk(jamMasuk);
        koreksiAbsenPage.clickAjukan();
    }

    @When("user submits a koreksi with Jam keluar {string}")
    public void userSubmitsKoreksiWithJamKeluar(String jamKeluar) {
        koreksiAbsenPage.clickAjukanKoreksi();
        koreksiAbsenPage.enterJamKeluar(jamKeluar);
        koreksiAbsenPage.clickAjukan();
    }

    @When("user submits a koreksi with Jam masuk {string} and Jam keluar {string}")
    public void userSubmitsKoreksiWithJamMasukAndJamKeluar(String jamMasuk, String jamKeluar) {
        koreksiAbsenPage.clickAjukanKoreksi();
        koreksiAbsenPage.enterJamMasuk(jamMasuk);
        koreksiAbsenPage.enterJamKeluar(jamKeluar);
        koreksiAbsenPage.clickAjukan();
    }

    @When("user submits a koreksi without filling any fields")
    public void userSubmitsKoreksiWithoutFillingAnyFields() {
        koreksiAbsenPage.clickAjukanKoreksi();
        koreksiAbsenPage.clickAjukanExpectFail();
    }

    @When("user fills and resets the koreksi form")
    public void userFillsAndResetsTheKoreksiForm() {
        koreksiAbsenPage.clickAjukanKoreksi();
        koreksiAbsenPage.enterJamMasuk("22 06 2026, 09:00");
        koreksiAbsenPage.enterJamKeluar("22 06 2026, 18:00");
        koreksiAbsenPage.clickReset();
    }

    @When("user navigates back from the koreksi absen page")
    public void userNavigatesBackFromTheKoreksiAbsenPage() {
        koreksiAbsenPage.clickKembali();
    }

    @Then("koreksi absen page displays the correction list with total count")
    public void koreksiAbsenPageDisplaysListWithTotalCount() {
        Assertions.assertThat(koreksiAbsenPage.isPageLoaded())
            .as("Page title 'Halaman Koreksi' should be displayed")
            .isTrue();
        Assertions.assertThat(koreksiAbsenPage.isListKoreksiVisible())
            .as("'List Koreksi' section should be visible")
            .isTrue();
        Assertions.assertThat(koreksiAbsenPage.isTotalKoreksiVisible())
            .as("Total koreksi count should be displayed")
            .isTrue();
        Assertions.assertThat(koreksiAbsenPage.getTotalKoreksiCount())
            .as("Total koreksi should be a valid number >= 0")
            .isGreaterThanOrEqualTo(0);
    }

    @Then("koreksi absen submission form is displayed")
    public void koreksiAbsenSubmissionFormIsDisplayed() {
        Assertions.assertThat(koreksiAbsenPage.isDrawerOpen())
            .as("Koreksi submission form (drawer) should be open")
            .isTrue();
    }

    @Then("the koreksi is successfully submitted")
    public void theKoreksiIsSuccessfullySubmitted() {
        Assertions.assertThat(koreksiAbsenPage.isDrawerClosed())
            .as("Drawer should close after successful submission")
            .isTrue();
        Assertions.assertThat(koreksiAbsenPage.isOnKoreksiAbsenPage())
            .as("User should remain on the koreksi absen page after submission")
            .isTrue();
    }

    @Then("the latest koreksi item appears in the list")
    public void theLatestKoreksiItemAppearsInTheList() {
        Assertions.assertThat(koreksiAbsenPage.isCorrectionListVisible())
            .as("Correction list should be visible after submission")
            .isTrue();
        Assertions.assertThat(koreksiAbsenPage.isFirstCardHasJamMasukInfo())
            .as("The latest item should display Jam masuk information")
            .isTrue();
    }

    @Then("total koreksi increases by 1")
    public void totalKoreksiIncreasesBy1() {
        int totalSesudah = koreksiAbsenPage.getTotalKoreksiCount();
        Assertions.assertThat(totalSesudah)
            .as("Total koreksi should increase from %d to at least %d (accounting for parallel execution in staging)", totalKoreksiSebelum, totalKoreksiSebelum + 1)
            .isGreaterThan(totalKoreksiSebelum);
    }

    @Then("the koreksi list displays complete item information")
    public void theKoreksiListDisplaysCompleteItemInformation() {
        Assertions.assertThat(koreksiAbsenPage.isFirstCardHasUserName())
            .as("Item should display user's name")
            .isTrue();
        Assertions.assertThat(koreksiAbsenPage.isFirstCardHasJamMasukInfo())
            .as("Item should display Jam masuk information")
            .isTrue();
        Assertions.assertThat(koreksiAbsenPage.isFirstCardHasJamKeluarInfo())
            .as("Item should display Jam keluar information")
            .isTrue();
        Assertions.assertThat(koreksiAbsenPage.isFirstCardHasStatusInfo())
            .as("Item should display status information")
            .isTrue();
    }

    @Then("koreksi submission fails with error message {string}")
    public void koreksiSubmissionFailsWithErrorMessage(String expectedError) {
        Assertions.assertThat(koreksiAbsenPage.isErrorDisplayed())
            .as("An error message should appear after submitting without filling fields")
            .isTrue();
        Assertions.assertThat(koreksiAbsenPage.getErrorMessageText())
            .as("Error message text should match expected")
            .isEqualTo(expectedError);
    }

    @Then("the submission form remains open")
    public void theSubmissionFormRemainsOpen() {
        Assertions.assertThat(koreksiAbsenPage.isDrawerOpen())
            .as("Koreksi submission form should remain open after validation error")
            .isTrue();
    }

    @Then("all koreksi form fields are empty")
    public void allKoreksiFormFieldsAreEmpty() {
        Assertions.assertThat(koreksiAbsenPage.isJamMasukEmpty())
            .as("Jam masuk field should be empty after reset")
            .isTrue();
        Assertions.assertThat(koreksiAbsenPage.isJamKeluarEmpty())
            .as("Jam keluar field should be empty after reset")
            .isTrue();
    }

    @Then("user is no longer on the koreksi absen page")
    public void userIsNoLongerOnTheKoreksiAbsenPage() {
        Assertions.assertThat(koreksiAbsenPage.isOnKoreksiAbsenPage())
            .as("User should have navigated away from the koreksi absen page")
            .isFalse();
    }
}
