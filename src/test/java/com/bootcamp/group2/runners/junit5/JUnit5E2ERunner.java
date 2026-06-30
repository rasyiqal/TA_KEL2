package com.bootcamp.group2.runners.junit5;

import org.junit.platform.suite.api.*;


@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features/ui/e2e")


@ConfigurationParameter(
    key   = "cucumber.filter.tags",
    value = "@e2e"
)


@ConfigurationParameter(
    key   = "cucumber.glue",
    value = "com.bootcamp.group2.steps,com.bootcamp.group2.hooks"
)
@ConfigurationParameter(
    key   = "cucumber.plugin",
    value = "pretty," +
            "html:reports/cucumber/cucumber-report-e2e.html," +
            "json:reports/cucumber/cucumber-report-e2e.json," +
            "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
)
@ConfigurationParameter(
    key   = "cucumber.publish.quiet",
    value = "true"
)


@ConfigurationParameter(
    key   = "cucumber.execution.parallel.enabled",
    value = "false"
)
@ConfigurationParameter(
    key   = "junit.jupiter.execution.parallel.enabled",
    value = "false"
)
@ConfigurationParameter(
    key   = "junit.jupiter.execution.parallel.mode.default",
    value = "same_thread"
)
@ConfigurationParameter(
    key   = "junit.jupiter.execution.parallel.mode.classes.default",
    value = "same_thread"
)
public class JUnit5E2ERunner {

}
