package com.mlathon.server.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mlathon.server.enums.QuestionType;
import com.mlathon.server.enums.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Contest implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer contestId;

    private String title;
    @Column(columnDefinition = "TEXT")
    private String subtitle;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String overview;
    @Column(columnDefinition = "TEXT")
    private String datasetDescription;

    @Enumerated(EnumType.STRING)
    QuestionType questionType;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "admin_id") // Foreign key should match column name
    private User admin;

    @ManyToMany(mappedBy = "enrolledContests", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIgnore
    private Set<User> enrolledUsers = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "contest", cascade = CascadeType.ALL)
    private Set<Comment> comments = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "contest", cascade = CascadeType.ALL)
    private Set<Submission> submissions = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "contest", cascade = CascadeType.ALL)
    private Set<LeaderboardEntry> leaderboardEntries = new HashSet<>();

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private String trainingDataPath;
    private String testingDataPath;
    private String solutionFilePath;
    private String sampleSubmissionFilePath;
}

