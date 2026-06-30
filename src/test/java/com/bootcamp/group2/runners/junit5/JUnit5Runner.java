package com.bootcamp.group2.runners.junit5;

import org.junit.platform.suite.api.*;


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

}
