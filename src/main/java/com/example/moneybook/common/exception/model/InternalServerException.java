package com.example.moneybook.common.exception.model;

import com.example.moneybook.common.exception.ErrorCode;

public class InternalServerException extends MoneyBookException {

    public InternalServerException(String message) {
        super(message, ErrorCode.INTERNAL_SERVER_EXCEPTION);
    }
}
