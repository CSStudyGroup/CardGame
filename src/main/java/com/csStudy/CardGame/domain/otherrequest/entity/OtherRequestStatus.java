package com.csStudy.CardGame.domain.otherrequest.entity;

public enum OtherRequestStatus {
    PROCESS("PROCESS"),
    ACCEPTED("ACCEPTED"),
    REJECTED("REJECTED");

    private final String statusName;

    OtherRequestStatus(String statusName) {
        this.statusName = statusName;
    }

    public String getStatusName() {
        return statusName;
    }
}
