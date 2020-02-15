package com.jiraissues.testdata;

import com.jiraissues.annotations.Bug;
import org.testng.annotations.Test;

import static org.testng.Assert.fail;


public class AlwaysFailingTest {
    @Test
    @Bug("BUG-509")
    public void shouldAlwaysFail() {
        System.out.println("fail");
        fail("Failing as always");
    }
}
