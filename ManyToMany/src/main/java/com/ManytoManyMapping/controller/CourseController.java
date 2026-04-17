package com.ManytoManyMapping.controller;

import com.ManytoManyMapping.entity.Courses;
import com.ManytoManyMapping.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @PostMapping
    public Courses create(@RequestBody Courses course) {
        return courseService.saveCourse(course);
    }

    @GetMapping
    public List<Courses> getAll() {
        return courseService.getAllCourse();
    }

    @GetMapping("/{id}")
    public Courses getById(@PathVariable Long id) {
        return courseService.getCourseById(id);
    }

    @PutMapping("/{id}")
    public Courses update(@PathVariable Long id, @RequestBody Courses course) {
        return courseService.updateCourse(id, course);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return "Deleted";
    }
}