package com.jiraissues.testng.strategies;

import org.testng.ITestResult;

public interface TestNGStrategy {

    void applyBeforeTestStrategy(ITestResult testResult);
    void applyAfterTestStrategy(ITestResult testResult);
}
