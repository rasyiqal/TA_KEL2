package com.bootcamp.group2.steps.ui.mobile.m_sakit;

import com.bootcamp.group2.pages.mobile.m_sakit.MobileMenuSakitPage;
import io.cucumber.java.en.*;

public class MobileMenuSakitSteps {

    private MobileMenuSakitPage sakitPage;

    @When("user clicks menu Sakit")
    public void userClicksMenuSakit() {
        sakitPage = new MobileMenuSakitPage();
        sakitPage.clickMenuSakit();
    }

    @When("user clicks button Ajukan Sakit")
    public void userClicksButtonAjukanSakit() {
        sakitPage.clickAjukanSakit();
    }

    @When("user clicks Input Area")
    public void userClicksInputArea() {
        sakitPage.clickInputArea();
    }

    @When("user selects today as start date")
    public void userSelectsTodayAsStartDate() {
        sakitPage.pilihTanggalMulaiHariIni();
    }

    @When("user selects end date {int} days from today")
    public void userSelectsEndDateDaysFromToday(int plusDays) {
        sakitPage.pilihTanggalSelesaiPlusDays(plusDays);
    }

    @When("user clicks button Simpan")
    public void userClicksButtonSimpan() {
        sakitPage.clickSimpan();
    }
}