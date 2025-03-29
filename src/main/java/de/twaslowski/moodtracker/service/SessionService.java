package de.twaslowski.moodtracker.service;

import static java.lang.String.format;

import de.twaslowski.moodtracker.domain.entity.ConfigurationSession;
import de.twaslowski.moodtracker.domain.entity.User;
import de.twaslowski.moodtracker.exception.SessionExpiredException;
import de.twaslowski.moodtracker.exception.SessionNotFoundException;
import de.twaslowski.moodtracker.repository.ConfigurationSessionRepository;
import java.time.ZonedDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionService {

  @Value("${mood-tracker.configuration.host}")
  private String configurationBaseUrl;

  @Value("${mood-tracker.configuration.session.expiry}")
  private long sessionExpiry;

  private final ConfigurationSessionRepository configurationSessionRepository;

  public String createSessionFor(User user) {
    var session = configurationSessionRepository.save(ConfigurationSession.builder()
        .user(user)
        .sessionExpiry(ZonedDateTime.now().plusSeconds(sessionExpiry))
        .build());
    return format("%s?token=%s", configurationBaseUrl, session.getId());
  }

  public ConfigurationSession retrieveSession(String uuid) {
    var configurationSession = configurationSessionRepository.findById(uuid)
        .orElseThrow(SessionNotFoundException::new);
    if (configurationSession.getSessionExpiry().isAfter(ZonedDateTime.now())) {
      return configurationSession;
    } else {
      configurationSessionRepository.delete(configurationSession);
      throw new SessionExpiredException();
    }
  }
}
