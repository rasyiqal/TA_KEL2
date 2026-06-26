package com.bootcamp.group2.runners.junit5;

import org.junit.platform.suite.api.*;

/**
 * JUnit5Runner - Cucumber test runner using JUnit Platform Suite.
 *
 * <p>Run with: {@code mvn test} or {@code mvn test -Pjunit5}</p>
 *
 * <p>Tag filtering:</p>
 * <ul>
 *   <li>All tests     : mvn test</li>
 *   <li>Smoke only    : mvn test -Psmoke  (sets cucumber.filter.tags=@smoke)</li>
 *   <li>Regression    : mvn test -Pregression</li>
 *   <li>Custom tag    : mvn test -Dcucumber.filter.tags="@login and not @wip"</li>
 * </ul>
 *
 * <p>Parallel execution is configured in {@code junit-platform.properties}.</p>
 */
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(
    key   = "cucumber.glue",
    value = "com.bootcamp.group2.steps,com.bootcamp.group2.hooks"
)
@ConfigurationParameter(
    key   = "cucumber.plugin",
    value = "pretty," +
            "html:reports/cucumber/cucumber-report.html," +
            "json:reports/cucumber/cucumber-report.json," +
            "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm," +
            "com.bootcamp.group2.plugin.ScreenshotPlugin"
)
@ConfigurationParameter(
    key   = "cucumber.publish.quiet",
    value = "true"
)
public class JUnit5Runner {
    // This class is intentionally empty.
    // It serves as the JUnit Platform Suite entry point for Cucumber.
}
