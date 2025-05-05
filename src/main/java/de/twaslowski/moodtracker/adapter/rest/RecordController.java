package de.twaslowski.moodtracker.adapter.rest;

import de.twaslowski.moodtracker.domain.dto.RecordDTO;
import de.twaslowski.moodtracker.domain.entity.User;
import de.twaslowski.moodtracker.service.RecordService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class RecordController {

  private final RecordService recordService;

  @GetMapping("/record/{userId}")
  public ResponseEntity<List<RecordDTO>> getRecords(@AuthenticationPrincipal User user,
                                                    @PathVariable String userId,
                                                    @RequestParam(required = false) String from,
                                                    @RequestParam(required = false) String to
  ) {
    log.info("Getting records for user {} from {} to {}", userId, from, to);
    if (userId == null || userId.isEmpty()) {
      log.warn("User ID is null or empty");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    if (user == null || !user.getId().equals(userId)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    List<RecordDTO> records = recordService.getRecords(userId);
    return ResponseEntity.ok(records);
  }

//  private Date parseDate(String date, Date fallback) {
//    if (date == null || date.isEmpty()) {
//      return fallback;
//    }
//    try {
//      return Date.from(ZonedDateTime.parse(date).toInstant());
//    } catch (Exception e) {
//      log.warn("Failed to parse date: {}", date, e);
//      return fallback;
//    }
//  }
}
