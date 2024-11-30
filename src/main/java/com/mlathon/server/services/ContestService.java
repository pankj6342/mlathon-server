package com.mlathon.server.services;
import com.mlathon.server.entities.*;
import com.mlathon.server.enums.QuestionType;
import com.mlathon.server.payload.UserInfoDto;
import com.mlathon.server.payload.response.CommentDto;
import com.mlathon.server.payload.response.SubmissionResponse;
import com.mlathon.server.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ContestService {

    @Autowired
    private ContestRepository contestRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired private UserService userService;
    @Autowired private AttachmentService attachmentService;
    @Autowired private UserRepository userRepository;
    @Autowired private SubmissionRepository submissionRepository;
    @Autowired private LeaderboardRepository leaderboardRepository;

    public Contest createContest(Contest contest) {
        return contestRepository.save(contest);
    }

    public Optional<Contest> getContestById(int contestId) {
        return contestRepository.findById(contestId);
    }

    public List<Contest> getAllContests() {
        return contestRepository.findAll();
    }

    public List<Contest> getActiveContests() {
        // Implement logic to filter active contests based on current date
        // You can use LocalDateTime.now() to get the current date and time
        return contestRepository.findByEndDateAfter(LocalDateTime.now());
    }

    public Contest updateContest(Contest contest) {
        Optional<Contest> existingContest = getContestById(contest.getContestId());
        if (existingContest.isPresent()) {
            // Update existing contest details (excluding ID)
            existingContest.get().setTitle(contest.getTitle());
            existingContest.get().setDescription(contest.getDescription());
            existingContest.get().setStartDate(contest.getStartDate());
            existingContest.get().setEndDate(contest.getEndDate());
            existingContest.get().setComments(contest.getComments());
            existingContest.get().setOverview(contest.getOverview());
            existingContest.get().setDatasetDescription(contest.getDatasetDescription());
            existingContest.get().setSubtitle(contest.getSubtitle());
            return contestRepository.save(existingContest.get());
        } else {
            throw new RuntimeException("Contest not found with ID: " + contest.getContestId());
        }
    }

    public void deleteContest(int contestId) {
        Optional<Contest> contest = getContestById(contestId);
        if (contest.isPresent()) {
            contestRepository.delete(contest.get());
        } else {
            throw new RuntimeException("Contest not found with ID: " + contestId);
        }
    }

    public void enrollUserInContest(Integer userId, Integer contestId) throws Exception {
        User user = userRepository.findById(userId).orElseThrow(() -> new Exception("User not found"));
        Contest contest = contestRepository.findById(contestId).orElseThrow(() -> new Exception("Contest not found"));

        user.getEnrolledContests().add(contest);
        contest.getEnrolledUsers().add(user);

        userRepository.save(user); // Saving the user persists the relationship
    }

    public Submission evaluateAndSaveSubmission(int contestId, int userId, MultipartFile submissionFile, LocalDateTime submissionDate) throws Exception {
        Contest contest = getContestById(contestId).orElseThrow();
        //check submission limit
        Set<Submission> submissions = contest.getSubmissions()!=null ? contest.getSubmissions() : new HashSet<>();
        int submissionCount = 0;
        for(Submission s: submissions){
            if(s.getUser().getId()==userId) submissionCount++;
            if(submissionCount==5) return null;
        }

        //Retrieve Solution File
        String solutionFileUrl = contest.getSolutionFilePath();
        String fileId = solutionFileUrl.split("/download/")[1];
        byte[] solutionByteData = attachmentService.getAttachment(fileId).getCsvData();
        List<List<String>> solutionCsvData = attachmentService.convertByteArrayToCsvData(solutionByteData);
        String submissionFileUrl = attachmentService.uploadFile(submissionFile).getDownloadUrl();
        List<List<String>> submissionCsvData = attachmentService.convertByteArrayToCsvData(submissionFile.getBytes());

        //Calculate Score
        double score =  contest.getQuestionType().equals(QuestionType.CLASSIFICATION) ? calculateF1Score(solutionCsvData, submissionCsvData): calculateRMSEScore(solutionCsvData, submissionCsvData);

        //Save the submission file to db
        User user = userService.getUser(userId);
        UserInfoDto userInfoDto = new UserInfoDto(user.getId(), user.getName(), user.getEmail());
        String filePath = attachmentService.uploadFile(submissionFile).getDownloadUrl();

        //Create new submission entry
        Submission submission = new Submission();
        submission.setContest(contest);
        submission.setScore(score);
        submission.setUser(userInfoDto);
        submission.setFilePath(filePath);
        submission.setSubmissionDate(submissionDate);
        submissionRepository.save(submission);
        submissions.add(submission);
        contest.setSubmissions(submissions);

        //Create new leaderboard entry
        LeaderboardEntry leaderboardEntry = leaderboardRepository.findByUserAndContest(userInfoDto, contest);
        if(leaderboardEntry==null){
            //1st submission of the user
            leaderboardEntry = new LeaderboardEntry();
            leaderboardEntry.setContest(contest);
            leaderboardEntry.setUser(userInfoDto);
            leaderboardEntry.setBestScore(score);
            leaderboardEntry.setLastSubmissionDate(submissionDate);
            leaderboardRepository.save(leaderboardEntry);
            Set<LeaderboardEntry> leaderboardEntries = contest.getLeaderboardEntries() != null ? contest.getLeaderboardEntries() : new HashSet<>();
            leaderboardEntries.add(leaderboardEntry);
            contest.setLeaderboardEntries(leaderboardEntries);
        }
        else if(score>leaderboardEntry.getBestScore()){
            //subsequent submissions
            Set<LeaderboardEntry> leaderboardEntries = contest.getLeaderboardEntries() != null ? contest.getLeaderboardEntries() : new HashSet<>();
            leaderboardEntries.remove(leaderboardEntry);
            leaderboardEntry.setBestScore(score);
            leaderboardRepository.save(leaderboardEntry);
            leaderboardEntries.add(leaderboardEntry);
            contest.setLeaderboardEntries(leaderboardEntries);
        }
        contestRepository.save(contest);
        return submission;
    }

    private double calculateRMSEScore(List<List<String>> solutionCsvData, List<List<String>> submissionCsvData) {
        HashMap<String, String> solutionCsvMap = new HashMap<>();
        for (List<String> row : solutionCsvData) {
            solutionCsvMap.put(row.get(0), row.get(1));
        }
        double square_sum = 0;
        for(List<String> row: submissionCsvData){
            double actual = Double.parseDouble(solutionCsvMap.getOrDefault(row.get(0), "-1"));
            double predicted = Double.parseDouble(row.get(1));
            square_sum += (actual-predicted)*(actual-predicted);
        }
        return Math.sqrt(square_sum/solutionCsvMap.size());
    }

    private double calculateF1Score(List<List<String>> solutionCsvData, List<List<String>> submissionCsvData) {
        HashMap<String, String> solutionCsvMap = new HashMap<>();
        for (List<String> row : solutionCsvData) {
            solutionCsvMap.put(row.get(0), row.get(1));
        }
        
        double tp = 0, tn=0, fp=0, fn = 0;
        for (List<String> row : submissionCsvData) {
            String correctValue = solutionCsvMap.getOrDefault(row.get(0), "#");
            String submittedValue = row.get(1);

            if(correctValue.equalsIgnoreCase("0") && submittedValue.equalsIgnoreCase("0"))
                tn++;
            else if(correctValue.equalsIgnoreCase("0") && submittedValue.equalsIgnoreCase("1"))
                fp++;
            else if(correctValue.equalsIgnoreCase("1") && submittedValue.equalsIgnoreCase("0"))
                fn++;
            else if(correctValue.equalsIgnoreCase("1") && submittedValue.equalsIgnoreCase("1"))
                tp++;
        }
        double sc = ((2 * tp) /(2 * tp + fp + fn));
        return (double) Math.floor(sc * 100) / 100;
    }

    public CommentDto addComment(int contestId, Comment comment) throws Exception {
        Contest contest = null;
        Optional<Contest> contestOptional = getContestById(contestId);
        if(contestOptional.isEmpty()) return null;
        contest = contestOptional.get();

        User user = userService.getUser(comment.getUser().getId());
        UserInfoDto userInfoDto = new UserInfoDto(user.getId(), user.getName(), user.getEmail());
        comment.setUser(userInfoDto);

        Set<Comment> comments = contest.getComments()!=null ? contest.getComments() : new HashSet<>();
        comments.add(comment);
        contest.setComments(comments);
        contestRepository.save(contest);
        comment.setContest(contest);
        Comment addedComment = commentRepository.save(comment);
        return new CommentDto(addedComment.getId(), addedComment.getDescription(), addedComment.getDate(), contestId, addedComment.getUser());
    }
}