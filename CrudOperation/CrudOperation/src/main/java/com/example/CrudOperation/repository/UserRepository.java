package com.example.CrudOperation.repository;

import com.example.CrudOperation.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {

}
