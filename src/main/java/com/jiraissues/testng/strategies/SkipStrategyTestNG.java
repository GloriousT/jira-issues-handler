package com.jiraissues.testng.strategies;

import lombok.AllArgsConstructor;
import org.testng.ITestResult;
import org.testng.SkipException;

@AllArgsConstructor
public class SkipStrategyTestNG implements TestNGStrategy {

    @Override
    public void applyBeforeTestStrategy(ITestResult testResult) {
        testResult.setStatus(ITestResult.SKIP);
        throw new SkipException("Skipped");
    }

    @Override
    public void applyAfterTestStrategy(ITestResult testResult) {
        testResult.setStatus(ITestResult.SKIP);
    }
}