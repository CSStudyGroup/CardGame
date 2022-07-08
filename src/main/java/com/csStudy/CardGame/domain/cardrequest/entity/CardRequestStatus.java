package com.csStudy.CardGame.domain.cardrequest.entity;

public enum CardRequestStatus {
    PROCESS("PROCESS"),
    ACCEPTED("ACCEPTED"),
    REJECTED("REJECTED");

    private final String statusName;

    CardRequestStatus(String statusName) {
        this.statusName = statusName;
    }

    public String getStatusName() {
        return statusName;
    }
}
