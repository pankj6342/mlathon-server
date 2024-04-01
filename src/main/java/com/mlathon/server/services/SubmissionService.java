package com.mlathon.server.services;

import com.mlathon.server.repositories.SubmissionRepository;
import com.mlathon.server.entities.Submission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class SubmissionService {

    @Autowired
    private SubmissionRepository submissionRepository;

    public Submission saveSubmission(Submission submission) {
        return submissionRepository.save(submission);
    }

    public Optional<Submission> getSubmissionById(int submissionId) {
        return submissionRepository.findById(submissionId);
    }

    // Implement functionalities for uploading/downloading submission files based on your storage solution
    // Implement logic to trigger the scoring process (separate script or service call)
}
