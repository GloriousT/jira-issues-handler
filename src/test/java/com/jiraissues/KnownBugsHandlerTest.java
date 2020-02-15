package com.jiraissues;

import com.jiraissues.listeners.JiraIssuesTestNGListener;
import com.jiraissues.testdata.AlwaysFailingTest;
import com.jiraissues.testdata.AlwaysPassingTest;
import com.jiraissues.testdata.FailedOnBeforeClassTest;
import com.jiraissues.testdata.FailedOnBeforeMethodTest;
import org.testng.TestListenerAdapter;
import org.testng.TestNG;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class KnownBugsHandlerTest {

    private JiraIssuesTestNGListener knownBugsHandler;
    private TestListenerAdapter testListenerAdapter;
    private Configuration configuration;

    @BeforeMethod
    public void setup() {
        configuration = Configuration.getInstanace();
        knownBugsHandler = new JiraIssuesTestNGListener();
        testListenerAdapter = new TestListenerAdapter();
    }

    @Test
    public void shouldRespectSkipStrategy() {
        configuration.setStrategy(Strategy.SKIP);
        TestNG testng = prepareTestRunner(AlwaysFailingTest.class);

        testng.run();

        assertThat(testListenerAdapter.getSkippedTests().size()).isEqualTo(1);
        assertThat(testListenerAdapter.getFailedTests().size()).isEqualTo(0);
        assertThat(testListenerAdapter.getPassedTests().size()).isEqualTo(0);
    }

    @Test
    public void shouldRespectFailStrategy() {
        configuration.setStrategy(Strategy.FAIL);

        TestNG testng = prepareTestRunner(AlwaysPassingTest.class);

        testng.run();
        assertThat(testListenerAdapter.getSkippedTests().size()).isEqualTo(0);
        assertThat(testListenerAdapter.getFailedTests().size()).isEqualTo(1);
        assertThat(testListenerAdapter.getPassedTests().size()).isEqualTo(0);
    }

    @Test
    public void shouldRespectExecuteStrategy() {
        configuration.setStrategy(Strategy.EXECUTE);
        TestNG testng = prepareTestRunner(AlwaysFailingTest.class, AlwaysPassingTest.class);

        testng.run();

        assertThat(testListenerAdapter.getSkippedTests().size()).isEqualTo(0);
        assertThat(testListenerAdapter.getFailedTests().size()).isEqualTo(1);
        assertThat(testListenerAdapter.getPassedTests().size()).isEqualTo(1);
    }

    @DataProvider
    public Object[][] beforeConfigurationMethods() {
        return new Object[][]{
                {FailedOnBeforeClassTest.class},
                {FailedOnBeforeMethodTest.class}
        };
    }

    @Test(dataProvider = "beforeConfigurationMethods")
    public void shouldRespectSkipStrategyOnBeforeConfigurationMethods(Class testClass) {
        //given
        configuration.setStrategy(Strategy.SKIP);

        TestNG testng = prepareTestRunner(testClass);

        //when
        testng.run();

        //then
        assertThat(testListenerAdapter.getSkippedTests().size()).isEqualTo(1);
        assertThat(testListenerAdapter.getConfigurationSkips().size()).isEqualTo(1);
        assertThat(testListenerAdapter.getFailedTests().size()).isEqualTo(0);
        assertThat(testListenerAdapter.getPassedTests().size()).isEqualTo(0);
    }

    @Test(dataProvider = "beforeConfigurationMethods")
    public void shouldRespectFailStrategyOnBeforeConfigurationMethods(Class testClass) {
        //given
        configuration.setStrategy(Strategy.FAIL);
        TestNG testng = prepareTestRunner(testClass);

        //when
        testng.run();

        //then
        assertThat(testListenerAdapter.getSkippedTests().size()).isEqualTo(1);
        assertThat(testListenerAdapter.getConfigurationSkips().size()).isEqualTo(0);
        assertThat(testListenerAdapter.getConfigurationFailures().size()).isEqualTo(1);
        assertThat(testListenerAdapter.getFailedTests().size()).isEqualTo(0);
        assertThat(testListenerAdapter.getPassedTests().size()).isEqualTo(0);
    }

    private TestNG prepareTestRunner(Class... testClasses) {
        TestNG testng = new TestNG();
        testng.setTestClasses(testClasses);
        testng.addListener(testListenerAdapter);
        testng.addListener(knownBugsHandler);
        return testng;
    }
}
