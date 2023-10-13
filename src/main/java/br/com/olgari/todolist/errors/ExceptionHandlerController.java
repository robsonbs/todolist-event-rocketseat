package br.com.olgari.todolist.errors;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController {
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
    return ResponseEntity.badRequest().body(ex.getMostSpecificCause().getMessage());
  }

  @ExceptionHandler(RequestErrorException.class)
  public ResponseEntity<String> handleHttpMessageNotReadableException(RequestErrorException ex) {
    return ResponseEntity.badRequest().body(ex.getMessage());
  }
}
