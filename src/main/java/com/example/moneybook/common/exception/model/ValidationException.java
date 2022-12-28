package com.example.moneybook.common.exception.model;

import com.example.moneybook.common.exception.ErrorCode;

public class ValidationException extends MoneyBookException {

    public ValidationException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
