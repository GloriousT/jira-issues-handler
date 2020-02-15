package com.jiraissues;


public enum Strategy {
    SKIP, FAIL, EXECUTE;

    public static Strategy fromValue(String value) {
        for (Strategy strategy : values()) {
            String strategyName = strategy.name().toLowerCase();
            if (strategyName.equals(value.toLowerCase())) {
                return strategy;
            }
        }
        return null;
    }
}
