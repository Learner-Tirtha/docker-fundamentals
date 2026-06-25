package com.university.student_project_portal.controller;



import com.university.student_project_portal.model.*;
import com.university.student_project_portal.repository.FacultyRepository;
import com.university.student_project_portal.repository.ProjectRepository;
import com.university.student_project_portal.repository.StudentRepository;
import com.university.student_project_portal.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class PortalController {

    @Autowired private UserRepository userRepository;
    @Autowired private StudentRepository studentRepository;
    @Autowired private FacultyRepository facultyRepository;
    @Autowired private ProjectRepository projectRepository;

    // Redirect Root URL to Login Screen
    // Redirect Root URL to Login Screen
    @GetMapping("/")
    public String showIndex() { return "redirect:/login"; }

    // --- AUTHENTICATION PATHWAYS ---
    @GetMapping("/login")
    public String loginPage() { return "login"; }

    @PostMapping("/login")
    public String handleLogin(@RequestParam String username, @RequestParam String password, HttpSession session, Model model) {
        User user = userRepository.findById(username).orElse(null);
        if (user != null && user.getPassword().equals(password)) {
            session.setAttribute("user", user);
            if (user.getRole() == Role.STUDENT) return "redirect:/student-dashboard";
            if (user.getRole() == Role.FACULTY) return "redirect:/faculty-dashboard";
        }
        model.addAttribute("error", "Invalid username or password credentials.");
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() { System.out.println("Register page requested"); return "register"; }

    @PostMapping("/register")
    public String handleRegistration(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String name,
            @RequestParam String role,
            @RequestParam(required = false) String abcID,
            @RequestParam(required = false) String idNo,
            @RequestParam(required = false) String department,
            Model model) {

        // Check if username already exists
        if (userRepository.existsById(username)) {
            model.addAttribute("error", "Username already exists!");
            return "register";
        }

        // Save User first
        User user = new User(username, password, Role.valueOf(role.toUpperCase()));
        userRepository.save(user);

        if (user.getRole() == Role.STUDENT) {

            Student student = new Student();
            student.setName(name);
            student.setAbcID(abcID);
            student.setIdNo(idNo);
            student.setUser(user);

            studentRepository.save(student);

        } else {

            Faculty faculty = new Faculty();
            faculty.setName(name);
            faculty.setDepartment(department);
            faculty.setUser(user);

            facultyRepository.save(faculty);
        }

        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    // --- STUDENT ROUTING ---
    @GetMapping("/student-dashboard")
    public String studentDashboard(HttpSession session, Model model) {

        User user = (User) session.getAttribute("user");

        if (user == null || user.getRole() != Role.STUDENT) {
            return "redirect:/login";
        }

        Student student = studentRepository.findByUser_Username(user.getUsername());

        if (student == null) {
            model.addAttribute("error", "Student profile not found.");
            return "login";
        }

        List<Project> allProjects = projectRepository.findAll();

        model.addAttribute("student", student);
        model.addAttribute("projects", allProjects);

        return "student-dashboard";
    }

    @PostMapping("/project/apply/{id}")
    public String applyToProject(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        Project project = projectRepository.findById(id).orElse(null);
        if (project != null && project.getEmptyPosition() > 0) {
            project.setEmptyPosition(project.getEmptyPosition() - 1);
            projectRepository.save(project);
        }
        return "redirect:/student-dashboard";
    }

    // --- FACULTY ROUTING ---
    @GetMapping("/faculty-dashboard")
    public String facultyDashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != Role.FACULTY) return "redirect:/login";

        Faculty faculty = facultyRepository.findByUser_Username(user.getUsername());
        List<Project> ownProjects = projectRepository.findByFaculty(faculty);

        model.addAttribute("faculty", faculty);
        model.addAttribute("projects", ownProjects);
        model.addAttribute("totalProjectsInSystem", projectRepository.count());
        return "faculty-dashboard";
    }

    @PostMapping("/project/add")
    public String proposeProject(@RequestParam String title, @RequestParam String tech,
                                 @RequestParam String desc, @RequestParam float duration,
                                 @RequestParam int positions,HttpSession session){
        User user=(User)session.getAttribute("user");
        Faculty faculty=facultyRepository.findByUser_Username(user.getUsername());
        Project p=new Project();
        p.setProjectTitle(title);
        p.setTechnology(tech);
        p.setDescription(desc);
        p.setDuration(duration);
        p.setEmptyPosition(positions);
        p.setFaculty(faculty);
        projectRepository.save(p);
        return "redirect:/faculty-dashboard";
    }
    @PostMapping("/project/delete/{id}")
    public String deleteProject(@PathVariable Long id, HttpSession session) {

        User user = (User) session.getAttribute("user");

        if (user == null || user.getRole() != Role.FACULTY) {
            return "redirect:/login";
        }

        Project project = projectRepository.findById(id).orElse(null);

        if (project != null) {
            projectRepository.delete(project);
        }

        return "redirect:/faculty-dashboard";
    }
}