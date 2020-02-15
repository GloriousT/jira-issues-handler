package com.jiraissues.testng.strategies;

import lombok.AllArgsConstructor;
import org.testng.ITestResult;
import org.testng.TestException;

@AllArgsConstructor
public class FailStrategyTestNG implements TestNGStrategy {

    @Override
    public void applyBeforeTestStrategy(ITestResult testResult) {
        testResult.setStatus(ITestResult.FAILURE);
        throw new TestException("FAILED");
    }

    @Override
    public void applyAfterTestStrategy(ITestResult testResult) {
        testResult.setStatus(ITestResult.FAILURE);
    }
}
