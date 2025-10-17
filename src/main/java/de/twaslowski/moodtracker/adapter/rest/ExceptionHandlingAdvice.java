package de.twaslowski.moodtracker.adapter.rest;

import de.twaslowski.moodtracker.exception.MetricAlreadyTrackedException;
import de.twaslowski.moodtracker.exception.MetricNotTrackedException;
import de.twaslowski.moodtracker.exception.NotFoundException;
import de.twaslowski.moodtracker.exception.SessionExpiredException;
import de.twaslowski.moodtracker.exception.SessionNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ExceptionHandlingAdvice {

  @ExceptionHandler({SessionNotFoundException.class, SessionExpiredException.class})
  public final ResponseEntity<ErrorResponse> handleInvalidSession(Exception ex) {
    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(NotFoundException.class)
  public final ResponseEntity<ErrorResponse> handleMetricNotFound(NotFoundException ex) {
    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(MetricNotTrackedException.class)
  public final ResponseEntity<ProblemDetail> handleMetricNotTracked(MetricNotTrackedException ex) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
  }

  @ExceptionHandler(MetricAlreadyTrackedException.class)
  public final ResponseEntity<ProblemDetail> handleMetricAlreadyTracked(MetricAlreadyTrackedException ex) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
    return ResponseEntity.status(HttpStatus.CONFLICT).body(problemDetail);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public final ResponseEntity<ProblemDetail> handleValidationException(MethodArgumentNotValidException ex) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validation failed");
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
  }

  @ExceptionHandler(Exception.class)
  public final ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
    log.error("An unexpected error occurred", ex);
    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
