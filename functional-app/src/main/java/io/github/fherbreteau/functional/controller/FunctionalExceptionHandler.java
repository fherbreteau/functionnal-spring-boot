package io.github.fherbreteau.functional.controller;

import io.github.fherbreteau.functional.exception.CommandException;
import io.github.fherbreteau.functional.exception.PathException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class FunctionalExceptionHandler {

    @ExceptionHandler(CommandException.class)
    public ResponseEntity<Object> handleCommandException(CommandException e) {
        return ResponseEntity.badRequest().body(null);
    }

    @ExceptionHandler(PathException.class)
    public ResponseEntity<Object> handlePathException(PathException e) {
        return ResponseEntity.badRequest().body(null);
    }
}
