package com.jiraissues.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Documents which bugs a given test reproduces or verifies.
 */
@Retention(RUNTIME)
@Target({TYPE, METHOD})
public @interface Bug {

    /**
     * Issues identifiers, e.g., "AP-237" in JIRA.
     */
    String[] value();
}
