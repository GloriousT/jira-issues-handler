package com.jiraissues.testdata;

import org.testng.annotations.Test;

import static org.testng.Assert.fail;


public class AlwaysFailingNotAnnotatedTest {
    @Test
    public void shouldAlwaysFail() {
        fail("Failing as always");
    }
}
