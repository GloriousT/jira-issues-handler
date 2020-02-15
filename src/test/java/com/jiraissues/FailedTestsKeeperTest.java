package com.jiraissues;

import com.jiraissues.annotations.Bug;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

public class FailedTestsKeeperTest {

    private static final String POSSIBLE_ROOT_CAUSE1 = "report1";
    private static final String POSSIBLE_ROOT_CAUSE2 = "report2";
    private static final String SINGLE_ROOT_CAUSE = "some_value";
    private FailedTestsKeeper failedTestsKeeper = FailedTestsKeeper.getInstance();

    @AfterMethod
    private void after() {
        failedTestsKeeper.wipeRecords();
    }

    // this is not the test in fact. It is just to see the behavior
    @Test
    public void shouldStoreTestResults() throws NoSuchMethodException {
        //given
        Method annotatedMethod = MethodProvider.class.getMethod("annotatedMethod");
        Method notAnnotatedMethod = MethodProvider.class.getMethod("notAnnotatedMethod");

        failedTestsKeeper.keepTestResult(annotatedMethod);
        failedTestsKeeper.keepTestResult(notAnnotatedMethod);

        //when
        failedTestsKeeper.logFailedTests();

        //then
    }

    @Test
    public void shouldStoreTestResultsForMultipleReports() throws NoSuchMethodException {
        //given
        Method annotatedMethod = MethodProvider.class.getMethod("annotatedWithMultipleReportsMethod");

        failedTestsKeeper.keepTestResult(annotatedMethod);

        //when
        failedTestsKeeper.logFailedTests();

        //then
    }

    @Test
    public void shouldReturnSinglePossibleRootCause() throws NoSuchMethodException {
        //given
        Method annotatedMethod = MethodProvider.class.getMethod("annotatedMethod");
        failedTestsKeeper.keepTestResult(annotatedMethod);

        //when
        Collection<String> mentionedJiraTickets = failedTestsKeeper.getMentionedJiraIds();

        //then
        assertThat(mentionedJiraTickets).containsOnly(SINGLE_ROOT_CAUSE);
    }

    @Test
    public void shouldReturnListOfPossibleRootCauses() throws NoSuchMethodException {
        //given
        Method annotatedMethod = MethodProvider.class.getMethod("annotatedWithMultipleReportsMethod");
        failedTestsKeeper.keepTestResult(annotatedMethod);

        //when
        Collection<String> mentionedJiraTickets = failedTestsKeeper.getMentionedJiraIds();

        //then
        assertThat(mentionedJiraTickets).containsExactlyInAnyOrder(POSSIBLE_ROOT_CAUSE1, POSSIBLE_ROOT_CAUSE2);
    }

    @Test
    public void shouldNotStoreTestResultsIfNoTestFailures() throws NoSuchMethodException {
        //when
        failedTestsKeeper.logFailedTests();
    }

    private class MethodProvider {

        public void notAnnotatedMethod() {
            /*shame*/
        }

        @Bug(value = SINGLE_ROOT_CAUSE)
        public void annotatedMethod() {
            /*shame*/
        }

        @Bug(value = {POSSIBLE_ROOT_CAUSE1, POSSIBLE_ROOT_CAUSE2})
        public void annotatedWithMultipleReportsMethod() {
            /*shame*/
        }
    }
}
