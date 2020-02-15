package com.jiraissues;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static com.jiraissues.ClosedIssuesCheckLevel.*;
import static org.assertj.core.api.Assertions.assertThat;

public class ClosedIssuesCheckLevelTest {

    @DataProvider
    Object[][] levels() {
        return new Object[][]{
                {"warn", WARN},
                {"Warn", WARN},
                {"fail", FAIL},
                {"Fail", FAIL},
        };
    }

    @Test(dataProvider = "levels")
    public void shouldReturnLevelValue(String level, ClosedIssuesCheckLevel expectedLevel) {
        //when
        ClosedIssuesCheckLevel strategy = fromValue(level);

        //then
        assertThat(strategy).isEqualTo(expectedLevel);
    }

    @Test
    public void shouldReturnNull() {
        //when
        ClosedIssuesCheckLevel strategy = fromValue("nonsense");

        //then
        assertThat(strategy).isNull();
    }
}