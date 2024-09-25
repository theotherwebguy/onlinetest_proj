package org.onlinetestsystem.onlinetest.controller;

import jakarta.servlet.http.HttpSession;
import org.onlinetestsystem.onlinetest.entity.Users;
import org.onlinetestsystem.onlinetest.repository.UsersRepository;
import org.onlinetestsystem.onlinetest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class ContentController {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private HttpSession session;  // Inject session handling

    // Mapping for the login page (GET)
    @GetMapping("/auth/login")
    public String showLoginPage() {
        return "auth/login";  // This returns login.html located in the templates/auth folder
    }

    // Mapping to handle login form submission (POST)
    @PostMapping("/auth/login")
    public String handleLogin(@RequestParam("username") String username,
                              @RequestParam("password") String password,
                              RedirectAttributes redirectAttributes) {

        // Validate user credentials
        Optional<Users> userOpt = usersRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            Users user = userOpt.get();

            // Validate password
            if (userService.validatePassword(password, user.getPassword())) {
                // Store username in the session after successful login
                session.setAttribute("username", user.getUsername());
                session.setAttribute("fullName", user.getFullName());

                // Redirect based on role
                if (user.getRole().name().equalsIgnoreCase("STUDENT")) {
                    return "redirect:/student/dashboard";
                } else if (user.getRole().name().equalsIgnoreCase("LECTURER")) {
                    return "redirect:/lecturer/dashboard";
                }
            }
        }

        // If invalid credentials, return to login page with error
        redirectAttributes.addFlashAttribute("error", "Invalid username or password");
        return "redirect:/auth/login";
    }

    // Logout logic
    @GetMapping("/auth/logout")
    public String handleLogout() {
        session.invalidate();  // Invalidate the session on logout
        return "redirect:/auth/login";
    }
}
