package com.ManytoManyMapping.service;

import com.ManytoManyMapping.entity.Courses;
import com.ManytoManyMapping.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    // CREATE
    public Courses saveCourse(Courses course) {
        return courseRepository.save(course);
    }

    // GET ALL
    public List<Courses> getAllCourse() {
        return courseRepository.findAll();
    }

    // GET BY ID
    public Courses getCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
    }

    // UPDATE
    public Courses updateCourse(Long id, Courses updatedCourse) {
        Courses existing = getCourseById(id);
        existing.setTitle(updatedCourse.getTitle());
        return courseRepository.save(existing);
    }

    // DELETE
    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }
}