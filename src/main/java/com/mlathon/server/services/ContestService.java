package com.mlathon.server.services;
import com.mlathon.server.entities.Contest;
import com.mlathon.server.repositories.ContestRepository;
import com.mlathon.server.repositories.UserRepository;
import com.mlathon.server.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Service
public class ContestService {

    @Autowired
    private ContestRepository contestRepository;
    @Autowired private UserService userService;
    @Autowired private UserRepository userRepository;

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

    public void enrollUserInContest(Integer userId, Integer contestId) throws Exception {
        User user = userRepository.findById(userId).orElseThrow(() -> new Exception("User not found"));
        Contest contest = contestRepository.findById(contestId).orElseThrow(() -> new Exception("Contest not found"));

        user.getEnrolledContests().add(contest);
        contest.getEnrolledUsers().add(user);

        userRepository.save(user); // Saving the user persists the relationship
    }
}