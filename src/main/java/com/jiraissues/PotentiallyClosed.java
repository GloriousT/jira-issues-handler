package com.jiraissues;

import com.internal.jira.client.JiraClient;
import com.internal.jira.model.issue.IssueStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PotentiallyClosed {

    private static final Logger LOG = LoggerFactory.getLogger(PotentiallyClosed.class);
    private static final String ERROR_MESSAGE = "Issue %s is in %s state but still present in the @Bug annotation";

    private final Set<String> potentiallyClosedIssueIds = new HashSet<>();
    private final JiraClient jira;
    private final Configuration configuration;

    public PotentiallyClosed(JiraClient jiraClient, Configuration configuration) {
        this.jira = jiraClient;
        this.configuration = configuration;
    }

    public Set<String> performCheck(Collection<String> issueIds) {
        Map<String, IssueStatus> issues = collectTicketsStatuses(issueIds);
        issues.forEach(this::checkIrrelevantStatuses);
        failBuildIfNeeded();
        return potentiallyClosedIssueIds;
    }

    private void failBuildIfNeeded() {
        if (ClosedIssuesCheckLevel.FAIL == configuration.getClosedIssuesCheckLevel() && !potentiallyClosedIssueIds.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            potentiallyClosedIssueIds.forEach(it -> {
                builder.append("\n");
                builder.append(String.format(ERROR_MESSAGE, it, "Closed"));
                builder.append("\n");
            });
            throw new ClosedIssueException(builder.toString());
        }
    }

    private Map<String, IssueStatus> collectTicketsStatuses(Collection<String> issueIds) {
        Map<String, IssueStatus> issues = new HashMap<>();
        ExecutorService executor = Executors.newFixedThreadPool(10);
        issueIds.forEach(issueId -> executor.execute(getIssueStatus(issues, issueId)));

        awaitTerminationAfterShutdown(executor);
        return issues;
    }

    private Runnable getIssueStatus(Map<String, IssueStatus> issues, String issueId) {
        return () -> {
            try {
                IssueStatus issueStatus = jira.getIssueStatus(issueId);
                issues.put(issueId, issueStatus);
            } catch (Exception e) {
                LOG.error("Failed retrieving status for issue {}", issueId);
                LOG.debug("Exception when retrieving status for issue " + issueId, e);
            }
        };
    }

    private void checkIrrelevantStatuses(String issueKey, IssueStatus issueStatus) {
        if ("Closed".equals(issueStatus.getName())) {
            LOG.error(String.format(ERROR_MESSAGE, issueKey, "Closed"));
            potentiallyClosedIssueIds.add(issueKey);
        }
    }

    private void awaitTerminationAfterShutdown(ExecutorService threadPool) {
        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(5, TimeUnit.SECONDS)) {
                threadPool.shutdownNow();
            }
        } catch (InterruptedException ex) {
            threadPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
