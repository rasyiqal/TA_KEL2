package com.bootcamp.group2.runners.testng;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;


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


    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
