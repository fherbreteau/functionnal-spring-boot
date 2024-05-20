package io.github.fherbreteau.functional.controller;

import io.github.fherbreteau.functional.exception.CommandException;
import io.github.fherbreteau.functional.exception.GroupException;
import io.github.fherbreteau.functional.exception.PathException;
import io.github.fherbreteau.functional.exception.UserException;
import io.github.fherbreteau.functional.model.ErrorDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class FunctionalExceptionHandler {

    @ExceptionHandler(CommandException.class)
    public ResponseEntity<ErrorDTO> handleCommandException(CommandException e) {
        ErrorDTO error = new ErrorDTO();
        error.setType(e.getClass().getSimpleName());
        error.setMessage(e.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(PathException.class)
    public ResponseEntity<ErrorDTO> handlePathException(PathException e) {
        ErrorDTO error = new ErrorDTO();
        error.setType(e.getClass().getSimpleName());
        error.setMessage(e.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorDTO> handleUserException(UserException e) {
        ErrorDTO error = new ErrorDTO();
        error.setType(e.getClass().getSimpleName());
        error.setMessage(e.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(GroupException.class)
    public ResponseEntity<ErrorDTO> handlePathException(GroupException e) {
        ErrorDTO error = new ErrorDTO();
        error.setType(e.getClass().getSimpleName());
        error.setMessage(e.getMessage());
        return ResponseEntity.badRequest().body(error);
    }
}
