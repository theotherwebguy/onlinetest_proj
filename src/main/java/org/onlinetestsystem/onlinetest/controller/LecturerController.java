package org.onlinetestsystem.onlinetest.controller;

import org.onlinetestsystem.onlinetest.entity.Question;
import org.onlinetestsystem.onlinetest.entity.Test;
import org.onlinetestsystem.onlinetest.entity.Users;
import org.onlinetestsystem.onlinetest.service.TestService;
import org.onlinetestsystem.onlinetest.service.QuestionService;
import org.onlinetestsystem.onlinetest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/lecturer")
public class LecturerController {

    @Autowired
    private TestService testService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserService userService;

    // View lecturer dashboard
    @GetMapping("/dashboard")
    public String lecturerDashboard(Model model) {
        Users lecturer = userService.getCurrentLecturer();  // Get current lecturer (session is handled inside service)
        model.addAttribute("tests", testService.getTestsByLecturer(lecturer));  // Show only the tests for the lecturer
        return "lecturer/dashboard";
    }

    // Create test form
    @GetMapping("/create_test")
    public String createTestForm(Model model) {
        model.addAttribute("test", new Test());
        return "lecturer/create_test";
    }

    // Process test creation
    @PostMapping("/create_test")
    public String createTest(@ModelAttribute Test test) {
        Users lecturer = userService.getCurrentLecturer();  // Get lecturer from session
        test.setLecturer(lecturer);  // Associate test with the logged-in lecturer
        testService.createTest(test);
        return "redirect:/lecturer/dashboard";
    }

    // Add questions to a test
    @GetMapping("/add_questions/{testId}")
    public String addQuestionsForm(@PathVariable Long testId, Model model) {
        model.addAttribute("question", new Question());
        model.addAttribute("testId", testId);
        return "lecturer/add_question";
    }

    // Process adding questions to a test
    @PostMapping("/add_questions/{testId}")
    public String addQuestions(@PathVariable Long testId, @ModelAttribute Question question) {
        questionService.addQuestionToTest(testId, question);
        return "redirect:/lecturer/dashboard";
    }

    // Lecturer - View All Tests
    @GetMapping("/tests")
    public String viewLecturerTests(Model model) {
        Users lecturer = userService.getCurrentLecturer();  // Get current lecturer from session
        List<Test> tests = testService.getTestsByLecturer(lecturer);  // Fetch tests for the lecturer
        model.addAttribute("tests", tests);
        return "lecturer/tests";
    }
}
