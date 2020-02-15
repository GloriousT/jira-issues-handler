package com.jiraissues;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.jiraissues.ClosedIssuesCheckLevel.FAIL;
import static com.jiraissues.ClosedIssuesCheckLevel.WARN;
import static com.jiraissues.Strategy.*;
import static org.assertj.core.api.Assertions.assertThat;

public class ConfigurationTest {

    private static final String KNOWN_BUGS = "known.bugs";
    private static final String ISSUE_STATUS_CHECK_ENABLED = "closed.issues.check.enabled";
    private static final String ISSUE_STATUS_CHECK_LEVEL = "closed.issues.check.level";
    private Configuration configuration;

    @BeforeMethod
    private void beforeMethod() {
        configuration = new Configuration();
        System.clearProperty(KNOWN_BUGS);
        System.clearProperty(ISSUE_STATUS_CHECK_ENABLED);
        System.clearProperty(ISSUE_STATUS_CHECK_LEVEL);
    }

    @Test
    public void shouldApplyKnownBugStrategyFromSystemProperties() {
        //given
        System.setProperty(KNOWN_BUGS, "execute");

        //when
        Strategy knownBugsStrategy = configuration.getKnownBugsStrategy();

        //then
        assertThat(knownBugsStrategy).isEqualTo(EXECUTE);
    }

    @Test
    public void shouldApplyKnownBugStrategyFromPropertiesFile() {
        //given
        System.getProperties().remove(KNOWN_BUGS);

        //when
        Strategy knownBugsStrategy = configuration.getKnownBugsStrategy();

        //then
        assertThat(knownBugsStrategy).isEqualTo(SKIP);
    }

    @Test
    public void shouldApplyIssueStatusCheckFromSystemProperties() {
        //given
        System.setProperty(ISSUE_STATUS_CHECK_ENABLED, "false");

        //when
        Boolean isEnabled = configuration.isClosedIssuesCheckEnabled();

        //then
        assertThat(isEnabled).isFalse();
    }

    @Test
    public void shouldApplyIssueStatusCheckFromPropertiesFile() {
        //given
        System.getProperties().remove(ISSUE_STATUS_CHECK_ENABLED);

        //when
        Boolean isEnabled = configuration.isClosedIssuesCheckEnabled();

        //then
        assertThat(isEnabled).isTrue();
    }

    @Test
    public void shouldApplyIssueStatusCheckLevelFromSystemProperties() {
        //given
        System.setProperty(ISSUE_STATUS_CHECK_LEVEL, "fail");

        //when
        ClosedIssuesCheckLevel level = configuration.getClosedIssuesCheckLevel();

        //then
        assertThat(level).isEqualTo(FAIL);
    }

    @Test
    public void shouldApplyIssueStatusCheckLevelFromPropertiesFile() {
        //given
        System.getProperties().remove(ISSUE_STATUS_CHECK_LEVEL);

        //when
        ClosedIssuesCheckLevel level = configuration.getClosedIssuesCheckLevel();

        //then
        assertThat(level).isEqualTo(WARN);
    }
}