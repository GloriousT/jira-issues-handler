package com.jiraissues;

import com.internal.jira.client.JiraClient;
import com.internal.jira.model.issue.IssueStatus;
import com.google.common.collect.ImmutableSet;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Set;

import static com.jiraissues.ClosedIssuesCheckLevel.FAIL;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

public class PotentiallyClosedTest {

    private static final String CLOSED_ISSUE = "CLOSED_ISSUE";
    private static final String READY_FOR_TEST_ISSUE = "READY_FOR_TEST_ISSUE";
    private static final String NOT_CLOSED_ISSUE = "NOT_CLOSED";

    @Mock private JiraClient jiraClient;
    @Mock private Configuration configuration;
    @InjectMocks private PotentiallyClosed potentiallyClosed;

    @BeforeMethod
    private void beforeMethod() {
        potentiallyClosed = new PotentiallyClosed(jiraClient, configuration);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldLogPotentiallyClosedStatusesIssues() {
        List<String> closedIssues = singletonList(CLOSED_ISSUE);
        jiraRequestedFor(CLOSED_ISSUE).willReturn(issueWithStatus("Closed"));

        Set<String> issuesToLog = potentiallyClosed.performCheck(closedIssues);

        assertThat(issuesToLog).containsExactly(CLOSED_ISSUE);
    }

    @Test
    public void shouldMultipleLogPotentiallyClosedStatusesIssues() {
        Set<String> closedIssues = ImmutableSet.of(CLOSED_ISSUE, READY_FOR_TEST_ISSUE);
        jiraRequestedFor(CLOSED_ISSUE).willReturn(issueWithStatus("Closed"));
        jiraRequestedFor(READY_FOR_TEST_ISSUE).willReturn(issueWithStatus("Ready for Test"));

        Set<String> issuesToLog = potentiallyClosed.performCheck(closedIssues);

        assertThat(issuesToLog).hasSameElementsAs(ImmutableSet.of(CLOSED_ISSUE));
    }

    @Test
    public void shouldNotLogNotPotentiallyClosedStatusesIssues() {
        List<String> notClosedIssue = singletonList(NOT_CLOSED_ISSUE);
        jiraRequestedFor(NOT_CLOSED_ISSUE).willReturn(issueWithStatus("Open"));

        Set<String> issuesToLog = potentiallyClosed.performCheck(notClosedIssue);

        assertThat(issuesToLog).isEmpty();
    }

    @Test(expectedExceptions = ClosedIssueException.class)
    public void shouldThrowExceptionWhenIssueIsClosed() {
        Set<String> closedIssues = ImmutableSet.of(CLOSED_ISSUE, READY_FOR_TEST_ISSUE);
        jiraRequestedFor(CLOSED_ISSUE).willReturn(issueWithStatus("Closed"));
        jiraRequestedFor(READY_FOR_TEST_ISSUE).willReturn(issueWithStatus("Closed"));
        given(configuration.getClosedIssuesCheckLevel()).willReturn(FAIL);

        potentiallyClosed.performCheck(closedIssues);
    }

    private BDDMockito.BDDMyOngoingStubbing<IssueStatus> jiraRequestedFor(String issueStatus) {
        return given(jiraClient.getIssueStatus(issueStatus));
    }

    private IssueStatus issueWithStatus(String status) {
        return new IssueStatus(null, null, null, status, null, null);
    }
}