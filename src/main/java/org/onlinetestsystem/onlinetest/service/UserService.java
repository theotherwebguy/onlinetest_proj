package org.onlinetestsystem.onlinetest.service;

import jakarta.servlet.http.HttpSession;
import org.onlinetestsystem.onlinetest.entity.Users;
import org.onlinetestsystem.onlinetest.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private HttpSession session;  // Inject session handling

    // Method to get the currently logged-in user (lecturer or student)
    public Users getCurrentUser() {
        // Get the username from the session
        String username = (String) session.getAttribute("username");

        // Check if the username exists in the session
        if (username == null) {
            throw new RuntimeException("No user is logged in");
        }

        // Find the user by username
        Optional<Users> user = usersRepository.findByUsername(username);
        return user.orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Method to get the currently logged-in lecturer
    public Users getCurrentLecturer() {
        Users user = getCurrentUser();
        if (!user.getRole().name().equalsIgnoreCase("LECTURER")) {
            throw new RuntimeException("Logged in user is not a lecturer");
        }
        return user;
    }

    // Method to validate if a user is logged in and is a lecturer
    public boolean isLecturerLoggedIn() {
        Users user = getCurrentUser();
        return user.getRole().name().equalsIgnoreCase("LECTURER");
    }

    // Method to validate user password
    public boolean validatePassword(String rawPassword, String encodedPassword) {
        return org.mindrot.jbcrypt.BCrypt.checkpw(rawPassword, encodedPassword);  // Validate password using BCrypt
    }

    // Method to invalidate the current session (used for logout)
    public void logoutUser() {
        session.invalidate();  // Invalidate session when the user logs out
    }
}
