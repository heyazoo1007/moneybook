package com.example.moneybook.common.exception.model;

import com.example.moneybook.common.exception.ErrorCode;

public class NotFoundException extends MoneyBookException {

    public NotFoundException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
