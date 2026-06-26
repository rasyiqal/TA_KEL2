package com.bootcamp.group2.pages;

import com.bootcamp.group2.utils.DriverFactory;
import com.bootcamp.group2.utils.WaitUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BasePage {

    protected final Logger log = LoggerFactory.getLogger(getClass());
    protected final WebDriver driver;

    protected BasePage() {
        this.driver = DriverFactory.getDriver();
        PageFactory.initElements(driver, this);
    }

    @Step("Open URL: {url}")
    protected void navigateTo(String url) {
        log.info("Opening URL: {}", url);
        driver.get(url);
        com.bootcamp.group2.utils.ScreenshotUtils.attachStepScreenshot(driver, "Opened URL: " + url);
    }

    @Step("Click element: {locator}")
    protected void click(By locator) {
        int attempts = 0;
        while (attempts < 3) {
            try {
                WaitUtils.waitForClickable(driver, locator).click();
                com.bootcamp.group2.utils.ScreenshotUtils.attachStepScreenshot(driver, "Clicked element: " + locator);
                return;
            } catch (StaleElementReferenceException e) {
                log.warn("Stale element reference for locator {}, retrying click (attempt {}/3)", locator,
                        attempts + 1);
            }
            attempts++;
        }
        WaitUtils.waitForClickable(driver, locator).click();
        com.bootcamp.group2.utils.ScreenshotUtils.attachStepScreenshot(driver, "Clicked element: " + locator);
    }

    protected void click(WebElement element) {
        WaitUtils.waitForClickable(driver, element).click();
        com.bootcamp.group2.utils.ScreenshotUtils.attachStepScreenshot(driver, "Clicked web element");
    }

    @Step("Fill field with text: {text}")
    protected void type(By locator, String text) {
        WebElement el = WaitUtils.waitForVisible(driver, locator);
        el.clear();
        el.sendKeys(text);
        com.bootcamp.group2.utils.ScreenshotUtils.attachStepScreenshot(driver, "Typed text in element: " + locator);
    }

    protected void type(WebElement element, String text) {
        WaitUtils.waitForVisible(driver, element).clear();
        element.sendKeys(text);
        com.bootcamp.group2.utils.ScreenshotUtils.attachStepScreenshot(driver, "Typed text in web element");
    }

    protected void selectByVisibleText(By locator, String visibleText) {
        new Select(WaitUtils.waitForVisible(driver, locator)).selectByVisibleText(visibleText);
        com.bootcamp.group2.utils.ScreenshotUtils.attachStepScreenshot(driver,
                "Selected text: " + visibleText + " on element: " + locator);
    }

    protected String getText(By locator) {
        return WaitUtils.waitForVisible(driver, locator).getText();
    }

    protected String getText(WebElement element) {
        return WaitUtils.waitForVisible(driver, element).getText();
    }

    protected String getAttribute(By locator, String attribute) {
        return WaitUtils.waitForPresence(driver, locator).getAttribute(attribute);
    }

    protected boolean isDisplayed(By locator) {
        return WaitUtils.isElementPresent(driver, locator);
    }

    protected boolean isEnabled(By locator) {
        try {
            return WaitUtils.waitForVisible(driver, locator, 3).isEnabled();
        } catch (TimeoutException e) {
            return false;
        }
    }

    protected void hover(WebElement element) {
        new Actions(driver).moveToElement(element).perform();
    }

    protected Object executeJavaScript(String script, Object... args) {
        return ((JavascriptExecutor) driver).executeScript(script, args);
    }

    protected void scrollIntoView(WebElement element) {
        executeJavaScript("arguments[0].scrollIntoView(true);", element);
    }

    protected void jsClick(WebElement element) {
        executeJavaScript("arguments[0].click();", element);
    }

    protected void jsClick(By locator) {
        WebElement element = WaitUtils.waitForPresence(driver, locator);
        executeJavaScript("arguments[0].click();", element);
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    @Step("Wait for URL to contain: {urlFragment}")
    protected void waitForUrl(String urlFragment) {
        WaitUtils.waitForUrlContains(driver, urlFragment);
    }

    protected void waitForInvisible(By locator) {
        WaitUtils.waitForInvisible(driver, locator);
    }
}
