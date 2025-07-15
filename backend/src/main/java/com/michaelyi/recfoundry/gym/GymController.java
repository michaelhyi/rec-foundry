package com.michaelyi.recfoundry.gym;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@RestController
@RequestMapping("/api/v1/gyms")
public class GymController {
    @Autowired
    private GymService gymService;

    @GetMapping("/{id}")
    public ResponseEntity<Gym> getGymById(@PathVariable String id) {
        log.info("Fetching gym with ID: {}", id);

        try {
            Gym gym = gymService.getGymById(id);
            log.info("Gym found: {}", gym);

            return new ResponseEntity<>(gym, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            Gym gym = Gym.builder()
                    .error("Gym not found")
                    .build();
            return new ResponseEntity<>(gym, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            Gym gym = Gym.builder()
                    .error("An error occurred while fetching the gym")
                    .build();
            return new ResponseEntity<>(gym, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<Gym>> getAllGyms() {
        List<Gym> gyms = gymService.getAllGyms();
        return new ResponseEntity<>(gyms, HttpStatus.OK);
    }

    @PostMapping("/queue")
    public ResponseEntity<GymQueueResponse> joinQueue(@RequestBody GymQueueRequest req) {
        log.info("Processing join queue request for gym ID: {}, player ID: {}", req.getGymId(), req.getPlayerId());

        try {
            String courtNumber;

            if (req.getStrategy() == GymQueueStrategy.SHORTEST_QUEUE) {
                courtNumber = gymService.joinShortestQueue(req.getGymId(), req.getPlayerId());
            } else if (req.getStrategy() == GymQueueStrategy.BALANCED_QUEUE) {
                courtNumber = gymService.joinMostBalancedQueue(req.getGymId(), req.getPlayerId());
            } else {
                throw new IllegalArgumentException("Invalid queue strategy: " + req.getStrategy());
            }

            GymQueueResponse response = GymQueueResponse.builder()
                    .courtNumber(courtNumber)
                    .build();
            log.info("Player {} joined queue for gym {} at court {}", req.getPlayerId(), req.getGymId(), courtNumber);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.error("Invalid queue strategy for gym ID: {}, player ID: {}", req.getGymId(), req.getPlayerId(), e);

            GymQueueResponse response = GymQueueResponse.builder()
                    .error("Invalid queue strategy: " + req.getStrategy())
                    .build();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Error joining queue for gym ID: {}, player ID: {}", req.getGymId(), req.getPlayerId(), e);

            GymQueueResponse response = GymQueueResponse.builder()
                    .error("An error occurred while joining the queue")
                    .build();
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
