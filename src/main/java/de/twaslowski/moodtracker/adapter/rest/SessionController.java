package de.twaslowski.moodtracker.adapter.rest;

import de.twaslowski.moodtracker.domain.entity.User;
import de.twaslowski.moodtracker.service.SessionService;
import de.twaslowski.moodtracker.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
  private final UserService userService;

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

  @GetMapping("/create/{userId}")
  public ResponseEntity<String> create(@PathVariable long userId) {
    log.info("Creating user: {}", userId);
    var user = userService.createUserFromTelegramId(userId);
    return ResponseEntity.ok(user.getId());
  }
}
