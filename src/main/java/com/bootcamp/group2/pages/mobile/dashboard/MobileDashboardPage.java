package com.bootcamp.group2.pages.mobile.dashboard;

import com.bootcamp.group2.pages.BasePage;
import com.bootcamp.group2.pages.mobile.koreksiabsen.MobileKoreksiAbsenPage;
import com.bootcamp.group2.utils.WaitUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class MobileDashboardPage extends BasePage {

    private final By greetingText     = By.xpath("//p[starts-with(normalize-space(), 'Hai,')]");
    private final By koreksiAbsenMenu = By.xpath("//a[contains(@class,'user__menu__item') and normalize-space()='Koreksi Absen']");

    @Step("Check if mobile home page is loaded")
    public boolean isHomePageLoaded() {
        return isDisplayed(greetingText) && getCurrentUrl().contains("apps/absent");
    }

    @Step("Check if mobile dashboard is loaded")
    public boolean isDashboardLoaded() {
        try {
            WaitUtils.waitFor(driver,
                ExpectedConditions.not(ExpectedConditions.urlContains("absen/login")));
            WaitUtils.waitForVisible(driver, greetingText);
            return true;
        } catch (TimeoutException e) {
            log.warn("Dashboard not loaded — URL still contains 'absen/login': {}", getCurrentUrl());
            return false;
        }
    }

    @Step("Click 'Koreksi Absen' from home menu")
    public MobileKoreksiAbsenPage clickKoreksiAbsen() {
        try {
            org.openqa.selenium.WebElement menu = WaitUtils.waitForPresence(driver, koreksiAbsenMenu);
            try {
                WaitUtils.waitForClickable(driver, koreksiAbsenMenu);
                menu.click();
            } catch (Exception e) {
                log.warn("Normal click failed or element intercepted, fallback to jsClick: {}", e.getMessage());
                org.openqa.selenium.WebElement freshMenu = WaitUtils.waitForPresence(driver, koreksiAbsenMenu);
                jsClick(freshMenu);
            }
        } catch (Exception e) {
            log.error("Koreksi Absen menu not found in DOM", e);
            throw e;
        }
        return new MobileKoreksiAbsenPage();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}
