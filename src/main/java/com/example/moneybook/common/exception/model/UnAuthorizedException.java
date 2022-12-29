package com.example.moneybook.common.exception.model;

import com.example.moneybook.common.exception.ErrorCode;

public class UnAuthorizedException extends MoneyBookException {

    public UnAuthorizedException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
