package com.mlathon.server.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mlathon.server.enums.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @Enumerated(EnumType.STRING)
    UserRole role;
    @Column(nullable=false, unique = true)
    private String email;
    @JsonIgnore
    private String password;

//    @JsonIgnore
    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL)
    private Set<Contest> createdContests = new HashSet<>();

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "contest_enrollment", // Join table for the relationship
            joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "contest_id"))
    private Set<Contest> enrolledContests = new HashSet<>();
}

