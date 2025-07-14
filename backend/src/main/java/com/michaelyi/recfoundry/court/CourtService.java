package com.michaelyi.recfoundry.court;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourtService {
    @Autowired
    private CourtRepository courtRepository;

    public void popTeam(CourtPopTeamRequest req) throws JsonProcessingException {
        courtRepository.clearTeam(req.getCourtId(), req.getTeamId());
        courtRepository.nextTeam(req.getCourtId(), req.getTeamId());
    }
}
