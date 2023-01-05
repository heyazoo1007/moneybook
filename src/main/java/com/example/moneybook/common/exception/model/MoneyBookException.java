package com.example.moneybook.common.exception.model;

import com.example.moneybook.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public class MoneyBookException extends RuntimeException {

    private final ErrorCode errorCode;

    public MoneyBookException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public int getStatus() {
        return errorCode.getStatus();
    }
}
