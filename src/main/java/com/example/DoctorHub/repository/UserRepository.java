package com.example.DoctorHub.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.DoctorHub.model.User;
import com.example.DoctorHub.Enum.Role;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    
    // Find user by email
    Optional<User> findByEmail(String email);
    
    // Check if email exists
    boolean existsByEmail(String email);
    
    // Find users by role
    List<User> findByRole(Role role);
    
    // Find users by role string (for flexibility)
    @Query(value = "SELECT * FROM user u WHERE u.role = :role", nativeQuery = true)
    List<User> findByRoleString(@Param("role") String role);
    
    // Find active users (if you add an active field later)
    // List<User> findByActiveTrue();
    
    // Find users by name containing (for search functionality)
    List<User> findByNameContainingIgnoreCase(String name);
}
