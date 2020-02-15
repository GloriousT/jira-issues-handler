package com.jiraissues.testng;

import com.jiraissues.AnnotatedTestRunner;
import com.jiraissues.Configuration;
import com.jiraissues.Strategy;
import com.jiraissues.annotations.AnnotatedMethod;
import com.jiraissues.testng.strategies.StrategyTestNG;
import org.testng.ITestResult;

import java.lang.reflect.Method;

public class TestNGAnnotatedTestRunner implements AnnotatedTestRunner {

    private Configuration configuration = Configuration.getInstanace();

    private static AnnotatedMethod getTestMethod(ITestResult result) {
        return new AnnotatedMethod(result.getMethod().getConstructorOrMethod().getMethod());
    }

    private static boolean isTestAnnotated(ITestResult testResult) {
        Method testMethod = getTestMethod(testResult).getMethod();
        AnnotatedMethod annotatedMethod = new AnnotatedMethod(testMethod);
        return annotatedMethod.isAnnotated();
    }

    @Override
    public void applyPreExecutionStrategy(ITestResult result) {
        if (isTestAnnotated(result)) {
            Strategy knownBugsStrategy = configuration.getKnownBugsStrategy();
            StrategyTestNG.fromStrategy(knownBugsStrategy).applyBeforeTestStrategy(result);
        }
    }

    @Override
    public void applyPostExecutionStrategy(ITestResult result) {
        if (isTestAnnotated(result)) {
            Strategy knownBugsStrategy = configuration.getKnownBugsStrategy();
            StrategyTestNG.fromStrategy(knownBugsStrategy).applyAfterTestStrategy(result);
        }
    }
}
