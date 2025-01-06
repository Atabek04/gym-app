package com.epam.gym.main.cucumber.runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/cucumber/training_controller.feature",
        glue = {"com.epam.gym.main.cucumber.stepdefinition",
                "com.epam.gym.main.cucumber"},
        plugin = {
                "pretty",
                "html:target/cucumber-reports.html",
                "json:target/cucumber-reports.json"
        },
        monochrome = true
)
public class CucumberTests {
}
