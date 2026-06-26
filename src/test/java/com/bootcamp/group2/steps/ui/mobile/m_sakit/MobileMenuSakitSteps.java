package com.bootcamp.group2.steps.ui.mobile.m_sakit;

import com.bootcamp.group2.pages.mobile.m_sakit.MobileMenuSakitPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;

public class MobileMenuSakitSteps {

    private MobileMenuSakitPage menuSakitPage;

    @When("user clicks menu Sakit")
    public void userClicksMenuSakit() {
        menuSakitPage = new MobileMenuSakitPage();
        menuSakitPage.clickMenuSakit();
    }

    @And("user clicks button Ajukan Sakit")
    public void userClicksButtonAjukanSakit() {
        menuSakitPage.clickAjukanSakit();
    }

    @And("user clicks Access Alarm icon")
    public void userClicksAccessAlarmIcon() {
        menuSakitPage.clickAccessAlarmIcon();
    }
}
