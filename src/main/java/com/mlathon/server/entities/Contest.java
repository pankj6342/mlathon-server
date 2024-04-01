package com.mlathon.server.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
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
    private String description;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "admin_id") // Foreign key should match column name
    private User admin;

    @ManyToMany(mappedBy = "enrolledContests", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIgnore
    private Set<User> enrolledUsers = new HashSet<>();
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private String trainingDataPath;
    private String testingDataPath;
}

