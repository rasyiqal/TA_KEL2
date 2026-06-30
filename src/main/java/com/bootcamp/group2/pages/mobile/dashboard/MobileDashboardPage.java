package com.bootcamp.group2.pages.mobile.dashboard;

import com.bootcamp.group2.pages.BasePage;
import com.bootcamp.group2.pages.mobile.koreksiabsen.MobileKoreksiAbsenPage;
import com.bootcamp.group2.pages.mobile.lembur.MobileLemburPage;
import com.bootcamp.group2.pages.mobile.login.MobileLoginPage;
import com.bootcamp.group2.utils.ConfigManager;
import com.bootcamp.group2.utils.WaitUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class MobileDashboardPage extends BasePage {

    private final By greetingText     = By.xpath("//p[starts-with(normalize-space(), 'Hai,')]");
    private final By koreksiAbsenMenu = By.xpath("//a[contains(@class,'user__menu__item') and normalize-space()='Koreksi Absen']");
    private final By lemburMenu       = By.xpath("//a[contains(@class,'user__menu__item') and normalize-space()='Lembur']");

    // Burger / side menu button — a MuiButton containing <img alt="menu">
    private final By sideMenuBtn = By.xpath("//button[.//img[@alt='menu']]");

    // Logout button inside the opened side menu
    private final By logoutMenuItem = By.xpath(
        "//button[normalize-space()='Logout' or normalize-space()='Keluar']"
    );

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

    @Step("Open side menu (burger menu at top-right)")
    public MobileDashboardPage openSideMenu() {
        WaitUtils.waitForClickable(driver, sideMenuBtn).click();
        // Wait briefly for the side panel / drawer to appear
        try {
            WaitUtils.waitForVisible(driver, logoutMenuItem, 5);
        } catch (Exception e) {
            log.warn("Logout menu item not immediately visible after opening side menu");
        }
        return this;
    }

    /**
     * Logout from the mobile app via homepage burger menu → Keluar.
     *
     * <p>Flow: open side menu → click Keluar/Logout → wait for login page.</p>
     */
    @Step("Logout from mobile app via side menu")
    public MobileLoginPage logout() {
        // Ensure we are on the mobile homepage where the burger menu is accessible
        if (!getCurrentUrl().contains("apps/absent")) {
            log.info("Not on mobile homepage — navigating there before logout");
            navigateTo(ConfigManager.getMobileBaseUrl().replace("absen/login", "absen/apps/absent"));
            WaitUtils.waitForVisible(driver, greetingText);
        }
        openSideMenu();
        click(logoutMenuItem);
        // Wait until the URL changes to the login page
        WaitUtils.waitForUrlContains(driver, "absen/login");
        return new MobileLoginPage();
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

    @Step("Click 'Lembur' from home menu")
    public MobileLemburPage clickLembur() {
        try {
            org.openqa.selenium.WebElement menu = WaitUtils.waitForPresence(driver, lemburMenu);
            try {
                WaitUtils.waitForClickable(driver, lemburMenu);
                menu.click();
            } catch (Exception e) {
                log.warn("Normal click failed or element intercepted, fallback to jsClick: {}", e.getMessage());
                org.openqa.selenium.WebElement freshMenu = WaitUtils.waitForPresence(driver, lemburMenu);
                jsClick(freshMenu);
            }
        } catch (Exception e) {
            log.error("Lembur menu not found in DOM", e);
            throw e;
        }
        return new MobileLemburPage();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}
