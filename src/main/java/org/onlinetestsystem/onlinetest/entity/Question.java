package org.onlinetestsystem.onlinetest.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String questionText;

    @ElementCollection
    private List<String> answerOptions;

    private int correctAnswerIndex;

    @ManyToOne
    @JoinColumn(name = "test_id")
    private Test test;
}
