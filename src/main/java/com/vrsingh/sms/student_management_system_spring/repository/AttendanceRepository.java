package com.vrsingh.sms.student_management_system_spring.repository;
import com.vrsingh.sms.student_management_system_spring.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
}