package com.mlathon.server.repositories;

import com.mlathon.server.entities.Submission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubmissionRepository extends JpaRepository<Submission, Integer> {

}
