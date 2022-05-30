package com.csStudy.CardGame.domain;

public enum RequestStatus {
    PROCESS("PROCESS"),
    ACCEPTED("ACCEPTED"),
    REJECTED("REJECTED");

    private final String statusName;

    RequestStatus(String statusName) {
        this.statusName = statusName;
    }

    public String getStatusName() {
        return statusName;
    }
}
