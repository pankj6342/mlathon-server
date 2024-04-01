package com.mlathon.server.repositories;

import com.mlathon.server.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mlathon.server.entities.User;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmailAndPassword(String email, String password);
    User findByEmail(String email);
    @Query("SELECT p FROM User p WHERE p.id = :userId")
    Optional<User> findUserById(Integer userId);

    User findByEmailAndPasswordAndRole(String email, String password, UserRole role);
}
