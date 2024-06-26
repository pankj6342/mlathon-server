package com.mlathon.server.repositories;

import com.mlathon.server.entities.Contest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ContestRepository extends JpaRepository<Contest, Integer> {
    @Query("SELECT c FROM Contest c WHERE c.endDate > :now")
    List<Contest> findByEndDateAfter(LocalDateTime now);
}
