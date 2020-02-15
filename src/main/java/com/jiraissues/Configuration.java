package com.jiraissues;

import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

import static java.util.Objects.isNull;

public class Configuration {

    private static final String PROPERTIES_FILENAME = "test.properties";
    private static Properties properties = new Properties();
    private Strategy knownBugStrategy;
    private Boolean isClosedIssuesCheckEnabled;
    private ClosedIssuesCheckLevel closedIssuesCheckLevel;

    private static Configuration instance;

    Configuration() {

    }

    void setStrategy(Strategy strategy) {
        knownBugStrategy = strategy;
    }

    public static Configuration getInstanace() {
        if (null == instance) {
            instance = new Configuration();
        }
        return instance;
    }

    public Strategy getKnownBugsStrategy() {
        if (isNull(knownBugStrategy)) {
            knownBugStrategy = readKnownBugsStrategy()
                    .map(Strategy::fromValue)
                    .orElse(Strategy.SKIP);
        }
        return knownBugStrategy;
    }

    public boolean isClosedIssuesCheckEnabled() {
        String propertyName = "closed.issues.check.enabled";
        String defaultValue = "true";
        if (isNull(isClosedIssuesCheckEnabled)) {
            String isEnabled = readProperty(propertyName, defaultValue)
                    .orElse("true");
            isClosedIssuesCheckEnabled = Boolean.valueOf(isEnabled);
        }
        return isClosedIssuesCheckEnabled;
    }

    public ClosedIssuesCheckLevel getClosedIssuesCheckLevel() {
        if (isNull(closedIssuesCheckLevel)) {
            closedIssuesCheckLevel = readProperty("closed.issues.check.level", ClosedIssuesCheckLevel.WARN.name().toLowerCase())
                    .map(ClosedIssuesCheckLevel::fromValue)
                    .orElse(ClosedIssuesCheckLevel.WARN);
        }
        return closedIssuesCheckLevel;
    }

    private static Optional<String> readKnownBugsStrategy() {
        String propertyName = "known.bugs";
        String defaultValue = Strategy.SKIP.name().toLowerCase();
        return readProperty(propertyName, defaultValue);
    }

    private static Optional<String> readProperty(String propertyName, String defaultValue) {
        String strategy = System.getProperty(propertyName);
        if (strategy == null) {
            try {
                properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(PROPERTIES_FILENAME));
                strategy = properties.getProperty(propertyName, defaultValue);
            } catch (IOException e) {
                //properties file not found, ignore - default strategy will be used
            }
        }
        return Optional
                .ofNullable(strategy);
    }
}
