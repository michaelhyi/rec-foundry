package com.michaelyi.recfoundry.court;

import com.michaelyi.recfoundry.player.Player;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class CourtController {
    @Autowired
    private CourtRepository courtRepository;

    @Autowired
    private CourtService courtService;

    @PostMapping("/api/v1/courts/queue")
    public ResponseEntity<Void> joinQueue(@RequestBody CourtQueueRequest req) {
        try {
            courtRepository.queuePlayer(req.getCourtId(), req.getPlayerId());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/api/v1/courts/{id}/queue")
    public ResponseEntity<List<Player>> getQueue(@PathVariable String id) {
        try {
            List<Player> queue = courtRepository.getQueue(id);
            return new ResponseEntity<>(queue, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/api/v1/courts/next-team")
    public ResponseEntity<Void> nextTeam(@RequestBody CourtPopTeamRequest req) {
        log.info("Processing next team request for court ID: {}, team ID: {}", req.getCourtId(), req.getTeamId());

        try {
            courtService.popTeam(req);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
