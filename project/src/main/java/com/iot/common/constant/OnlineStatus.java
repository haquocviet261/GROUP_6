package com.iot.common.constant;

public enum OnlineStatus {
    ONLINE(1L),
    OFFLINE(0L);
    private final Long code;

    OnlineStatus(Long code) {
        this.code = code;
    }

    public Long getCode() {
        return this.code;
    }

    public static OnlineStatus fromCode(Long code) {
        for (OnlineStatus status : OnlineStatus.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Cannot find status Code: " + code);
    }
}
