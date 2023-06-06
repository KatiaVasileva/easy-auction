package ru.skypro.coursework.easyauction.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class LotNotFoundException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Lot is not found!";

    public LotNotFoundException() {
        super(DEFAULT_MESSAGE);
    }
}