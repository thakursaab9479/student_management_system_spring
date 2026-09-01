package com.vrsingh.sms.student_management_system_spring.repository;
import com.vrsingh.sms.student_management_system_spring.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
}
