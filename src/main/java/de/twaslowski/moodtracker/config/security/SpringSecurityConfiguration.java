package de.twaslowski.moodtracker.config.security;

import static org.springframework.security.config.http.SessionCreationPolicy.NEVER;

import de.twaslowski.moodtracker.config.RequestResponseLoggingFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SpringSecurityConfiguration {

  @Bean
  public SecurityFilterChain configureWebSecurity(HttpSecurity httpSecurity,
                                                  RequestResponseLoggingFilter loggingFilter,
                                                  JwtFilter jwtFilter
  ) throws Exception {
    return httpSecurity
        .csrf(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(loggingFilter, JwtFilter.class)
        .authorizeHttpRequests(this::configureRestAuthorizations)
        .sessionManagement(session -> session.sessionCreationPolicy(NEVER))
        .build();
  }

  private void configureRestAuthorizations(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorizationRegistry) {
    authorizationRegistry
        .requestMatchers("/actuator/health").permitAll()
        .requestMatchers("/api/v1/session/**").permitAll()
        .requestMatchers("/api/v1/metric").authenticated()
        .requestMatchers("/api/v1/metric/**").authenticated()
        .requestMatchers("/api/v1/records").authenticated()
        .requestMatchers("/api/v1/record/**").authenticated()
        .requestMatchers("/api/v2/records").authenticated()
        .anyRequest().denyAll();
  }
}
