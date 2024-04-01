package com.mlathon.server.controllers;

import com.mlathon.server.entities.Contest;
import com.mlathon.server.services.AttachmentService;
import com.mlathon.server.services.ContestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/contests")
@CrossOrigin
public class ContestController {

    @Autowired
    private ContestService contestService;
    @Autowired
    private AttachmentService attachmentService;

    @PostMapping("/create")
    public ResponseEntity<Contest> createContest(@ModelAttribute  Contest contest,
                                                 @RequestPart(name = "trainingData") MultipartFile trainingDataFile,  // Use @RequestPart for file uploads
                                                 @RequestPart(name = "testingData") MultipartFile testingDataFile) throws IOException {
        String trainingDataPath = null;
        String testingDataPath = null;

        try {
            if (trainingDataFile != null && !trainingDataFile.isEmpty()) {
                trainingDataPath = attachmentService.uploadFile(trainingDataFile).getDownloadUrl(); // Call method to upload training data
            }
            if (testingDataFile != null && !testingDataFile.isEmpty()) {
                testingDataPath = attachmentService.uploadFile(testingDataFile).getDownloadUrl(); // Call method to upload testing data
            }

            contest.setTrainingDataPath(trainingDataPath);
            contest.setTestingDataPath(testingDataPath);

            Contest savedContest = contestService.createContest(contest);
            return new ResponseEntity<>(savedContest, HttpStatus.CREATED);
        } catch (IOException e) {
            // Handle potential IO exceptions during upload
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{contestId}")
    public ResponseEntity<Contest> getContestById(@PathVariable int contestId) {
        Optional<Contest> contest = contestService.getContestById(contestId);
        if (contest.isPresent()) {
            return new ResponseEntity<>(contest.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/")
    public ResponseEntity<List<Contest>> getAllContests() {
        List<Contest> contests = contestService.getAllContests();
        return new ResponseEntity<>(contests, HttpStatus.OK);
    }

    @GetMapping("/active")
    public ResponseEntity<List<Contest>> getActiveContests() {
        List<Contest> activeContests = contestService.getActiveContests();
        return new ResponseEntity<>(activeContests, HttpStatus.OK);
    }

    @PutMapping("/{contestId}")
    public ResponseEntity<Contest> updateContest(@PathVariable int contestId, @Valid @RequestBody Contest contest) {
        Optional<Contest> existingContest = contestService.getContestById(contestId);
        if (existingContest.isPresent()) {
            if (contest.getTitle() != null) existingContest.get().setTitle(contest.getTitle());
            if (contest.getDescription() != null) existingContest.get().setDescription(contest.getDescription());
            if (contest.getStartDate() != null) existingContest.get().setStartDate(contest.getStartDate());
            if (contest.getEndDate() != null) existingContest.get().setEndDate(contest.getEndDate());
            Contest updatedContest = contestService.updateContest(existingContest.get());
            return new ResponseEntity<>(updatedContest, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/{contestId}")
    public ResponseEntity<Void> deleteContest(@PathVariable int contestId) {
        contestService.deleteContest(contestId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/enroll/{contestId}")
    public ResponseEntity<Void> enrollUserInContest(@PathVariable int contestId, @RequestBody int userId) {
        try {
            contestService.enrollUserInContest(userId, contestId);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Handle specific exceptions with appropriate status codes
        }
    }

}
