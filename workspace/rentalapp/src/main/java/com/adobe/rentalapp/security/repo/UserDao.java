package com.adobe.rentalapp.security.repo;


import com.adobe.rentalapp.security.entity.User;
import com.adobe.rentalapp.security.service.UserDetailsServiceImpl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDao extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
}
