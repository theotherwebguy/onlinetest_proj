package org.onlinetestsystem.onlinetest.service;

import org.onlinetestsystem.onlinetest.entity.Test;
import org.onlinetestsystem.onlinetest.entity.Users;
import org.onlinetestsystem.onlinetest.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TestService {

    @Autowired
    private TestRepository testRepository;

    public Test createTest(Test test) {
        return testRepository.save(test);
    }

    public List<Test> getAllTests() {
        return testRepository.findAll();
    }

    // Fetch tests by lecturer
    public List<Test> getTestsByLecturer(Users lecturer) {
        return testRepository.findByLecturer(lecturer);
    }
}
