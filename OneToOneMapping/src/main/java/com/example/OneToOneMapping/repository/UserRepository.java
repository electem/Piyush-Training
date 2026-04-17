package com.example.OneToOneMapping.repository;

import com.example.OneToOneMapping.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
}

