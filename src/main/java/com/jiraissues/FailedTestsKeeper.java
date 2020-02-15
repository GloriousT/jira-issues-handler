package com.jiraissues;

import com.jiraissues.annotations.AnnotatedMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.String.format;

public class FailedTestsKeeper {

    private static final Logger logger = LoggerFactory.getLogger(FailedTestsKeeper.class);
    private static final String NO_KNOWN_BUG_PREFIX = "No known bug mentioned in code";
    private static final String POSSIBLE_KNOWN_BUG_PREFIX = "Possible issue https://track.issues.com/jira/browse/";

    private final Map<String, String> failedTests = new ConcurrentHashMap<>();
    private final Set<String> mentionedJiraTickets = new HashSet<>();
    private static FailedTestsKeeper instance = null;

    private FailedTestsKeeper() {

    }

    public static FailedTestsKeeper getInstance() {
        if (instance == null) {
            instance = new FailedTestsKeeper();
        }
        return instance;
    }

    public void keepTestResult(Method test) {
        AnnotatedMethod annotatedMethod = new AnnotatedMethod(test);
        String possibleRootCause = getPossibleRootCause(annotatedMethod);

        failedTests.put(fullTestName(test), possibleRootCause);
    }

    void wipeRecords() {
        failedTests.clear();
        mentionedJiraTickets.clear();
    }

    public Collection<String> getMentionedJiraIds() {
        return new HashSet<>(mentionedJiraTickets);
    }

    public void logFailedTests() {
        for (Map.Entry<String, String> failedTest : failedTests.entrySet()) {
            logger.warn("{} - {}", failedTest.getKey(), failedTest.getValue());
        }
    }

    private String getPossibleRootCause(AnnotatedMethod testMethod) {
        String possibleRootCause;
        if (testMethod.isAnnotated()) {
            possibleRootCause = definePossibleRootCauses(testMethod);
        } else {
            possibleRootCause = NO_KNOWN_BUG_PREFIX;
        }
        return possibleRootCause;
    }

    private String definePossibleRootCauses(AnnotatedMethod annotatedMethod) {
        String possibleRootCause;
        StringBuilder builder = new StringBuilder();
        for (String bugReport : annotatedMethod.getAnnotationValue()) {
            builder.append(POSSIBLE_KNOWN_BUG_PREFIX)
                    .append(bugReport)
                    .append(System.lineSeparator());
            mentionedJiraTickets.add(bugReport);
        }
        possibleRootCause = builder.toString();
        return possibleRootCause;
    }

    private static String fullTestName(Method testMethod) {
        return format("%s.%s", testMethod.getDeclaringClass().getSimpleName(), testMethod.getName());
    }
}
