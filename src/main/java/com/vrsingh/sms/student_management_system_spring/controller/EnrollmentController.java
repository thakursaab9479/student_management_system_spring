package com.vrsingh.sms.student_management_system_spring.controller;

import com.vrsingh.sms.student_management_system_spring.entity.Enrollment;
import com.vrsingh.sms.student_management_system_spring.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @Autowired
    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    @GetMapping
    public List<Enrollment> getAllEnrollments() {
        return enrollmentService.getAllEnrollments();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Enrollment enrollStudent(@RequestParam Long studentId,
                                    @RequestParam Long courseId,
                                    @RequestParam LocalDate enrollmentDate) {
        return enrollmentService.enrollStudent(studentId, courseId, enrollmentDate);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteEnrollment(@PathVariable Long id) {
        enrollmentService.deleteEnrollment(id);
    }
}