package com.mlathon.server.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mlathon.server.payload.UserInfoDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LeaderboardEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private UserInfoDto user;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "contest_id")
    private Contest contest;
    private LocalDateTime lastSubmissionDate;
    private double bestScore;
}