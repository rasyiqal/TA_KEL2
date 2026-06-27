package com.bootcamp.group2.steps.ui.mobile.lembur;

import com.bootcamp.group2.pages.mobile.dashboard.MobileDashboardPage;
import com.bootcamp.group2.pages.mobile.lembur.MobileLemburPage;
import com.bootcamp.group2.pages.mobile.login.MobileLoginPage;
import com.bootcamp.group2.utils.ConfigManager;
import io.cucumber.java.en.*;
import org.assertj.core.api.Assertions;

public class MobileLemburSteps {

    private MobileLemburPage lemburPage;
    private int totalLemburSebelum;

    @Given("user is logged in and on the lembur page")
    public void userIsLoggedInAndOnTheLemburPage() {
        MobileDashboardPage dashboardPage = new MobileLoginPage()
            .open()
            .enterEmail(ConfigManager.getValidEmail())
            .enterPassword(ConfigManager.getValidPassword())
            .clickLogin();

        dashboardPage.isDashboardLoaded();
        lemburPage = dashboardPage.clickLembur();
    }

    @Given("user notes the current total lembur")
    public void userNotesTheCurrentTotalLembur() {
        totalLemburSebelum = lemburPage.getTotalLemburCount();
    }

    @When("user opens the lembur submission form")
    public void userOpensTheLemburSubmissionForm() {
        lemburPage.clickAjukanLembur();
    }

    @When("user submits lembur with jam masuk {string}, jam keluar {string}, and catatan {string}")
    public void userSubmitsLemburWithAllFields(String jamMasuk, String jamKeluar, String catatan) {
        lemburPage.clickAjukanLembur();
        lemburPage.enterJamMasuk(jamMasuk);
        lemburPage.enterJamKeluar(jamKeluar);
        lemburPage.enterCatatan(catatan);
        lemburPage.clickAjukan();
    }

    @When("user submits lembur without jam masuk")
    public void userSubmitsLemburWithoutJamMasuk() {
        lemburPage.clickAjukanLembur();
        lemburPage.enterJamKeluar("26 06 2026, 17:00");
        lemburPage.enterCatatan("Testing lembur valid");
        lemburPage.clickAjukanExpectFail();
    }

    @When("user submits lembur without jam keluar")
    public void userSubmitsLemburWithoutJamKeluar() {
        lemburPage.clickAjukanLembur();
        lemburPage.enterJamMasuk("26 06 2026, 08:00");
        lemburPage.enterCatatan("Testing lembur valid");
        lemburPage.clickAjukanExpectFail();
    }

    @When("user submits lembur without catatan")
    public void userSubmitsLemburWithoutCatatan() {
        lemburPage.clickAjukanLembur();
        lemburPage.enterJamMasuk("26 06 2026, 08:00");
        lemburPage.enterJamKeluar("26 06 2026, 17:00");
        lemburPage.clickAjukanExpectFail();
    }

    @When("user submits lembur with catatan shorter than 5 characters")
    public void userSubmitsLemburWithCatatanShorterThan5Characters() {
        lemburPage.clickAjukanLembur();
        lemburPage.enterJamMasuk("26 06 2026, 08:00");
        lemburPage.enterJamKeluar("26 06 2026, 17:00");
        lemburPage.enterCatatan("ab");
        lemburPage.clickAjukanExpectFail();
    }

    @When("user fills and resets the lembur form")
    public void userFillsAndResetsTheLemburForm() {
        lemburPage.clickAjukanLembur();
        lemburPage.enterJamMasuk("22 06 2026, 09:00");
        lemburPage.enterJamKeluar("22 06 2026, 22:00");
        lemburPage.enterCatatan("Testing reset lembur form");
        lemburPage.clickReset();
    }

    @When("user navigates back from the lembur page")
    public void userNavigatesBackFromTheLemburPage() {
        lemburPage.clickKembali();
    }

    @Then("lembur page displays the overtime list with total count")
    public void lemburPageDisplaysOvertimeListWithTotalCount() {
        Assertions.assertThat(lemburPage.isPageLoaded())
            .as("Page title 'Halaman Lembur' should be displayed")
            .isTrue();
        Assertions.assertThat(lemburPage.isListOvertimeVisible())
            .as("'List Overtime' section should be visible")
            .isTrue();
        Assertions.assertThat(lemburPage.isTotalLemburVisible())
            .as("Total lembur count should be displayed")
            .isTrue();
        Assertions.assertThat(lemburPage.getTotalLemburCount())
            .as("Total lembur should be a valid number >= 0")
            .isGreaterThanOrEqualTo(0);
    }

    @Then("lembur submission form is displayed")
    public void lemburSubmissionFormIsDisplayed() {
        Assertions.assertThat(lemburPage.isDrawerOpen())
            .as("Lembur submission form (drawer) should be open")
            .isTrue();
    }

    @Then("the lembur is successfully submitted")
    public void theLemburIsSuccessfullySubmitted() {
        Assertions.assertThat(lemburPage.isDrawerClosed())
            .as("Drawer should close after successful lembur submission")
            .isTrue();
        Assertions.assertThat(lemburPage.isOnLemburPage())
            .as("User should remain on the lembur page after submission")
            .isTrue();
    }

    @Then("the latest lembur item appears in the list")
    public void theLatestLemburItemAppearsInTheList() {
        Assertions.assertThat(lemburPage.isOvertimeListVisible())
            .as("Overtime list should be visible after submission")
            .isTrue();
        Assertions.assertThat(lemburPage.isFirstCardHasJamMasukInfo())
            .as("The latest lembur item should display Jam Masuk information")
            .isTrue();
    }

    @Then("total lembur increases by 1")
    public void totalLemburIncreasesBy1() {
        int totalSesudah = lemburPage.getTotalLemburCount();
        Assertions.assertThat(totalSesudah)
            .as("Total lembur should increase by 1 from %d to %d", totalLemburSebelum, totalLemburSebelum + 1)
            .isEqualTo(totalLemburSebelum + 1);
    }

    @Then("the lembur list displays complete item information")
    public void theLemburListDisplaysCompleteItemInformation() {
        Assertions.assertThat(lemburPage.isFirstCardHasUserName())
            .as("Lembur item should display user's name")
            .isTrue();
        Assertions.assertThat(lemburPage.isFirstCardHasJamMasukInfo())
            .as("Lembur item should display Jam Masuk information")
            .isTrue();
        Assertions.assertThat(lemburPage.isFirstCardHasJamKeluarInfo())
            .as("Lembur item should display Jam Keluar information")
            .isTrue();
        Assertions.assertThat(lemburPage.isFirstCardHasStatusInfo())
            .as("Lembur item should display Status information")
            .isTrue();
        Assertions.assertThat(lemburPage.isFirstCardHasNotesInfo())
            .as("Lembur item should display Notes information")
            .isTrue();
        Assertions.assertThat(lemburPage.isFirstCardHasTanggalPengajuanInfo())
            .as("Lembur item should display Tanggal Pengajuan information")
            .isTrue();
    }

    @Then("lembur submission fails with field error {string}")
    public void lemburSubmissionFailsWithFieldError(String expectedError) {
        Assertions.assertThat(lemburPage.isFieldErrorDisplayed(expectedError))
            .as("Field error '%s' should be displayed after invalid submission", expectedError)
            .isTrue();
    }

    @Then("the lembur submission form remains open")
    public void theLemburSubmissionFormRemainsOpen() {
        Assertions.assertThat(lemburPage.isDrawerOpen())
            .as("Lembur submission form should remain open after validation error")
            .isTrue();
    }

    @Then("all lembur form fields are empty")
    public void allLemburFormFieldsAreEmpty() {
        Assertions.assertThat(lemburPage.isJamMasukEmpty())
            .as("Jam masuk field should be empty after reset")
            .isTrue();
        Assertions.assertThat(lemburPage.isJamKeluarEmpty())
            .as("Jam keluar field should be empty after reset")
            .isTrue();
        Assertions.assertThat(lemburPage.isCatatanEmpty())
            .as("Catatan field should be empty after reset")
            .isTrue();
    }

    @Then("user is no longer on the lembur page")
    public void userIsNoLongerOnTheLemburPage() {
        Assertions.assertThat(lemburPage.isOnLemburPage())
            .as("User should have navigated away from the lembur page")
            .isFalse();
    }
}
