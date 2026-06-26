package com.bootcamp.group2.runners.testng;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

/**
 * TestNGRunner - Cucumber test runner using TestNG.
 *
 * <p>Run with: {@code mvn test -Ptestng}</p>
 *
 * <p>Parallel execution is enabled via {@code @DataProvider(parallel = true)}.
 * Thread count is configured in {@code testng-suite.xml}.</p>
 *
 * <p>Tag filtering:</p>
 * <ul>
 *   <li>Set tags in {@code testng-suite.xml} via parameter, or</li>
 *   <li>{@code mvn test -Ptestng -Dcucumber.filter.tags="@smoke"}</li>
 * </ul>
 */
@CucumberOptions(
    features  = "src/test/resources/features",
    glue      = {
        "com.bootcamp.group2.steps",
        "com.bootcamp.group2.hooks"
    },
    plugin    = {
        "pretty",
        "html:reports/cucumber/cucumber-report-testng.html",
        "json:reports/cucumber/cucumber-report-testng.json",
        "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm",
        "com.bootcamp.group2.plugin.ScreenshotPlugin"
    },
    monochrome = true,
    publish    = false
)
public class TestNGRunner extends AbstractTestNGCucumberTests {

    /**
     * Enables parallel scenario execution.
     * Thread count is controlled by testng-suite.xml.
     *
     * <p>Set {@code parallel = false} to run scenarios sequentially.</p>
     */
    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
