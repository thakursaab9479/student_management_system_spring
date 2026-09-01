package com.vrsingh.sms.student_management_system_spring.repository;
import com.vrsingh.sms.student_management_system_spring.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
