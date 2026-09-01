package com.vrsingh.sms.student_management_system_spring.repository;
import com.vrsingh.sms.student_management_system_spring.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
