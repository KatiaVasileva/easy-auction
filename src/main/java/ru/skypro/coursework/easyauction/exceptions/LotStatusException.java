package ru.skypro.coursework.easyauction.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class LotStatusException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Invalid status of the lot!";

    public LotStatusException() {
        super(DEFAULT_MESSAGE);
    }
}
