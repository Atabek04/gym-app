package com.epam.gym.authservice.cucumber.runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/cucumber/feature",
        glue = {"com.epam.gym.authservice.cucumber.stepdefinition",
                "com.epam.gym.authservice.cucumber"},
        plugin = {
                "pretty",
                "html:target/cucumber-reports.html",
                "json:target/cucumber-reports.json"
        },
        monochrome = true
)
public class CucumberTests {
}
