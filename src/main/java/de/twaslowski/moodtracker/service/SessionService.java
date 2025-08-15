package de.twaslowski.moodtracker.service;

import static java.lang.String.format;

import de.twaslowski.moodtracker.domain.entity.User;
import de.twaslowski.moodtracker.exception.SessionExpiredException;
import de.twaslowski.moodtracker.exception.SessionNotFoundException;
import de.twaslowski.moodtracker.exception.UserNotFoundException;
import de.twaslowski.moodtracker.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SessionService {

  private final UserRepository userRepository;

  @Value("${mood-tracker.configuration.url}")
  private String configurationBaseUrl;

  @Value("${mood-tracker.configuration.jwt.expiry}")
  private long sessionExpiry;

  @Value("${mood-tracker.configuration.jwt.key}")
  private String jwtSecret;

  public String createSessionFor(User user) {
    Date expiryDate = Date.from(ZonedDateTime.now().plusSeconds(sessionExpiry).toInstant());

    String token = Jwts.builder()
        .setClaims(Map.of())
        .setSubject(user.getId())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(expiryDate)
        .signWith(getSigningKey(), SignatureAlgorithm.HS256)
        .compact();

    return format("%s?token=%s", configurationBaseUrl, token);
  }

  private SecretKey getSigningKey() {
    return Keys.hmacShaKeyFor(jwtSecret.getBytes());
  }

  public User validateAndGetUser(String token) {
    try {
      var userId = extractAllClaims(token).getSubject();
      return userRepository.findById(userId)
          .orElseThrow(() -> new UserNotFoundException(userId));
    } catch (ExpiredJwtException e) {
      throw new SessionExpiredException();
    } catch (JwtException e) {
      throw new SessionNotFoundException();
    }
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(getSigningKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }
}
