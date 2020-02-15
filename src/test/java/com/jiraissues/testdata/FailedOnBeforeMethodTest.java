package com.jiraissues.testdata;

import com.jiraissues.annotations.Bug;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.fail;

public class FailedOnBeforeMethodTest {

    @Bug("BUG-509")
    @BeforeMethod
    public void shouldAlwaysFailAsConfig() {
        fail("Failing as always");
    }

    @Test
    public void shouldAlwaysSkip() {

    }

}
