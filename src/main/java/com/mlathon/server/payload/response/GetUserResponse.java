package com.mlathon.server.payload.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mlathon.server.entities.Contest;
import com.mlathon.server.enums.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetUserResponse {
    private Integer id;
    private String name;
    private UserRole role;
    private String email;
    private Set<Contest> createdContests = new HashSet<>();
    private Set<Contest> enrolledContests = new HashSet<>();
}
