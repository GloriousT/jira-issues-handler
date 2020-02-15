package com.jiraissues.testng.strategies;

import lombok.AllArgsConstructor;
import org.testng.ITestResult;

@AllArgsConstructor
public class ExecuteStrategyTestNG implements TestNGStrategy {

    @Override
    public void applyBeforeTestStrategy(ITestResult testResult) {
        // Do not interrupt, just execute the test
    }

    @Override
    public void applyAfterTestStrategy(ITestResult testResult) {
        // Do not interrupt, just execute the test
    }
}
