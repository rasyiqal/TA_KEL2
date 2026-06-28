package com.bootcamp.group2.steps.ui.mobile.cuti;

import com.bootcamp.group2.pages.mobile.cuti.MobileMenuCutiPage;
import io.cucumber.java.en.*;

public class MobileMenuCutiSteps {

    private MobileMenuCutiPage cutiPage;

    @When("user clicks menu Cuti")
    public void userClicksMenuCuti() {
        cutiPage = new MobileMenuCutiPage();
        cutiPage.clickMenuCuti();
    }

    @When("user clicks button Ajukan Cuti")
    public void userClicksButtonAjukanCuti() {
        cutiPage.clickAjukanCuti();
    }

    @When("user selects tipe cuti")
    public void userSelectsTipeCuti() {
        cutiPage.pilihTipeCuti();
    }

    @When("user clicks Input Area Cuti")
    public void userClicksInputAreaCuti() {
        cutiPage.clickInputArea();
    }

    @When("user selects today as cuti start date")
    public void userSelectsTodayAsCutiStartDate() {
        cutiPage.pilihTanggalMulaiHariIni();
    }

    @When("user selects cuti end date {int} days from today")
    public void userSelectsCutiEndDateDaysFromToday(int plusDays) {
        cutiPage.pilihTanggalSelesaiPlusDays(plusDays);
    }

    @When("user inputs random catatan")
    public void userInputsRandomCatatan() {
        cutiPage.inputCatatanRandom();
    }

    @When("user clicks button Ajukan form Cuti")
    public void userClicksButtonAjukanFormCuti() {
        cutiPage.clickAjukanForm();
    }

    @When("user clicks button Reset Cuti")
    public void userClicksButtonResetCuti() {
        cutiPage.clickReset();
    }

    @When("user clicks button Simpan Cuti")
    public void userClicksButtonSimpanCuti() {
        cutiPage.clickSimpan();
    }
}