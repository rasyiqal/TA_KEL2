package com.bootcamp.group2.hooks;

import com.bootcamp.group2.utils.DriverFactory;
import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class E2EHooks {

    private static final Logger log = LoggerFactory.getLogger(E2EHooks.class);


    @After(value = "@e2e", order = 20000)
    public void clearSessionAfterE2EScenario(Scenario scenario) {
        WebDriver driver;
        try {
            driver = DriverFactory.getDriver();
        } catch (IllegalStateException e) {

            log.warn("E2E cleanup skipped (no active driver) after: {}", scenario.getName());
            return;
        }

        log.info("E2E cleanup — wiping browser session after scenario: [{}]", scenario.getName());


        try {
            driver.manage().deleteAllCookies();
            log.debug("E2E cleanup: cookies deleted");
        } catch (Exception e) {
            log.warn("E2E cleanup: failed to delete cookies — {}", e.getMessage());
        }


        try {
            ((JavascriptExecutor) driver).executeScript(
                "try { window.localStorage.clear();   } catch(e) {}" +
                "try { window.sessionStorage.clear(); } catch(e) {}"
            );
            log.debug("E2E cleanup: localStorage + sessionStorage cleared");
        } catch (Exception e) {
            log.warn("E2E cleanup: failed to clear browser storage — {}", e.getMessage());
        }


        try {
            driver.navigate().to("about:blank");
            log.debug("E2E cleanup: navigated to about:blank");
        } catch (Exception e) {
            log.warn("E2E cleanup: failed to navigate away — {}", e.getMessage());
        }

        log.info("E2E cleanup complete — next scenario will start from fresh login");
    }
}
