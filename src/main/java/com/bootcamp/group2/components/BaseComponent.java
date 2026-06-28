package com.bootcamp.group2.components;

import com.bootcamp.group2.utils.DriverFactory;
import com.bootcamp.group2.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * BaseComponent - Base class for reusable UI components.
 *
 * <p>Components are reusable UI fragments shared across multiple pages
 * (e.g., Header, Footer, Navigation Menu, Modals, Toast Notifications).</p>
 *
 * <p>Use composition in Page Objects:</p>
 * <pre>
 *   public class DashboardPage extends BasePage {
 *       private final HeaderComponent header = new HeaderComponent();
 *       private final NavigationComponent nav = new NavigationComponent();
 *   }
 * </pre>
 *
 * <p>This keeps Page Objects thin and promotes reuse.</p>
 */
public abstract class BaseComponent {

    protected final Logger log = LoggerFactory.getLogger(getClass());
    protected final WebDriver driver;

    protected BaseComponent() {
        this.driver = DriverFactory.getDriver();
        PageFactory.initElements(driver, this);
    }

    protected WebElement waitForVisible(By locator) {
        return WaitUtils.waitForVisible(driver, locator);
    }

    protected WebElement waitForClickable(By locator) {
        return WaitUtils.waitForClickable(driver, locator);
    }

    protected boolean isDisplayed(By locator) {
        return WaitUtils.isElementPresent(driver, locator);
    }

    protected String getText(By locator) {
        return waitForVisible(locator).getText();
    }

    protected void click(By locator) {
        waitForClickable(locator).click();
    }
}
