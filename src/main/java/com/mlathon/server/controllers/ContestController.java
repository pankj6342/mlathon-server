package com.mlathon.server.controllers;

import com.mlathon.server.entities.*;
import com.mlathon.server.payload.UserDto;
import com.mlathon.server.payload.UserInfoDto;
import com.mlathon.server.payload.response.CommentDto;
import com.mlathon.server.payload.response.GetContestResponse;
import com.mlathon.server.services.AttachmentService;
import com.mlathon.server.services.ContestService;
import com.mlathon.server.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/contests")
@CrossOrigin
public class ContestController {

    @Autowired
    private ContestService contestService;
    @Autowired
    private AttachmentService attachmentService;
    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<Contest> createContest(@ModelAttribute Contest contest,
                                                 @RequestPart(name = "trainingData") MultipartFile trainingDataFile,  // Use @RequestPart for file uploads
                                                 @RequestPart(name = "testingData") MultipartFile testingDataFile,
                                                 @RequestPart(name = "solutionFile") MultipartFile solutionFile,
                                                 @RequestPart(name = "sampleSubmissionFile") MultipartFile sampleSubmissionFile,
                                                 @RequestParam(name = "adminId") Integer adminId) throws IOException {
        String trainingDataPath = null;
        String testingDataPath = null;
        String solutionFilePath = null;
        String sampleSubmissionFilePath = null;
        User admin = null;
        try {
            if (trainingDataFile != null && !trainingDataFile.isEmpty()) {
                trainingDataPath = attachmentService.uploadFile(trainingDataFile).getDownloadUrl(); // Call method to upload training data
            }
            if (testingDataFile != null && !testingDataFile.isEmpty()) {
                testingDataPath = attachmentService.uploadFile(testingDataFile).getDownloadUrl(); // Call method to upload testing data
            }
            if (solutionFile != null && !solutionFile.isEmpty()) {
                solutionFilePath = attachmentService.uploadFile(solutionFile).getDownloadUrl();
            }
            if (sampleSubmissionFile != null && !sampleSubmissionFile.isEmpty()) {
                sampleSubmissionFilePath = attachmentService.uploadFile(sampleSubmissionFile).getDownloadUrl();
            }
            if (adminId != null) {
                admin = userService.getUser(adminId);
            }
            contest.setTrainingDataPath(trainingDataPath);
            contest.setTestingDataPath(testingDataPath);
            contest.setSolutionFilePath(solutionFilePath);
            contest.setSampleSubmissionFilePath(sampleSubmissionFilePath);
            contest.setAdmin(admin);

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
    public ResponseEntity<GetContestResponse> getContestById(@PathVariable int contestId) {
        Optional<Contest> contestOptional = contestService.getContestById(contestId);
        if (contestOptional.isPresent()) {
            Contest contest = contestOptional.get();
            Set<LeaderboardEntry> leaderboardEntriesSet = contest.getLeaderboardEntries();
            List<LeaderboardEntry> leaderboardEntries = new ArrayList<LeaderboardEntry>(leaderboardEntriesSet);
            List<Submission> submissions = new ArrayList<Submission>(contest.getSubmissions());
            leaderboardEntries.sort((o1, o2)->Double.compare(o2.getBestScore(), o1.getBestScore()));
            submissions.sort((o1, o2) -> o2.getSubmissionDate().compareTo(o1.getSubmissionDate()));
            Map<Integer, Integer> submissionCount = new HashMap<>();
            for(Submission s: submissions){
                submissionCount.put(s.getUser().getId(), 1 + submissionCount.getOrDefault(s.getUser().getId(), 0));
            }
            GetContestResponse response = new GetContestResponse(contest.getContestId(), contest.getTitle(), contest.getSubtitle(), contest.getDescription(), contest.getOverview(), contest.getDatasetDescription(), contest.getAdmin().getId(), 5, submissionCount, contest.getEnrolledUsers(), contest.getComments(), submissions, leaderboardEntries, contest.getStartDate(), contest.getEndDate(), contest.getQuestionType(), contest.getTrainingDataPath(), contest.getTestingDataPath(), contest.getSolutionFilePath(), contest.getSampleSubmissionFilePath());
            return new ResponseEntity<>(response, HttpStatus.OK);
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

    @PostMapping("/submit/{contestId}")
    public ResponseEntity<Submission> submitSolution(@PathVariable int contestId,
                                               @RequestPart(name = "submissionFile") MultipartFile submissionFile,
                                               @RequestParam(name = "submissionDate")LocalDateTime submissionDate,
                                               @RequestParam(name = "userId") int userId) throws Exception {
        Submission submission = contestService.evaluateAndSaveSubmission(contestId, userId, submissionFile, submissionDate);
        return new ResponseEntity<Submission>(submission, HttpStatus.OK);
    }

    @PostMapping("/add-comment/{contestId}")
    public ResponseEntity<CommentDto> addComment(@RequestBody Comment comment,
                                                 @PathVariable int contestId) {
        try {
            CommentDto addedComment = contestService.addComment(contestId, comment);
            return new ResponseEntity<>(addedComment, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
