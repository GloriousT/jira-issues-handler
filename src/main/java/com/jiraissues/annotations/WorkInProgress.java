package com.jiraissues.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Documents which jira issues a given test will verify.
 */
@Retention(RUNTIME)
@Target({TYPE, METHOD})
public @interface WorkInProgress {

    /**
     * Issues identifiers, e.g., "ALERTING-237" in JIRA.
     */
    String[] value();
}
