package org.onlinetestsystem.onlinetest.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @OneToMany(mappedBy = "test", cascade = CascadeType.ALL)
    private List<Question> questions;

    @ManyToOne
    @JoinColumn(name = "lecturer_id")  // This column will hold the ID of the lecturer who created the test
    private Users lecturer;

    private String startDate;
    private String endDate;
}
