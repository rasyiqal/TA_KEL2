package com.bootcamp.group2.pages.mobile.dashboard;

import com.bootcamp.group2.pages.BasePage;
import com.bootcamp.group2.pages.mobile.login.MobileLoginPage;
import com.bootcamp.group2.utils.WaitUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class MobileDashboardPage extends BasePage {

    @Step("Check if mobile dashboard page is loaded")
    public boolean isDashboardLoaded() {
        try {
            WaitUtils.waitFor(driver,
                ExpectedConditions.not(ExpectedConditions.urlContains("absen/login")));
            return true;
        } catch (TimeoutException e) {
            log.warn("Dashboard not loaded — URL still contains 'absen/login': {}", getCurrentUrl());
            return false;
        }
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}
