package com.mlathon.server.payload.response;

import com.mlathon.server.entities.LeaderboardEntry;
import com.mlathon.server.entities.Submission;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@AllArgsConstructor
@Getter
@Setter
public class SubmissionResponse {
    private Set<Submission> submissions;
    private Set<LeaderboardEntry> leaderboardEntries;
}
