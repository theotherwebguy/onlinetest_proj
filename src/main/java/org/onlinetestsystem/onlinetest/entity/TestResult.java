package org.onlinetestsystem.onlinetest.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Map;

@Data
@Entity
public class TestResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Users student;

    @ManyToOne
    @JoinColumn(name = "test_id")
    private Test test;

    @ElementCollection
    private Map<Long, Integer> studentGrades ;

    private int score;
    private boolean passed;  // Indicates if the student passed the test
}
