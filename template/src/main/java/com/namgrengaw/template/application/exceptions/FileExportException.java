package com.namgrengaw.template.application.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class FileExportException extends IOException {
    public FileExportException(String message) {
        super(message);
    }

    public FileExportException(String message, Throwable cause) {
        super(message, cause);
    }
}
