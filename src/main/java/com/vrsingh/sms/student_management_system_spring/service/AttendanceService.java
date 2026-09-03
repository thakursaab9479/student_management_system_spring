package com.vrsingh.sms.student_management_system_spring.service;

import com.vrsingh.sms.student_management_system_spring.entity.Attendance;
import com.vrsingh.sms.student_management_system_spring.entity.Student;
import com.vrsingh.sms.student_management_system_spring.entity.Course;
import com.vrsingh.sms.student_management_system_spring.repository.AttendanceRepository;
import com.vrsingh.sms.student_management_system_spring.repository.StudentRepository;
import com.vrsingh.sms.student_management_system_spring.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public AttendanceService(AttendanceRepository attendanceRepository,
                             StudentRepository studentRepository,
                             CourseRepository courseRepository) {
        this.attendanceRepository = attendanceRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    public Page<Attendance> getAllAttendance(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return attendanceRepository.findAll(pageable);
    }

    public Attendance markAttendance(Long studentId, Long courseId, LocalDate classDate, String status) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id " + studentId));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with id " + courseId));

        Attendance attendance = new Attendance();
        attendance.setStudent(student);
        attendance.setCourse(course);
        attendance.setClassDate(classDate);
        attendance.setStatus(status);

        return attendanceRepository.save(attendance);
    }

    public void deleteAttendance(Long id) {
        attendanceRepository.deleteById(id);
    }
}