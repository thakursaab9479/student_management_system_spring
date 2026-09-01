package com.vrsingh.sms.student_management_system_spring.controller;

import com.vrsingh.sms.student_management_system_spring.entity.Grade;
import com.vrsingh.sms.student_management_system_spring.service.GradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grades")
public class GradeController {

    private final GradeService gradeService;

    @Autowired
    public GradeController(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','STUDENT')")
    @GetMapping
    public List<Grade> getAllGrades() {
        return gradeService.getAllGrades();
    }

    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    @PostMapping
    public Grade addGrade(@RequestParam Long studentId,
                          @RequestParam Long courseId,
                          @RequestParam String examType,
                          @RequestParam Integer marks) {
        return gradeService.addGrade(studentId, courseId, examType, marks);
    }

    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    @DeleteMapping("/{id}")
    public void deleteGrade(@PathVariable Long id) {
        gradeService.deleteGrade(id);
    }
}