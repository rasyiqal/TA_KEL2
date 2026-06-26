package com.bootcamp.group2.plugin;

import com.bootcamp.group2.utils.DriverFactory;
import com.bootcamp.group2.utils.ScreenshotManager;
import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.*;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

public class ScreenshotPlugin implements ConcurrentEventListener {

    private static final Logger log = LoggerFactory.getLogger(ScreenshotPlugin.class);

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestCaseStarted.class, this::onTestCaseStarted);
        publisher.registerHandlerFor(TestStepFinished.class, this::onTestStepFinished);
    }

    private void onTestCaseStarted(TestCaseStarted event) {
        ScreenshotManager.resetStepCounter();
    }

    private void onTestStepFinished(TestStepFinished event) {
        if (!(event.getTestStep() instanceof PickleStepTestStep)) return;

        WebDriver driver = getDriverSafely();
        if (driver == null) return;

        PickleStepTestStep step = (PickleStepTestStep) event.getTestStep();
        String stepText     = step.getStep().getText();
        String featureName  = extractFeatureName(event.getTestCase().getUri());
        String scenarioName = event.getTestCase().getName();

        ScreenshotManager.saveStepScreenshot(driver, featureName, scenarioName, stepText);
    }

    private WebDriver getDriverSafely() {
        try {
            return DriverFactory.getDriver();
        } catch (IllegalStateException e) {
            return null;
        }
    }

    private String extractFeatureName(URI featureUri) {
        String path = featureUri.getSchemeSpecificPart();

        int idx = path.indexOf("features/");
        if (idx >= 0) {
            path = path.substring(idx + "features/".length());
        }

        int lastSlash = path.lastIndexOf('/');
        if (lastSlash >= 0) {
            path = path.substring(0, lastSlash);
        }

        if (path.startsWith("ui/")) {
            path = path.substring(3);
        }

        return path.replace("/", "_");
    }
}
