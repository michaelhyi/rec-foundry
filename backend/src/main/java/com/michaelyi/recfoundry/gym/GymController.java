package com.michaelyi.recfoundry.gym;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GymController {
    @Autowired
    private GymService gymService;

    @GetMapping("/api/v1/gyms")
    public ResponseEntity<List<Gym>> getAllGyms() {
        List<Gym> gyms = gymService.getAllGyms();
        return new ResponseEntity<>(gyms, HttpStatus.OK);
    }
}
