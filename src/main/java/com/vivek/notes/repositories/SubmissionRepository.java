package com.vivek.notes.repositories;

import com.vivek.notes.entities.Submission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubmissionRepository extends JpaRepository<Submission, Integer> {

}
