package ru.skypro.coursework.easyauction.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BidNotFoundException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Bid is not found!";

    public BidNotFoundException() {
            super(DEFAULT_MESSAGE);
    }
}
