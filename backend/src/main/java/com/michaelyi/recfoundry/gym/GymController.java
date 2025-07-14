package com.michaelyi.recfoundry.gym;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@RestController
public class GymController {
    @Autowired
    private GymService gymService;

    @GetMapping("/api/v1/gyms/{id}")
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

    @GetMapping("/api/v1/gyms")
    public ResponseEntity<List<Gym>> getAllGyms() {
        List<Gym> gyms = gymService.getAllGyms();
        return new ResponseEntity<>(gyms, HttpStatus.OK);
    }
}
