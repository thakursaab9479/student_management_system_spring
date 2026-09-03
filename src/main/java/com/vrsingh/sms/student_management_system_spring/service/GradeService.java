package com.vrsingh.sms.student_management_system_spring.service;

import com.vrsingh.sms.student_management_system_spring.entity.Grade;
import com.vrsingh.sms.student_management_system_spring.entity.Student;
import com.vrsingh.sms.student_management_system_spring.entity.Course;
import com.vrsingh.sms.student_management_system_spring.repository.GradeRepository;
import com.vrsingh.sms.student_management_system_spring.repository.StudentRepository;
import com.vrsingh.sms.student_management_system_spring.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class GradeService {

    private final GradeRepository gradeRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public GradeService(GradeRepository gradeRepository,
                        StudentRepository studentRepository,
                        CourseRepository courseRepository) {
        this.gradeRepository = gradeRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    public Page<Grade> getAllGrades(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return gradeRepository.findAll(pageable);
    }

    public Grade addGrade(Long studentId, Long courseId, String examType, Integer marks) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id " + studentId));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with id " + courseId));

        Grade grade = new Grade();
        grade.setStudent(student);
        grade.setCourse(course);
        grade.setExamType(examType);
        grade.setMarks(marks);

        return gradeRepository.save(grade);
    }

    public void deleteGrade(Long id) {
        gradeRepository.deleteById(id);
    }
}