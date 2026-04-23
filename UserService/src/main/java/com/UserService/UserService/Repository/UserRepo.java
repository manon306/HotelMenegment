package com.UserService.UserService.Repository;

import java.util.Optional;
import com.UserService.UserService.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {

    // Optional UpdateUser(Long Id);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);
    
}
