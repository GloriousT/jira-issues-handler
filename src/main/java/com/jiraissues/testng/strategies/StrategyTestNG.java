package com.jiraissues.testng.strategies;

import com.jiraissues.Strategy;
import lombok.AllArgsConstructor;
import org.testng.ITestResult;

@AllArgsConstructor
public enum StrategyTestNG implements TestNGStrategy {
    SKIP(new SkipStrategyTestNG()),
    EXECUTE(new ExecuteStrategyTestNG()),
    FAIL(new FailStrategyTestNG());

    TestNGStrategy testStrategy;

    public void applyBeforeTestStrategy(ITestResult testResult) {
        testStrategy.applyBeforeTestStrategy(testResult);
    }

    public void applyAfterTestStrategy(ITestResult testResult) {
        testStrategy.applyAfterTestStrategy(testResult);
    }

    public static StrategyTestNG fromStrategy(Strategy strategy) {
        for (StrategyTestNG testStrategy : values()) {
            String strategyName = testStrategy.name().toLowerCase();
            if (strategyName.equals(strategy.name().toLowerCase())) {
                return testStrategy;
            }
        }
        throw new RuntimeException(String.format("Provided strategy \"%s\" does not exist", strategy));
    }
}
