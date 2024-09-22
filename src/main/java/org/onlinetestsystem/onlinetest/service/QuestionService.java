package org.onlinetestsystem.onlinetest.service;

import org.onlinetestsystem.onlinetest.entity.Question;
import org.onlinetestsystem.onlinetest.entity.Test;
import org.onlinetestsystem.onlinetest.repository.QuestionRepository;
import org.onlinetestsystem.onlinetest.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private TestRepository testRepository;

    public void addQuestionToTest(Long testId, Question question) {
        Optional<Test> test = testRepository.findById(testId);
        if (test.isPresent()) {
            // Associate the question with the test
            question.setTest(test.get());
            questionRepository.save(question);  // Save the question with the test
        } else {
            throw new RuntimeException("Test not found with ID: " + testId);
        }
    }
}
