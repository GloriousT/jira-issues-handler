package com.jiraissues.listeners;

import com.jiraissues.Configuration;
import com.jiraissues.FailedTestsKeeper;
import com.jiraissues.PotentiallyClosed;
import org.testng.ISuite;
import org.testng.ISuiteListener;

import static com.internal.jira.common.ClientProvider.jiraClient;

public class KnownBugsSuiteLogger implements ISuiteListener {

    @Override
    public void onStart(ISuite iSuite) {
        /*NOP*/
    }

    @Override
    public void onFinish(ISuite iSuite) {
        FailedTestsKeeper failedTestsKeeper = FailedTestsKeeper.getInstance();
        failedTestsKeeper.logFailedTests();

        performCheck(failedTestsKeeper);
    }

    private void performCheck(FailedTestsKeeper failedTestsKeeper) {
        Configuration config = Configuration.getInstanace();
        if (config.isClosedIssuesCheckEnabled()) {
            PotentiallyClosed issueIds = new PotentiallyClosed(jiraClient(), config);
            issueIds.performCheck(failedTestsKeeper.getMentionedJiraIds());
        }
    }
}
