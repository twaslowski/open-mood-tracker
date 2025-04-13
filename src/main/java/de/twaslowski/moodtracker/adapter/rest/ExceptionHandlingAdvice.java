package de.twaslowski.moodtracker.adapter.rest;

import de.twaslowski.moodtracker.exception.MetricAlreadyTrackedException;
import de.twaslowski.moodtracker.exception.MetricNotFoundException;
import de.twaslowski.moodtracker.exception.MetricNotTrackedException;
import de.twaslowski.moodtracker.exception.SessionExpiredException;
import de.twaslowski.moodtracker.exception.SessionNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ExceptionHandlingAdvice {

  @ExceptionHandler({SessionNotFoundException.class, SessionExpiredException.class})
  public final ResponseEntity<ErrorResponse> handleInvalidUrl(Exception ex) {
    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(MetricNotFoundException.class)
  public final ResponseEntity<ErrorResponse> handleMetricNotFound(MetricNotFoundException ex) {
    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MetricNotTrackedException.class)
  public final ResponseEntity<ProblemDetail> handleMetricNotTracked(MetricNotTrackedException ex) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(problemDetail);
  }

  @ExceptionHandler(MetricAlreadyTrackedException.class)
  public final ResponseEntity<ProblemDetail> handleMetricAlreadyTracked(MetricAlreadyTrackedException ex) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
    return ResponseEntity.status(HttpStatus.CONFLICT).body(problemDetail);
  }

  @ExceptionHandler(Exception.class)
  public final ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
    log.error("An unexpected error occurred", ex);
    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
