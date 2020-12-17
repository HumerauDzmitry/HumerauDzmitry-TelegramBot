package com.telegram.bot.exception;

import org.zalando.problem.StatusType;

public enum ErrorType {

    PUSH,
    INFO,
    INFO_DETAILS,
    INFO_DETAILS_ARCHIVE,
    BUSINESS_VALIDATION_ERROR,
    SYSTEM_VALIDATION_ERROR,
    SYSTEM_ERROR,
    DISPLAY_WARNING;

    public static ErrorType fromStatus(StatusType status) {
        if (status == null) return null;
        int code = status.getStatusCode();
        if (code >= 500) {
            return SYSTEM_ERROR;
        }
        if (code >= 400) {
            return SYSTEM_VALIDATION_ERROR;
        }
        return INFO; //TODO может и не надо заполнять
    }
}
