package com.mlathon.server.payload.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mlathon.server.entities.Comment;
import com.mlathon.server.entities.LeaderboardEntry;
import com.mlathon.server.entities.Submission;
import com.mlathon.server.entities.User;
import com.mlathon.server.enums.QuestionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetContestResponse {
    private Integer contestId;
    private String title;
    private String subtitle;
    private String description;
    private String overview;
    private String datasetDescription;
    private Integer adminId;
    private Integer submissionLimit;
    private Map<Integer, Integer> submissionCount = new HashMap<>();
    private Set<User> enrolledUsers = new HashSet<>();
    private Set<Comment> comments = new HashSet<>();
    private List<Submission> submissions = new ArrayList<>();
    private List<LeaderboardEntry> leaderboardEntries = new ArrayList<>();
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private QuestionType questionType;
    private String trainingDataPath;
    private String testingDataPath;
    private String solutionFilePath;
    private String sampleSubmissionFilePath;
}
