//package com.vivek.notes.services;
//
//import com.vivek.notes.entities.Contest;
//import com.vivek.notes.entities.LeaderboardEntry;
//import com.vivek.notes.entities.Submission;
//import com.vivek.notes.repositories.LeaderboardRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//public class LeaderboardService {
//
//    @Autowired
//    private LeaderboardRepository leaderboardRepository;
//
//    public List<LeaderboardEntry> getLeaderboardForContest(Contest contest) {
//        return leaderboardRepository.findByContest(contest);
//    }
//
//    public void updateLeaderboard(Contest contest) {
//        // Implement logic to calculate ranks based on submission scores (potentially in a separate service)
//        List<Submission> submissions = new ArrayList<>(); // fetch submissions for the contest
//        for (Submission submission : submissions) {
//            LeaderboardEntry entry = new LeaderboardEntry();
//            entry.setContest(contest);
//            entry.setParticipant(submission.getParticipant());
//            //pending
//        }
//    }
//}
//
