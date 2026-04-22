package com.samialsohan.taskflow.repository;
import com.samialsohan.taskflow.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}