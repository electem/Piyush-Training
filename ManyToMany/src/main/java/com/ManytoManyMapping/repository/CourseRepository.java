package com.ManytoManyMapping.repository;

import com.ManytoManyMapping.entity.Courses;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Courses,Long> {
}
