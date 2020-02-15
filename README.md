# Bugs handler

It happens often that the test is failing for a known bug which has been already reported but development plans to fix in remote future.
In such case test suite will be broken ("red") for long time. This small library addresses this issue, so that testers 
can track which tests are failing for known bugs and have more control over running them.

## Important! Current implementation works with the TestNG starting from version 6.11

## Marking tests

Each such test can be annotated with `@Bug` annotation, e.g.:

    @Test
    @Bug("BUG-509")
    public void checkWhether() {...
    
## Controlling test execution

Tests for which a bug has been reported can be:

- skipped (by default)

        mvn test -Dknown.bugs=skip

- failed without execution
    
        mvn test -Dknown.bugs=fail
    
- executed

        mvn test -Dknown.bugs=execute

Just remember to declare usage of `JiraIssuesTestNGListener` TestNG listener for your tests:
    
    <listener class-name="JiraIssuesTestNGListener" />

If you would like to have the known bugs logged after suite is finished - add one more listener:
    
    <listener class-name="KnownBugsSuiteLogger" />
    
## Usage

Include the Maven dependency in your project POM:

        <dependency>
            <groupId>com.internal</groupId>
            <artifactId>jira-issues-handler</artifactId>
            <version>${latestVersion}</version>
        </dependency>

