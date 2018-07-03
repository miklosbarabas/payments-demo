package org.miklosbarabas.demo.options;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"pretty", "html:target/cucumber-html-report", "json:target/cucumber-json-report.json"},
        glue = {"org.miklosbarabas.demo"},
        features = {"src/test/java/org/miklosbarabas/demo/features"},
        monochrome = true
)
public class CucumberTests {}
