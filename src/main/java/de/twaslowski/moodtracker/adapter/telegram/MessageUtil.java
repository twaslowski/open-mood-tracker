package de.twaslowski.moodtracker.adapter.telegram;

import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageUtil {

  private final MessageSource messageSource;

  public String getMessage(String code) {
    return messageSource.getMessage(code, null, Locale.ENGLISH);
  }

  public String getMessage(String code, Object... args) {
    return messageSource.getMessage(code, args, Locale.ENGLISH);
  }
}
