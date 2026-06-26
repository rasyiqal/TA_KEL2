package com.bootcamp.group2.pages.mobile.m_sakit;

import com.bootcamp.group2.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

public class MobileMenuSakitPage extends BasePage {

    private final By menuSakitBtn = By.xpath("//img[@alt='Sakit']");
    private final By ajukanSakitBtn = By.xpath("//button[normalize-space()='Ajukan Sakit']");
    private final By accessAlarmIconBtn = By.xpath("//*[@data-testid='AccessAlarmIcon']");

    @Step("Click menu Sakit")
    public MobileMenuSakitPage clickMenuSakit() {
        jsClick(menuSakitBtn);
        return this;
    }

    @Step("Click button Ajukan Sakit")
    public MobileMenuSakitPage clickAjukanSakit() {
        click(ajukanSakitBtn);
        return this;
    }

    @Step("Click Access Alarm icon")
    public MobileMenuSakitPage clickAccessAlarmIcon() {
        click(accessAlarmIconBtn);
        return this;
    }
}
