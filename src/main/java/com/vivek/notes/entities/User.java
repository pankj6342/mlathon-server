package com.vivek.notes.entities;

import java.util.ArrayList;
import java.util.List;
import com.vivek.notes.enums.UserType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @Column(nullable=false, unique = true)
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private UserType role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Notes> notes = new ArrayList<>();

    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL) // Relationship with Contest
    private List<Contest> contests = new ArrayList<>();

//    @OneToMany(mappedBy = "participant", cascade = CascadeType.ALL) // Relationship with LeaderboardEntry
//    private List<LeaderboardEntry> leaderboardEntries = new ArrayList<>();
}

