package com.bootcamp.group2.hooks;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.bootcamp.group2.utils.ConfigManager;
import com.bootcamp.group2.utils.DriverFactory;
import com.bootcamp.group2.utils.ScreenshotUtils;
import io.cucumber.java.*;
import io.qameta.allure.Allure;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;

public class Hooks {

    private static final Logger log = LoggerFactory.getLogger(Hooks.class);

    private static ExtentReports extentReports;
    private static final ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    @BeforeAll
    public static void beforeAll() {
        log.info("Suite started | env={} | platform={}", ConfigManager.getEnvironment(), ConfigManager.getPlatform());
        initExtentReports();
    }

    @AfterAll
    public static void afterAll() {
        if (extentReports != null) extentReports.flush();
        log.info("Suite finished");
    }

    @Before
    public void beforeScenario(Scenario scenario) {
        log.info("Starting: {}", scenario.getName());
        DriverFactory.initDriver();

        ExtentTest test = extentReports.createTest(scenario.getName());
        test.info("Tags: " + scenario.getSourceTagNames());
        test.info("Platform: " + ConfigManager.getPlatform());
        extentTest.set(test);
        ScreenshotUtils.setExtentTest(test);
    }

    @After
    public void afterScenario(Scenario scenario) {
        WebDriver driver = null;
        try {
            driver = DriverFactory.getDriver();
        } catch (IllegalStateException ignored) {}

        if (scenario.isFailed() && driver != null) {
            log.warn("Scenario FAILED: {} — taking screenshot", scenario.getName());
            captureAndAttachScreenshot(driver, scenario);
        }

        updateExtentReport(scenario);
        DriverFactory.quitDriver();
        ScreenshotUtils.clearExtentTest();
        log.info("Finished: {} — Status: {}", scenario.getName(), scenario.getStatus());
    }

    private static void initExtentReports() {
        ExtentSparkReporter reporter = new ExtentSparkReporter("reports/extent/AutomationReport.html");
        reporter.config().setDocumentTitle("HADIR — QA Automation Report");
        reporter.config().setReportName("Bootcamp Group 2 — Test Results");

        extentReports = new ExtentReports();
        extentReports.attachReporter(reporter);
        extentReports.setSystemInfo("Environment", ConfigManager.getEnvironment());
        extentReports.setSystemInfo("Platform", ConfigManager.getPlatform().toString());
        extentReports.setSystemInfo("Java", System.getProperty("java.version"));
        extentReports.setSystemInfo("OS", System.getProperty("os.name"));
    }

    private void captureAndAttachScreenshot(WebDriver driver, Scenario scenario) {
        try {
            byte[] bytes = ScreenshotUtils.captureAsBytes(driver);
            if (bytes.length > 0) {
                scenario.attach(bytes, "image/png", "Failure Screenshot");
                Allure.addAttachment("Screenshot: " + scenario.getName(), "image/png",
                    new ByteArrayInputStream(bytes), ".png");

                ExtentTest test = extentTest.get();
                if (test != null) {
                    test.fail("Scenario failed — screenshot attached");
                    test.addScreenCaptureFromBase64String(
                        java.util.Base64.getEncoder().encodeToString(bytes), "Failure Screenshot"
                    );
                }
            }
        } catch (Exception e) {
            log.error("Failed to take screenshot: {}", e.getMessage());
        }
    }

    private void updateExtentReport(Scenario scenario) {
        ExtentTest test = extentTest.get();
        if (test == null) return;

        switch (scenario.getStatus()) {
            case PASSED  -> test.log(Status.PASS, "PASSED");
            case FAILED  -> test.log(Status.FAIL, "FAILED");
            case SKIPPED -> test.log(Status.SKIP, "SKIPPED");
            default      -> test.log(Status.INFO, scenario.getStatus().toString());
        }

        extentTest.remove();
    }
}
