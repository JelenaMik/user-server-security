package com.example.security.repository;

import java.util.List;
import java.util.Optional;

import com.example.security.enums.Role;
import com.example.security.repository.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    List<User> findFirst10ByOrderByIdAsc();
    List <User> findFirst10ByEmailContaining(String email);
    List <User> findFirst10ByRole(Role role);
    Boolean existsByEmail(String email);

}
