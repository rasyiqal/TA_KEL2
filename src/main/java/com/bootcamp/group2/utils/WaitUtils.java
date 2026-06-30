package com.bootcamp.group2.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * WaitUtils - Centralized explicit wait utilities.
 *
 * <p>All methods use explicit waits. Never use {@code Thread.sleep()} in tests.
 * Default timeout is read from config (defaults to 10 seconds).</p>
 */
public class WaitUtils {

    private static final Logger log = LoggerFactory.getLogger(WaitUtils.class);
    private static final int DEFAULT_TIMEOUT = ConfigManager.getInt("timeout.explicit", 10);
    private static final int SHORT_TIMEOUT   = 3;
    private static final int LONG_TIMEOUT    = 30;

    private WaitUtils() {
        // Utility class
    }

    public static WebElement waitForVisible(WebDriver driver, By locator) {
        return getWait(driver, DEFAULT_TIMEOUT)
            .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement waitForVisible(WebDriver driver, By locator, int timeoutSeconds) {
        return getWait(driver, timeoutSeconds)
            .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement waitForVisible(WebDriver driver, WebElement element) {
        return getWait(driver, DEFAULT_TIMEOUT)
            .until(ExpectedConditions.visibilityOf(element));
    }

    public static WebElement waitForClickable(WebDriver driver, By locator) {
        return getWait(driver, DEFAULT_TIMEOUT)
            .until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static WebElement waitForClickable(WebDriver driver, By locator, int timeoutSeconds) {
        return getWait(driver, timeoutSeconds)
            .until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static WebElement waitForClickable(WebDriver driver, WebElement element) {
        return getWait(driver, DEFAULT_TIMEOUT)
            .until(ExpectedConditions.elementToBeClickable(element));
    }

    public static WebElement waitForPresence(WebDriver driver, By locator) {
        return getWait(driver, DEFAULT_TIMEOUT)
            .until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public static void waitForUrlContains(WebDriver driver, String urlFragment) {
        getWait(driver, DEFAULT_TIMEOUT)
            .until(ExpectedConditions.urlContains(urlFragment));
    }

    public static void waitForTitleContains(WebDriver driver, String title) {
        getWait(driver, DEFAULT_TIMEOUT)
            .until(ExpectedConditions.titleContains(title));
    }

    public static void waitForInvisible(WebDriver driver, By locator) {
        getWait(driver, DEFAULT_TIMEOUT)
            .until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public static void waitForInvisible(WebDriver driver, By locator, int timeoutSeconds) {
        getWait(driver, timeoutSeconds)
            .until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public static void waitForStaleness(WebDriver driver, WebElement element) {
        getWait(driver, DEFAULT_TIMEOUT)
            .until(ExpectedConditions.stalenessOf(element));
    }

    public static void waitForTextPresent(WebDriver driver, By locator, String text) {
        getWait(driver, DEFAULT_TIMEOUT)
            .until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }

    public static <T> T waitFor(WebDriver driver, ExpectedCondition<T> condition) {
        return getWait(driver, DEFAULT_TIMEOUT).until(condition);
    }

    public static <T> T waitFor(WebDriver driver, ExpectedCondition<T> condition, int timeoutSeconds) {
        return getWait(driver, timeoutSeconds).until(condition);
    }

    /**
     * Checks if an element is present without throwing an exception.
     * Useful for conditional logic (e.g., "close cookie banner if present").
     */
    public static boolean isElementPresent(WebDriver driver, By locator) {
        try {
            waitForVisible(driver, locator, SHORT_TIMEOUT);
            return true;
        } catch (TimeoutException | NoSuchElementException e) {
            log.debug("Element not present: {}", locator);
            return false;
        }
    }

    private static WebDriverWait getWait(WebDriver driver, int timeoutSeconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
    }
}
