package org.onlinetestsystem.onlinetest.repository;

import org.onlinetestsystem.onlinetest.entity.Test;
import org.onlinetestsystem.onlinetest.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestRepository extends JpaRepository<Test, Long> {
    // Fetch tests created by a specific lecturer
    List<Test> findByLecturer(Users lecturer);

    List<Test> findByLecturerId(Long lecturerId);

}
