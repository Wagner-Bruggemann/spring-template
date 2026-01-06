package com.namgrengaw.template.application.adapters.in.dtos;

import java.util.Date;

public record ExceptionResponse(Date timestamp, String message, String details) {}
