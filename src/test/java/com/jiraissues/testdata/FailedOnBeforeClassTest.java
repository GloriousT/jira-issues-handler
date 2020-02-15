package com.jiraissues.testdata;

import com.jiraissues.annotations.Bug;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.fail;

public class FailedOnBeforeClassTest {

    @Bug("BUG-509")
    @BeforeClass
    public void shouldAlwaysFailAsConfig() {
        fail("Failing as always");
    }

    @Test
    public void shouldAlwaysSkip() {

    }
}
