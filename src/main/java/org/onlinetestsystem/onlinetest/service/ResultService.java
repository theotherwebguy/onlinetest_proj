package org.onlinetestsystem.onlinetest.service;

import org.onlinetestsystem.onlinetest.entity.TestResult;
import org.onlinetestsystem.onlinetest.repository.TestResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResultService {

    @Autowired
    private TestResultRepository testResultRepository;

    public List<TestResult> getResultsForStudent(Long studentId) {
        return testResultRepository.findByStudentId(studentId);
    }
}

