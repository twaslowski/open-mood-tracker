package de.twaslowski.moodtracker.adapter.rest;

import de.twaslowski.moodtracker.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/session")
public class SessionController {

  private final SessionService sessionService;

  @GetMapping("/{id}")
  public ResponseEntity<Long> getSession(@PathVariable String id) {
    return ResponseEntity.ok(sessionService.retrieveSession(id).getUser().getId());
  }
}
