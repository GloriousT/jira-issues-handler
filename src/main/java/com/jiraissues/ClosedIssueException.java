package com.jiraissues;

class ClosedIssueException extends RuntimeException {
    ClosedIssueException(String message) {
        super(message);
    }
}
