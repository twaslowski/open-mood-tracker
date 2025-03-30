package de.twaslowski.moodtracker.adapter.rest;

import de.twaslowski.moodtracker.domain.entity.User;
import de.twaslowski.moodtracker.service.SessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/session")
public class SessionController {

  private final SessionService sessionService;

  @PostMapping("/validate")
  public ResponseEntity<?> getSession(@AuthenticationPrincipal User user) {
    if (user == null) {
      log.warn("Authentication failed");
      return ResponseEntity.status(401).build();
    }
    return ResponseEntity.ok(user.getTelegramId());
  }

  @PostMapping("/initiate")
  public ResponseEntity<String> initiate(@RequestBody User user) {
    String jwt = sessionService.createSessionFor(user);
    return ResponseEntity.ok(jwt);
  }
}
