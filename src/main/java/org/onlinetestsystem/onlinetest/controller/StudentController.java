package org.onlinetestsystem.onlinetest.controller;

import org.onlinetestsystem.onlinetest.entity.StudentAnswer;
import org.onlinetestsystem.onlinetest.entity.Test;
import org.onlinetestsystem.onlinetest.entity.TestResult;
import org.onlinetestsystem.onlinetest.entity.Users;
import org.onlinetestsystem.onlinetest.service.ResultService;
import org.onlinetestsystem.onlinetest.service.TestService;
import org.onlinetestsystem.onlinetest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Controller
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private TestService testService;

    @Autowired
    private UserService userService;

    @Autowired
    private ResultService resultService;

    // Initialize logger for this class
    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    // Method to display available tests to students
    @GetMapping("/tests")
    public String viewAvailableTests(Model model) {
        logger.info("Fetching available tests for student.");

        List<Test> availableTests = testService.getAvailableTests();
        model.addAttribute("tests", availableTests);

        logger.info("Available tests: {}", availableTests.size());
        return "student/tests";
    }

    // Method to display the "take test" page
    @GetMapping("/take_test/{testId}")
    public String takeTest(@PathVariable Long testId, Model model) {
        logger.info("Fetching test with ID: {}", testId);

        Test test = testService.getTestById(testId);
        model.addAttribute("test", test);

        logger.info("Test '{}' loaded with {} questions.", test.getName(), test.getQuestions().size());
        return "student/take_test";
    }

    // Method to handle test submission
// Method to handle test submission
    @PostMapping("/submit_test/{testId}")
    public String submitTest(@PathVariable Long testId, @ModelAttribute StudentAnswer studentAnswer, RedirectAttributes redirectAttributes) {
        logger.info("Processing submission for test ID: {}", testId);

        // Log the selected answers before any checks
        logger.info("Selected Answers (before filtering): {}", studentAnswer.getSelectedAnswers());

        // Check if answers are empty or contain only null values
        if (studentAnswer.getSelectedAnswers() == null || studentAnswer.getSelectedAnswers().isEmpty()) {
            logger.warn("No answers submitted for test ID: {}", testId);
            redirectAttributes.addFlashAttribute("error", "Please answer all questions before submitting.");
            return "redirect:/student/take_test/" + testId;
        }

        // Filter out null values from the selected answers list
        studentAnswer.getSelectedAnswers().removeIf(answer -> answer == null);
        logger.info("Selected Answers (after filtering): {}", studentAnswer.getSelectedAnswers());

        // Check again if the list is now empty after filtering
        if (studentAnswer.getSelectedAnswers().isEmpty()) {
            logger.warn("All answers were null for test ID: {}", testId);
            redirectAttributes.addFlashAttribute("error", "Please provide valid answers for all questions.");
            return "redirect:/student/take_test/" + testId;
        }

        // Attach test and student information
        studentAnswer.setTest(testService.getTestById(testId));
        studentAnswer.setStudent(userService.getCurrentUser());

        logger.info("Student '{}' submitted answers for test '{}'", studentAnswer.getStudent().getFullName(), studentAnswer.getTest().getName());

        // Save the student's answers
        testService.submitTestAnswers(studentAnswer);

        // Calculate and save the result
        try {

            logger.info("Calculating test result for student '{}'", studentAnswer.getStudent().getFullName());
            TestResult result = testService.calculateTestResult(studentAnswer);
            logger.info("Test result for student '{}' is saved successfully.", studentAnswer.getStudent().getFullName());
            // Save the calculated result
            testService.saveTestResult(result);

        } catch (IllegalArgumentException e) {

            logger.error("Error during result calculation for test ID: {}", testId, e);
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/student/take_test/" + testId;

        }

        logger.info("Test submitted successfully for student '{}'", studentAnswer.getStudent().getFullName());
        return "redirect:/student/results";
    }



    // Method to display test results
    @GetMapping("/results")
    public String viewResults(Model model) {
        Users student = userService.getCurrentUser();
        logger.info("Fetching results for student '{}'", student.getFullName());

        List<TestResult> results = resultService.getResultsForStudent(student.getId());
        model.addAttribute("results", results);

        logger.info("Found {} results for student '{}'", results.size(), student.getFullName());
        return "student/results";
    }

    // Method to display the student dashboard
    @GetMapping("/dashboard")
    public String viewStudentDashboard(Model model) {
        Users student = userService.getCurrentUser();
        model.addAttribute("fullName", student.getFullName());

        logger.info("Loading dashboard for student '{}'", student.getFullName());

        List<Test> availableTests = testService.getAvailableTests();
        model.addAttribute("tests", availableTests);

        logger.info("Loaded dashboard with {} available tests for student '{}'", availableTests.size(), student.getFullName());
        return "student/dashboard";
    }
}
