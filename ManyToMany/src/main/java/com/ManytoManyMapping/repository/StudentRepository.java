package com.ManytoManyMapping.repository;

import com.ManytoManyMapping.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student,Long> {
}
