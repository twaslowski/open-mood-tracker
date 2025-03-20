package de.twaslowski.moodtracker.service;

import static java.lang.String.format;

import de.twaslowski.moodtracker.domain.entity.ConfigurationSession;
import de.twaslowski.moodtracker.domain.entity.User;
import de.twaslowski.moodtracker.repository.ConfigurationSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConfigurationSessionService {

  @Value("${mood-tracker.ui.host}")
  private String configurationBaseUrl;

  private final ConfigurationSessionRepository configurationSessionRepository;

  public String createSessionFor(User user) {
    var session = configurationSessionRepository.save(ConfigurationSession.builder()
        .user(user)
        .build());
    return format("%s/%s", configurationBaseUrl, session.getUuid());
  }
}
