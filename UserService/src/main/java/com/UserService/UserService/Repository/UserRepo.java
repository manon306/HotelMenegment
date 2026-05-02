package com.UserService.UserService.Repository;

import java.util.Optional;

import com.UserService.UserService.ENUMS.Role;
import com.UserService.UserService.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByIdAndRole(Long id, Role role);

}
