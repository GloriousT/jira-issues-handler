package com.jiraissues.listeners;

import com.jiraissues.AnnotatedTestRunner;
import com.jiraissues.FailedTestsKeeper;
import com.jiraissues.testng.TestNGAnnotatedTestRunner;
import org.testng.IConfigurationListener;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;

import java.lang.reflect.Method;

import static org.testng.ITestResult.SUCCESS;

public class JiraIssuesTestNGListener implements IInvokedMethodListener, IConfigurationListener {

    private final FailedTestsKeeper failedTestsKeeper = FailedTestsKeeper.getInstance();
    private AnnotatedTestRunner testStrategyApplier = new TestNGAnnotatedTestRunner();

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        testStrategyApplier.applyPreExecutionStrategy(testResult);
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        testStrategyApplier.applyPostExecutionStrategy(testResult);
        if (testResult.getStatus() != SUCCESS) {
            failedTestsKeeper.keepTestResult(getTestMethod(testResult));
        }
    }

    @Override
    public void onConfigurationSuccess(ITestResult testResult) {
        testStrategyApplier.applyPostExecutionStrategy(testResult);
    }

    @Override
    public void onConfigurationFailure(ITestResult testResult) {
        testStrategyApplier.applyPostExecutionStrategy(testResult);
        failedTestsKeeper.keepTestResult(getTestMethod(testResult));
    }

    @Override
    public void onConfigurationSkip(ITestResult testResult) {
        testStrategyApplier.applyPostExecutionStrategy(testResult);
        failedTestsKeeper.keepTestResult(getTestMethod(testResult));
    }

    private Method getTestMethod(ITestResult testResult) {
        return testResult.getMethod().getConstructorOrMethod().getMethod();
    }
}
