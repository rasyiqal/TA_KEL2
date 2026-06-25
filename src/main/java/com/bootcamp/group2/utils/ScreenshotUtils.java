package com.bootcamp.group2.utils;

import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ScreenshotUtils - Utility class for capturing and attaching screenshots.
 *
 * <p>Screenshots are automatically:</p>
 * <ul>
 *   <li>Saved to {@code reports/screenshots/} directory</li>
 *   <li>Attached to Allure report via {@code Allure.addAttachment()}</li>
 *   <li>Attached to ExtentReports via Hooks</li>
 * </ul>
 */
public class ScreenshotUtils {

    private static final Logger log = LoggerFactory.getLogger(ScreenshotUtils.class);
    private static final String SCREENSHOT_DIR = "reports/screenshots/";
    private static final DateTimeFormatter FORMATTER =
        DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS");

    private ScreenshotUtils() {
        // Utility class
    }

    /**
     * Captures a screenshot and attaches it to both Allure and saves to disk.
     *
     * @param driver    Active WebDriver instance
     * @param testName  Name used for the screenshot file and Allure attachment
     * @return          Path to the saved screenshot, or null if capture failed
     */
    public static String captureScreenshot(WebDriver driver, String testName) {
        if (driver == null) {
            log.warn("Cannot capture screenshot: WebDriver is null");
            return null;
        }

        try {
            byte[] screenshotBytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);

            // Attach to Allure report
            attachToAllure(screenshotBytes, testName);

            // Save to disk
            return saveToFile(screenshotBytes, testName);

        } catch (Exception e) {
            log.error("Failed to capture screenshot for: {}", testName, e);
            return null;
        }
    }

    private static final ThreadLocal<com.aventstack.extentreports.ExtentTest> currentExtentTest = new ThreadLocal<>();

    public static void setExtentTest(com.aventstack.extentreports.ExtentTest test) {
        currentExtentTest.set(test);
    }

    public static void clearExtentTest() {
        currentExtentTest.remove();
    }

    /**
     * Captures a screenshot and attaches it to both Allure and ExtentReports for a specific step.
     */
    public static void attachStepScreenshot(WebDriver driver, String stepName) {
        if (driver == null) return;
        try {
            byte[] bytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            if (bytes.length > 0) {
                // Attach to Allure
                attachToAllure(bytes, stepName);

                // Attach to ExtentReports
                com.aventstack.extentreports.ExtentTest test = currentExtentTest.get();
                if (test != null) {
                    test.info(stepName, com.aventstack.extentreports.MediaEntityBuilder.createScreenCaptureFromBase64String(
                        java.util.Base64.getEncoder().encodeToString(bytes), "Screenshot"
                    ).build());
                }
            }
        } catch (Exception e) {
            log.warn("Failed to attach step screenshot: {}", e.getMessage());
        }
    }

    /**
     * Returns screenshot as byte array (for ExtentReports Base64 embedding).
     *
     * @param driver Active WebDriver instance
     * @return       Screenshot bytes, or empty array if failed
     */
    public static byte[] captureAsBytes(WebDriver driver) {
        try {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            log.error("Failed to capture screenshot as bytes", e);
            return new byte[0];
        }
    }

    /**
     * Returns screenshot as Base64 string (for HTML embedding).
     *
     * @param driver Active WebDriver instance
     * @return       Base64 encoded screenshot string, or null if failed
     */
    public static String captureAsBase64(WebDriver driver) {
        try {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
        } catch (Exception e) {
            log.error("Failed to capture screenshot as Base64", e);
            return null;
        }
    }

    // =========================================================================
    //  Private Helpers
    // =========================================================================

    private static void attachToAllure(byte[] screenshotBytes, String name) {
        try {
            Allure.addAttachment(
                "Screenshot: " + name,
                "image/png",
                new ByteArrayInputStream(screenshotBytes),
                ".png"
            );
        } catch (Exception e) {
            log.warn("Failed to attach screenshot to Allure: {}", e.getMessage());
        }
    }

    private static String saveToFile(byte[] screenshotBytes, String testName) throws IOException {
        Path screenshotDir = Paths.get(SCREENSHOT_DIR);
        Files.createDirectories(screenshotDir);

        String fileName  = sanitizeFileName(testName) + "_" + LocalDateTime.now().format(FORMATTER) + ".png";
        Path   filePath  = screenshotDir.resolve(fileName);

        Files.write(filePath, screenshotBytes);
        log.info("Screenshot saved: {}", filePath.toAbsolutePath());
        return filePath.toString();
    }

    private static String sanitizeFileName(String name) {
        return name.replaceAll("[^a-zA-Z0-9_\\-]", "_").replaceAll("_{2,}", "_");
    }
}
