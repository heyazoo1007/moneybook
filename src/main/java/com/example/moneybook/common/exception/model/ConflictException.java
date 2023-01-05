package com.example.moneybook.common.exception.model;

import com.example.moneybook.common.exception.ErrorCode;
import lombok.Getter;
@Getter
public class ConflictException extends MoneyBookException {

    public ConflictException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public ConflictException(ErrorCode errorCode) {
        super(errorCode.getMessage(), errorCode);
    }
}
