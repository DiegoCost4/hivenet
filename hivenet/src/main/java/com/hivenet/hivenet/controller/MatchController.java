package com.hivenet.hivenet.controller;

import com.hivenet.hivenet.model.Match;
import com.hivenet.hivenet.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matches")
public class MatchController {

    @Autowired
    private MatchService matchService;

    @PostMapping("/{projectId}/like")
    public ResponseEntity<String> likeProject(@PathVariable Long projectId) {
        return ResponseEntity.ok(matchService.likeProject(projectId));
    }

    @GetMapping
    public ResponseEntity<List<Match>> getMatches() {
        return ResponseEntity.ok(matchService.getMatchesForUser());
    }
}
