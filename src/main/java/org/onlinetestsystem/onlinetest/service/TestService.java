package org.onlinetestsystem.onlinetest.service;

import org.onlinetestsystem.onlinetest.entity.*;
import org.onlinetestsystem.onlinetest.repository.StudentAnswerRepository;
import org.onlinetestsystem.onlinetest.repository.TestRepository;
import org.onlinetestsystem.onlinetest.repository.TestResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;

@Service
public class TestService {

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private StudentAnswerRepository studentAnswerRepository;

    @Autowired
    private TestResultRepository testResultRepository;

    private static final Logger logger = LoggerFactory.getLogger(TestService.class);

    // Create a new test
    public Test createTest(Test test) {
        logger.info("Creating a new test: {}", test);
        return testRepository.save(test);
    }

    // Get all tests
    public List<Test> getAllTests() {
        logger.info("Fetching all tests.");
        return testRepository.findAll();
    }

    // Fetch tests by lecturer
    public List<Test> getTestsByLecturer(Users lecturer) {
        logger.info("Fetching tests for lecturer: {}", lecturer.getFullName());
        return testRepository.findByLecturer(lecturer);
    }

    // Get available tests
    public List<Test> getAvailableTests() {
        logger.info("Fetching available tests.");
        return testRepository.findAll();  // Can be filtered for availability if needed
    }

    // Get test by ID
    public Test getTestById(Long testId) {
        logger.info("Fetching test with ID: {}", testId);
        return testRepository.findById(testId).orElseThrow(() -> {
            logger.error("Test not found with ID: {}", testId);
            return new RuntimeException("Test not found");
        });
    }

    // Submit student's test answers
    public void submitTestAnswers(StudentAnswer studentAnswer) {
        logger.info("Submitting answers for student: {} on test: {}", studentAnswer.getStudent().getFullName());
        studentAnswerRepository.save(studentAnswer);
        logger.info("Successfully saved answers for student: {}", studentAnswer.getStudent().getFullName());
    }

    // Calculate the test result
    public TestResult calculateTestResult(StudentAnswer studentAnswer) {
        logger.info("Calculating test result for student: {}", studentAnswer.getStudent().getFullName());
        Test test = studentAnswer.getTest();
        List<Question> questions = test.getQuestions();
        List<Integer> selectedAnswers = studentAnswer.getSelectedAnswers();

        // Check if questions or selected answers are empty
        if (questions.isEmpty()) {
            logger.error("Test questions are empty.");
            throw new IllegalArgumentException("Test questions are empty.");
        }

        if (selectedAnswers.isEmpty()) {
            logger.error("Student answers are empty.");
            throw new IllegalArgumentException("Student answers are empty.");
        }

        // Ensure the number of questions matches the number of selected answers
        if (questions.size() != selectedAnswers.size()) {
            logger.error("Number of questions does not match the number of selected answers.");
            throw new IllegalArgumentException("Number of questions does not match the number of selected answers.");
        }

        int score = 0;

        // Calculate the score
        for (int i = 0; i < questions.size(); i++) {
            logger.debug("Question {}: Correct answer index: {}, Student selected: {}", i, questions.get(i).getCorrectAnswerIndex(), selectedAnswers.get(i));
            if (questions.get(i).getCorrectAnswerIndex() == selectedAnswers.get(i)) {
                score++;
                logger.debug("Question {} is correct.", i);
            } else {
                logger.debug("Question {} is incorrect.", i);
            }
        }

        // Create and return the TestResult object
        TestResult result = new TestResult();
        result.setStudent(studentAnswer.getStudent());
        result.setTest(test);
        result.setScore(score);
        result.setPassed(score >= (questions.size() * 0.5));  // Assuming 50% pass mark

        logger.info("Test result calculated: Score = {}, Passed = {}", score, result.isPassed());

        logger.info("Test result successfully created for student: {}", studentAnswer.getStudent().getFullName());

        return result;
    }

    // Save the TestResult after it is calculated
    public void saveTestResult(TestResult result) {
        logger.info("Saving test result for student: {}", result.getStudent().getFullName());
        testResultRepository.save(result);  // This saves the result to the database
        logger.info("Test result saved successfully for student: {}", result.getStudent().getFullName());
    }

    // Get results for a student
    public List<TestResult> getResultsForStudent(Long studentId) {
        logger.info("Fetching test results for student ID: {}", studentId);
        return testResultRepository.findByStudentId(studentId);
    }
}
