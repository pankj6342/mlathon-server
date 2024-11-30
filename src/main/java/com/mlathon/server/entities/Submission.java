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
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer submissionId;
    private UserInfoDto user;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "contest_id")
    private Contest contest;
    private LocalDateTime submissionDate;
    private String filePath; // Path to the uploaded output file
    private double score; // Initially null, updated later
}
