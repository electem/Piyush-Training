package com.ManytoManyMapping.service;

import com.ManytoManyMapping.entity.Courses;
import com.ManytoManyMapping.entity.Student;
import com.ManytoManyMapping.repository.CourseRepository;
import com.ManytoManyMapping.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    // CREATE
    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    // GET ALL
    public List<Student> getAllStudent() {
        return studentRepository.findAll();
    }

    // GET BY ID
    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));
    }

    // UPDATE
    public Student updateStudent(Long id, Student student) {
        Student existing = getStudentById(id);
        existing.setName(student.getName());
        return studentRepository.save(existing);
    }

    // DELETE
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    // ASSIGN COURSES (FIXED)
    public Student assignCourses(Long studentId, Set<Long> courseIds) {

        Student student = getStudentById(studentId);

        Set<Courses> courses = new HashSet<>(courseRepository.findAllById(courseIds));

        student.getCourses().clear();
        student.getCourses().addAll(courses);

        return studentRepository.save(student);
    }
}