package com.vivek.notes.controllers;

import com.vivek.notes.entities.Contest;
import com.vivek.notes.services.ContestService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/contests")
public class ContestController {

    @Autowired
    private ContestService contestService;

    @PostMapping("/create")
    public ResponseEntity<Contest> createContest(@Valid @RequestBody Contest contest) {
        Contest createdContest = contestService.createContest(contest);
        return new ResponseEntity<>(createdContest, HttpStatus.CREATED);
    }

    @GetMapping("/{contestId}")
    public ResponseEntity<Contest> getContestById(@PathVariable int contestId) {
        Optional<Contest> contest = contestService.getContestById(contestId);
        if (contest.isPresent()) {
            return new ResponseEntity<>(contest.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/")
    public ResponseEntity<List<Contest>> getAllContests() {
        List<Contest> contests = contestService.getAllContests();
        return new ResponseEntity<>(contests, HttpStatus.OK);
    }

    @GetMapping("/active")
    public ResponseEntity<List<Contest>> getActiveContests() {
        List<Contest> activeContests = contestService.getActiveContests();
        return new ResponseEntity<>(activeContests, HttpStatus.OK);
    }

    @PutMapping("/{contestId}")
    public ResponseEntity<Contest> updateContest(@PathVariable int contestId, @Valid @RequestBody Contest contest) {
        Optional<Contest> existingContest = contestService.getContestById(contestId);
        if (existingContest.isPresent()) {
            if (contest.getTitle() != null) existingContest.get().setTitle(contest.getTitle());
            if (contest.getDescription() != null) existingContest.get().setDescription(contest.getDescription());
            if (contest.getStartDate() != null) existingContest.get().setStartDate(contest.getStartDate());
            if (contest.getEndDate() != null) existingContest.get().setEndDate(contest.getEndDate());
            Contest updatedContest = contestService.updateContest(existingContest.get());
            return new ResponseEntity<>(updatedContest, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/{contestId}")
    public ResponseEntity<Void> deleteContest(@PathVariable int contestId) {
        contestService.deleteContest(contestId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
