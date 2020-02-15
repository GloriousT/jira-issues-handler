package com.jiraissues.testdata;

import com.jiraissues.annotations.Bug;
import org.testng.annotations.Test;

public class AlwaysPassingTest {
    @Test
    @Bug("BUG-509")
    public void shouldAlwaysPass() {
        System.out.println("pass");
    }
}
