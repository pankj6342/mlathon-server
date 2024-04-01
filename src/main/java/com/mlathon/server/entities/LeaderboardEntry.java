//package com.vivek.notes.entities;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//@Entity
//@NoArgsConstructor
//@AllArgsConstructor
//@Getter
//@Setter
//@IdClass(LeaderboardEntryId.class)
//public class LeaderboardEntry {
//
//    @Id
//    @ManyToOne
//    @JoinColumn(name = "contest_id", nullable = false) // Foreign key should match column name
//    private Contest contest;
//
//    @Id
//    @ManyToOne
//    @JoinColumn(name = "participant_id", nullable = false) // Foreign key should match column name
//    private User participant;
//
//    private Integer rank;
//    private Integer score;
//}
//
