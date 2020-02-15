package com.jiraissues;

import org.testng.ITestResult;

public interface AnnotatedTestRunner {

    void applyPreExecutionStrategy(ITestResult result);

    void applyPostExecutionStrategy(ITestResult result);
}
