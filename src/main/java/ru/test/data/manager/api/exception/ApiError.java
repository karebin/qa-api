package ru.test.data.manager.api.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ApiError {
    private String message;
    private HttpStatus httpStatus;
    private LocalDateTime timeStamp;

}
