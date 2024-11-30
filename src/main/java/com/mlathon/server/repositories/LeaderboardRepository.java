package com.mlathon.server.repositories;

import com.mlathon.server.entities.Contest;
import com.mlathon.server.entities.LeaderboardEntry;
import com.mlathon.server.entities.Submission;
import com.mlathon.server.payload.ContestInfoDto;
import com.mlathon.server.payload.UserInfoDto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaderboardRepository extends JpaRepository<LeaderboardEntry, Integer> {

    LeaderboardEntry findByUser(UserInfoDto userInfoDto);

    LeaderboardEntry findByUserAndContest(UserInfoDto userInfoDto, Contest contest);
}
