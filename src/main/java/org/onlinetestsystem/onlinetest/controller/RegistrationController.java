package org.onlinetestsystem.onlinetest.controller;

import org.mindrot.jbcrypt.BCrypt;
import org.onlinetestsystem.onlinetest.entity.Role;
import org.onlinetestsystem.onlinetest.entity.Users;
import org.onlinetestsystem.onlinetest.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class RegistrationController {

    @Autowired
    private UsersRepository usersRepository;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Users user) {
        // Check if the username already exists
        if (usersRepository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists");
        }

        // Validate and convert the role
        Role role;
        try {
            role = Role.fromString(String.valueOf(user.getRole()));  // Ensure `user.getRole()` is a String
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid role specified");
        }

        // Encode the password using BCrypt and set the role
        String encodedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(encodedPassword);
        user.setRole(role);

        // Save the user
        usersRepository.save(user);
        return ResponseEntity.ok("Registration successful");
    }
}
