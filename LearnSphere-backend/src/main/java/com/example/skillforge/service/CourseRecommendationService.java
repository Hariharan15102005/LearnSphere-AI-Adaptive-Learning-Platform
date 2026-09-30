package com.example.skillforge.service;

import com.example.skillforge.model.entity.Course;
import com.example.skillforge.model.enums.DifficultyLevel;
import com.example.skillforge.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseRecommendationService {

    private final CourseRepository courseRepository;
    private final com.example.skillforge.repository.EnrollmentRepository enrollmentRepository;
    private final com.example.skillforge.repository.CourseProgressRepository courseProgressRepository;
    private final com.example.skillforge.repository.UserRepository userRepository;
    private final com.example.skillforge.repository.StudentRepository studentRepository;

    private List<Long> getExcludedCourseIds(Long studentId) {
        if (studentId == null) return Collections.emptyList();
        java.util.Set<Long> set = new java.util.HashSet<>();

        Long resolvedStudentId = studentId;
        Long resolvedUserId = studentId;

        var studentOpt = studentRepository.findById(studentId);
        if (studentOpt.isPresent() && studentOpt.get().getUser() != null) {
            resolvedUserId = studentOpt.get().getUser().getId();
        } else {
            var studentByUserOpt = studentRepository.findByUserId(studentId);
            if (studentByUserOpt.isPresent()) {
                resolvedStudentId = studentByUserOpt.get().getId();
            }
        }

        // 1. From CourseProgress
        courseProgressRepository.findByStudentId(resolvedStudentId).forEach(p -> {
            if (p.getCourseId() != null) set.add(p.getCourseId());
        });
        if (!resolvedStudentId.equals(resolvedUserId)) {
            courseProgressRepository.findByStudentId(resolvedUserId).forEach(p -> {
                if (p.getCourseId() != null) set.add(p.getCourseId());
            });
        }

        // 2. From Enrollments (All enrolled and completed courses)
        enrollmentRepository.findByStudentId(resolvedStudentId).forEach(e -> {
            if (e.getCourse() != null && e.getCourse().getId() != null) set.add(e.getCourse().getId());
        });
        if (!resolvedStudentId.equals(resolvedUserId)) {
            enrollmentRepository.findByStudentId(resolvedUserId).forEach(e -> {
                if (e.getCourse() != null && e.getCourse().getId() != null) set.add(e.getCourse().getId());
            });
        }

        return new java.util.ArrayList<>(set);
    }

    public Course recommendNextCourse(Long completedCourseId, Long studentId) {
        System.out.println("DEBUG: recommendNextCourse called for course " + completedCourseId + " student " + studentId);
        Course completedCourse = courseRepository.findById(completedCourseId).orElse(null);
        if (completedCourse == null) return null;

        // Get all completed and enrolled course IDs to EXCLUDE
        List<Long> excludedCourseIds = new java.util.ArrayList<>(getExcludedCourseIds(studentId));
        if (!excludedCourseIds.contains(completedCourseId)) {
            excludedCourseIds.add(completedCourseId);
        }

        DifficultyLevel currentLevel = completedCourse.getDifficultyLevel();
        String category = completedCourse.getCategory();

        // Level Up Logic
        DifficultyLevel nextLevel = getNextLevel(currentLevel);

        // 1. Try to find same category, NEXT level
        List<Course> nextLevelCourses = courseRepository.findByCategoryAndDifficultyLevelAndIdNotIn(
                category, nextLevel, excludedCourseIds
        );
        if (!nextLevelCourses.isEmpty()) {
            return nextLevelCourses.get(0);
        }

        // 2. Try to find same category, SAME level
        List<Course> sameLevelCourses = courseRepository.findByCategoryAndDifficultyLevelAndIdNotIn(
                category, currentLevel, excludedCourseIds
        );
        if (!sameLevelCourses.isEmpty()) {
            return sameLevelCourses.get(0);
        }

        // 3. Similar courses in same category
        List<Course> similarCourses = courseRepository.findRelatedCourses(category, completedCourseId);
        for (Course c : similarCourses) {
            if (!excludedCourseIds.contains(c.getId())) {
                return c;
            }
        }

        // 4. Match student's profile skills
        if (studentId != null) {
            Long resolvedUserId = studentId;
            var sOpt = studentRepository.findById(studentId);
            if (sOpt.isPresent() && sOpt.get().getUser() != null) {
                resolvedUserId = sOpt.get().getUser().getId();
            }
            var userOpt = userRepository.findById(resolvedUserId);
            if (userOpt.isEmpty()) {
                userOpt = userRepository.findById(studentId);
            }
            if (userOpt.isPresent() && userOpt.get().getSkills() != null && !userOpt.get().getSkills().trim().isEmpty()) {
                String[] skillList = userOpt.get().getSkills().split(",");
                List<Course> allPublished = courseRepository.findAllPublishedCourses();
                for (String rawSkill : skillList) {
                    String skill = rawSkill.trim().toLowerCase();
                    if (skill.isEmpty()) continue;
                    for (Course c : allPublished) {
                        if (excludedCourseIds.contains(c.getId())) continue;
                        boolean matches = (c.getCategory() != null && c.getCategory().toLowerCase().contains(skill)) ||
                                          (c.getTitle() != null && c.getTitle().toLowerCase().contains(skill)) ||
                                          (c.getDescription() != null && c.getDescription().toLowerCase().contains(skill)) ||
                                          (c.getTags() != null && c.getTags().toLowerCase().contains(skill));
                        if (matches) {
                            return c;
                        }
                    }
                }
            }
        }

        // 5. Fallback: Top popular globally, excluding completed/enrolled
        List<Course> popular = courseRepository.findTop3ByCategoryAndIsPublishedTrueOrderByTotalEnrollmentsDesc(category != null ? category : "Programming"); 
        for (Course c : popular) {
            if (!excludedCourseIds.contains(c.getId())) {
                return c;
            }
        }
        
        // 6. Ultimate Fallback: Any published course not completed/enrolled
        List<Course> allPublished = courseRepository.findAllPublishedCourses();
        for (Course c : allPublished) {
            if (!excludedCourseIds.contains(c.getId())) {
                return c;
            }
        }

        return null; // No recommendation found
    }

    public List<Course> recommendCoursesForDashboard(Long studentId) {
        List<Long> excludedIds = getExcludedCourseIds(studentId);
        List<Course> recommendations = new java.util.ArrayList<>();

        // 1. If last interacted course was completed -> recommend next level course
        var lastProgress = courseProgressRepository.findTopByStudentIdOrderByLastUpdatedDesc(studentId);
        if (lastProgress.isPresent()) {
            Long lastCourseId = lastProgress.get().getCourseId();
            if (lastProgress.get().getProgressPercent() != null && lastProgress.get().getProgressPercent() >= 99.0) {
                Course next = recommendNextCourse(lastCourseId, studentId);
                if (next != null) {
                    recommendations.add(next);
                }
            }
        }

        // 2. Profile skills / Interests matching (high personalization)
        if (studentId != null) {
            Long resolvedUserId = studentId;
            var sOpt = studentRepository.findById(studentId);
            if (sOpt.isPresent() && sOpt.get().getUser() != null) {
                resolvedUserId = sOpt.get().getUser().getId();
            }
            var userOpt = userRepository.findById(resolvedUserId);
            if (userOpt.isEmpty()) {
                userOpt = userRepository.findById(studentId);
            }
            if (userOpt.isPresent() && userOpt.get().getSkills() != null && !userOpt.get().getSkills().trim().isEmpty()) {
                String[] skillList = userOpt.get().getSkills().split(",");
                List<Course> allPublished = courseRepository.findAllPublishedCourses();
                for (String rawSkill : skillList) {
                    String skill = rawSkill.trim().toLowerCase();
                    if (skill.isEmpty()) continue;
                    for (Course c : allPublished) {
                        if (excludedIds.contains(c.getId()) || recommendations.contains(c)) continue;
                        boolean matches = (c.getCategory() != null && c.getCategory().toLowerCase().contains(skill)) ||
                                          (c.getTitle() != null && c.getTitle().toLowerCase().contains(skill)) ||
                                          (c.getDescription() != null && c.getDescription().toLowerCase().contains(skill)) ||
                                          (c.getTags() != null && c.getTags().toLowerCase().contains(skill));
                        if (matches) {
                            recommendations.add(c);
                            if (recommendations.size() >= 3) break;
                        }
                    }
                    if (recommendations.size() >= 3) break;
                }
            }
        }

        // 3. Category progression / Related courses from last active course
        if (recommendations.size() < 3 && lastProgress.isPresent()) {
            Long lastCourseId = lastProgress.get().getCourseId();
            Course active = courseRepository.findById(lastCourseId).orElse(null);
            if (active != null) {
                List<Course> related = courseRepository.findRelatedCourses(active.getCategory(), lastCourseId);
                for (Course c : related) {
                    if (!excludedIds.contains(c.getId()) && !recommendations.contains(c)) {
                        recommendations.add(c);
                        if (recommendations.size() >= 3) break;
                    }
                }
            }
        }

        // 4. Fallback: Popular courses (filter out enrolled/completed)
        if (recommendations.size() < 3) {
            List<Course> popular = courseRepository.findTop3ByCategoryAndIsPublishedTrueOrderByTotalEnrollmentsDesc("Programming"); 
            for (Course c : popular) {
                if (!excludedIds.contains(c.getId()) && !recommendations.contains(c)) {
                    recommendations.add(c);
                    if (recommendations.size() >= 3) break;
                }
            }
        }

        // 5. Ultimate Fallback: Any published courses not enrolled/completed
        if (recommendations.size() < 3) {
            List<Course> allPublished = courseRepository.findAllPublishedCourses();
            for (Course c : allPublished) {
                if (!excludedIds.contains(c.getId()) && !recommendations.contains(c)) {
                    recommendations.add(c);
                    if (recommendations.size() >= 3) break;
                }
            }
        }

        return recommendations;
    }

    private DifficultyLevel getNextLevel(DifficultyLevel level) {
        if (level == null) return DifficultyLevel.INTERMEDIATE;
        switch (level) {
            case BEGINNER: return DifficultyLevel.INTERMEDIATE;
            case INTERMEDIATE: return DifficultyLevel.ADVANCED;
            case ADVANCED: return DifficultyLevel.ADVANCED; // Stay at advanced or find specialized
            default: return DifficultyLevel.BEGINNER;
        }
    }
}
