package com.vivek.notes.entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Contest {
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

//    @OneToMany(mappedBy = "contest", cascade = CascadeType.ALL) // Relationship with LeaderboardEntry
//    private List<LeaderboardEntry> leaderboardEntries = new ArrayList<>();

    private LocalDateTime startDate;
    private LocalDateTime endDate;
}

