package de.twaslowski.moodtracker.adapter.rest;

import de.twaslowski.moodtracker.exception.SessionExpiredException;
import de.twaslowski.moodtracker.exception.SessionNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ExceptionHandlingAdvice {

  @ExceptionHandler({SessionNotFoundException.class, SessionExpiredException.class})
  public final ResponseEntity<ErrorResponse> handleInvalidUrl(Exception ex) {
    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(Exception.class)
  public final ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
    log.error("An unexpected error occurred", ex);
    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
