package com.example.skillforge.controller;

import com.example.skillforge.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Test controller for INSTRUCTOR role endpoints
 * All endpoints require INSTRUCTOR role
 */
@RestController
@RequestMapping("/api/instructor")
@lombok.RequiredArgsConstructor
public class InstructorController {

    private final com.example.skillforge.service.EmailService emailService;
    private final com.example.skillforge.repository.InstructorRepository instructorRepository;
    private final com.example.skillforge.repository.CourseRepository courseRepository;
    private final com.example.skillforge.repository.EnrollmentRepository enrollmentRepository;
    private final com.example.skillforge.repository.ReviewRepository reviewRepository;

    /**
     * Get instructor dashboard data
     * Requires INSTRUCTOR role
     */
    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getInstructorDashboard(Authentication authentication) {
        Map<String, Object> data = new HashMap<>();
        data.put("message", "Welcome to Instructor Dashboard");
        data.put("user", authentication.getName());
        data.put("role", "INSTRUCTOR");
        data.put("features", new String[]{
            "Create & Manage Courses",
            "Upload Course Materials",
            "Create Quizzes",
            "View Student Progress",
            "Manage Enrollments"
        });
        
        return ResponseEntity.ok(ApiResponse.success("Instructor dashboard data", data));
    }

    /**
     * Get instructor's courses
     */
    @GetMapping("/courses")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getCourses(Authentication authentication) {
        String email = authentication.getName();
        com.example.skillforge.model.entity.Instructor instructor = instructorRepository.findAll().stream()
                .filter(i -> i.getUser() != null && email.equals(i.getUser().getEmail()))
                .findFirst()
                .orElse(null);

        long totalCourses = 0;
        if (instructor != null) {
            totalCourses = courseRepository.findByInstructorId(instructor.getId()).size();
        }

        Map<String, Object> data = new HashMap<>();
        data.put("message", "Instructor Courses");
        data.put("user", authentication.getName());
        data.put("totalCourses", totalCourses);
        
        return ResponseEntity.ok(ApiResponse.success("Instructor courses", data));
    }

    /**
     * Get instructor analytics
     */
    @GetMapping("/analytics")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getAnalytics(Authentication authentication) {
        String email = authentication.getName();
        com.example.skillforge.model.entity.Instructor instructor = instructorRepository.findAll().stream()
                .filter(i -> i.getUser() != null && email.equals(i.getUser().getEmail()))
                .findFirst()
                .orElse(null);

        long totalStudents = 0;
        long activeCourses = 0;
        double completionRate = 0.0;
        double averageRating = 0.0;

        if (instructor != null) {
            java.util.List<com.example.skillforge.model.entity.Course> courses = courseRepository.findByInstructorId(instructor.getId());
            activeCourses = courses.stream().filter(c -> Boolean.TRUE.equals(c.getIsPublished())).count();
            
            Long studentsCount = enrollmentRepository.countTotalStudentsByInstructor(instructor.getId());
            totalStudents = studentsCount != null ? studentsCount : 0;

            java.util.List<Long> courseIds = courses.stream().map(com.example.skillforge.model.entity.Course::getId).collect(java.util.stream.Collectors.toList());
            if (!courseIds.isEmpty()) {
                java.util.List<com.example.skillforge.model.entity.Enrollment> enrollments = enrollmentRepository.findByCourseIdIn(courseIds);
                if (!enrollments.isEmpty()) {
                    long completedCount = enrollments.stream().filter(e -> Boolean.TRUE.equals(e.getIsCompleted())).count();
                    completionRate = Math.round((completedCount * 100.0 / enrollments.size()) * 10.0) / 10.0;
                }
            }

            if (!courseIds.isEmpty()) {
                averageRating = courseIds.stream()
                        .map(reviewRepository::getAverageRating)
                        .filter(r -> r != null && r > 0.0)
                        .mapToDouble(Double::doubleValue)
                        .average()
                        .orElse(0.0);
                averageRating = Math.round(averageRating * 10.0) / 10.0;
            }
        }

        Map<String, Object> data = new HashMap<>();
        data.put("totalStudents", totalStudents);
        data.put("activeCourses", activeCourses);
        data.put("completionRate", completionRate);
        data.put("averageRating", averageRating);
        
        return ResponseEntity.ok(ApiResponse.success("Instructor analytics", data));
    }
    /**
     * Contact Admin
     * Endpoint for instructor to send message to admin
     */
    @PostMapping("/contact-admin")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<ApiResponse<String>> contactAdmin(@RequestBody com.example.skillforge.dto.request.ContactAdminRequest request, Authentication authentication) {
        String instructorEmail = authentication.getName();
        // Ideally fetch Name from DB, but Email is sufficient for now or we can use a helper
        // Since we don't have easy access to User object here without Service, we'll just use Email as name or "Instructor"
        
        emailService.sendContactAdminEmail(instructorEmail, "Instructor", request.getSubject(), request.getMessage());
        
        return ResponseEntity.ok(ApiResponse.success("Message sent to admin successfully", null));
    }
}
