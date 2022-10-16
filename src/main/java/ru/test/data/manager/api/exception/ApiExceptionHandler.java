package ru.test.data.manager.api.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.test.data.manager.api.exception.erroe.client.ClientNotFound;
import ru.test.data.manager.api.exception.erroe.product.ProductValidation;

import java.time.LocalDateTime;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ClientNotFound.class})
    public ResponseEntity<Object> handlerClientNotFound(ClientNotFound e, WebRequest request) {

        return new ResponseEntity<Object>(new ApiError(e.getMessage(),
                HttpStatus.NOT_FOUND,
                LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProductValidation.class)
    public ResponseEntity<Object> handlerProductValidation(ProductValidation e, WebRequest request) {

        return new ResponseEntity<Object>(new ApiError(e.getMessage(),
                HttpStatus.EXPECTATION_FAILED,
                LocalDateTime.now()), HttpStatus.EXPECTATION_FAILED);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException e, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return new ResponseEntity<Object>(new ApiError(e.getMessage(),
                HttpStatus.NOT_FOUND,
                LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }
}
