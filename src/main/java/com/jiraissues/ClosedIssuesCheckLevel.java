package com.jiraissues;


public enum ClosedIssuesCheckLevel {
    WARN, FAIL;

    public static ClosedIssuesCheckLevel fromValue(String value) {
        for (ClosedIssuesCheckLevel strategy : values()) {
            String strategyName = strategy.name().toLowerCase();
            if (strategyName.equals(value.toLowerCase())) {
                return strategy;
            }
        }
        return null;
    }
}
