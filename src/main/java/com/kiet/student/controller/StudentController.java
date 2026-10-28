package com.kiet.student.controller;

import com.kiet.student.model.Student;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final List<Student> students = new ArrayList<>();

    public StudentController() {
        students.add(new Student(101, "Rahul Sharma", "rahul@kiet.edu", "CSE(AI & ML)", 2, 8.65));
        students.add(new Student(102, "Priya Singh", "priya@kiet.edu", "CSE", 2, 9.20));
        students.add(new Student(103, "Ankit Verma", "ankit@kiet.edu", "IT", 3, 7.80));
        students.add(new Student(104, "Neha Gupta", "neha@kiet.edu", "CSE", 2, 9.40));
        students.add(new Student(105, "Rohan Mehta", "rohan@kiet.edu", "ECE", 1, 8.10));
    }

    @GetMapping
    public List<Student> getAllStudents() {
        return students;
    }

    @GetMapping("/search")
    public List<Student> searchStudents(@RequestParam String branch,
                                        @RequestParam int year) {
        return students.stream()
                .filter(s -> s.getBranch().toLowerCase().contains(branch.toLowerCase())
                        && s.getYear() == year)
                .collect(Collectors.toList());
    }

    @GetMapping("/topper")
    public List<Student> getToppers(@RequestParam double cgpa) {
        return students.stream()
                .filter(s -> s.getCgpa() >= cgpa)
                .collect(Collectors.toList());
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<?> getStudentById(@PathVariable int studentId) {
        for (Student s : students) {
            if (s.getStudentId() == studentId) {
                return ResponseEntity.ok(s);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", "Student not found"));
    }

    @PostMapping
    public ResponseEntity<Student> addStudent(@RequestBody Student student) {
        students.add(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(student);
    }

    @PutMapping("/{studentId}")
    public ResponseEntity<?> updateStudent(@PathVariable int studentId,
                                           @RequestBody Student updated) {
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getStudentId() == studentId) {
                updated.setStudentId(studentId);
                students.set(i, updated);
                return ResponseEntity.ok(updated);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", "Student not found"));
    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity<?> deleteStudent(@PathVariable int studentId) {
        boolean removed = students.removeIf(s -> s.getStudentId() == studentId);
        if (removed) {
            return ResponseEntity.ok(Map.of("message", "Student deleted successfully"));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", "Student not found"));
    }
}