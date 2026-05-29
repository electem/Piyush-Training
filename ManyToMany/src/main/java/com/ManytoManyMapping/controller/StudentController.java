package com.ManytoManyMapping.controller;

import com.ManytoManyMapping.entity.Student;
import com.ManytoManyMapping.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping
    public Student create(@RequestBody Student student) {
        return studentService.saveStudent(student);
    }

    @GetMapping
    public List<Student> getAll() {
        return studentService.getAllStudent();
    }

    @GetMapping("/{id}")
    public Student getById(@PathVariable Long id) {
        return studentService.getStudentById(id);
    }

    @PutMapping("/{id}")
    public Student update(@PathVariable Long id, @RequestBody Student student) {
        return studentService.updateStudent(id, student);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return "Deleted";
    }

    // ASSIGN COURSES
    @PostMapping("/{id}/courses")
    public Student assignCourses(@PathVariable Long id,
                                 @RequestBody Set<Long> courseIds) {
        return studentService.assignCourses(id, courseIds);
    }
}