package de.twaslowski.moodtracker.adapter.rest;

import de.twaslowski.moodtracker.domain.dto.RecordDTO;
import de.twaslowski.moodtracker.domain.entity.User;
import de.twaslowski.moodtracker.service.RecordService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class RecordController {

  private final RecordService recordService;

  @GetMapping("/records")
  public ResponseEntity<List<RecordDTO>> getRecords(@AuthenticationPrincipal User user,
                                                    @RequestParam(required = false) String from,
                                                    @RequestParam(required = false) String to
  ) {
    log.info("Getting records for user {} from {} to {}", user.getId(), from, to);
    List<RecordDTO> records = recordService.getRecords(user.getId());
    return ResponseEntity.ok(records);
  }
}
