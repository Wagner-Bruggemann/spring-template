package com.namgrengaw.template.application.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class FileImportException extends IOException {
    public FileImportException(String message) {
        super(message);
    }

    public FileImportException(String message, Throwable cause) {
        super(message, cause);
    }
}
