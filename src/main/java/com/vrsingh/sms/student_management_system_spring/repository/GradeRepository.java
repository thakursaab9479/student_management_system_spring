package com.vrsingh.sms.student_management_system_spring.repository;
import com.vrsingh.sms.student_management_system_spring.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GradeRepository extends JpaRepository<Grade, Long> {
}