package com.vivek.notes.services;
import com.vivek.notes.entities.Contest;
import com.vivek.notes.repositories.ContestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Service
public class ContestService {

    @Autowired
    private ContestRepository contestRepository;

    public Contest createContest(Contest contest) {
        return contestRepository.save(contest);
    }

    public Optional<Contest> getContestById(int contestId) {
        return contestRepository.findById(contestId);
    }

    public List<Contest> getAllContests() {
        return contestRepository.findAll();
    }

    public List<Contest> getActiveContests() {
        // Implement logic to filter active contests based on current date
        // You can use LocalDateTime.now() to get the current date and time
        return contestRepository.findByEndDateAfter(LocalDateTime.now());
    }

    public Contest updateContest(Contest contest) {
        Optional<Contest> existingContest = getContestById(contest.getContestId());
        if (existingContest.isPresent()) {
            // Update existing contest details (excluding ID)
            existingContest.get().setTitle(contest.getTitle());
            existingContest.get().setDescription(contest.getDescription());
            existingContest.get().setStartDate(contest.getStartDate());
            existingContest.get().setEndDate(contest.getEndDate());
            return contestRepository.save(existingContest.get());
        } else {
            throw new RuntimeException("Contest not found with ID: " + contest.getContestId());
        }
    }

    public void deleteContest(int contestId) {
        Optional<Contest> contest = getContestById(contestId);
        if (contest.isPresent()) {
            contestRepository.delete(contest.get());
        } else {
            throw new RuntimeException("Contest not found with ID: " + contestId);
        }
    }
}