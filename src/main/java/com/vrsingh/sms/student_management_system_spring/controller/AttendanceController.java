package com.vrsingh.sms.student_management_system_spring.controller;
import com.vrsingh.sms.student_management_system_spring.entity.Attendance;
import com.vrsingh.sms.student_management_system_spring.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @Autowired
    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @GetMapping
    public List<Attendance> getAllAttendance() {
        return attendanceService.getAllAttendance();
    }

    @PostMapping
    public Attendance markAttendance(@RequestParam Long studentId,
                                     @RequestParam Long courseId,
                                     @RequestParam LocalDate classDate,
                                     @RequestParam String status) {
        return attendanceService.markAttendance(studentId, courseId, classDate, status);
    }

    @DeleteMapping("/{id}")
    public void deleteAttendance(@PathVariable Long id) {
        attendanceService.deleteAttendance(id);
    }
}
