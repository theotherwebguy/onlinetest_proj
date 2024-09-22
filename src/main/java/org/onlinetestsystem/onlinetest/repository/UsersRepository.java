package org.onlinetestsystem.onlinetest.repository;

import org.onlinetestsystem.onlinetest.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
    // Custom method to find a user by username
    Optional<Users> findByUsername(String username);

    // Custom method to find all lecturers if needed in the future
    Optional<Users> findByRole(String role);
}
