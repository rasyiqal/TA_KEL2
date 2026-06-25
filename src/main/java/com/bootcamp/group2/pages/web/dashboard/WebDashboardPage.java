package com.bootcamp.group2.pages.web.dashboard;

import com.bootcamp.group2.pages.BasePage;
import com.bootcamp.group2.pages.web.login.WebLoginPage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import com.bootcamp.group2.utils.WaitUtils;

public class WebDashboardPage extends BasePage {

    private final By userMenu    = By.cssSelector("[class*='MuiAvatar'], [class*='avatar'], #user-menu");
    private final By pageContent = By.cssSelector("main, [class*='main'], [class*='content']");

    @Step("Check if web dashboard page is loaded")
    public boolean isDashboardLoaded() {
        try {
            WaitUtils.waitFor(driver,
                org.openqa.selenium.support.ui.ExpectedConditions.not(
                    org.openqa.selenium.support.ui.ExpectedConditions.urlContains("authentication/login")));
            return true;
        } catch (org.openqa.selenium.TimeoutException e) {
            return false;
        }
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    @Step("Return to login page (logout)")
    public WebLoginPage logout() {
        click(By.cssSelector("[aria-label='logout'], [data-testid='logout']"));
        return new WebLoginPage();
    }
}
