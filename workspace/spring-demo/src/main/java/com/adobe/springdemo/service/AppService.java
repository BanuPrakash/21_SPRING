package com.adobe.springdemo.service;

import com.adobe.springdemo.repo.UserRepo;
import com.adobe.springdemo.repo.UserRepoDatabaseImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppService {
//    private UserRepo userRepo = new UserRepoDatabaseImpl(); // Tight coupling

    @Autowired
    private UserRepo userRepo; // wiring is done by Spring Container

    public void newUser() {
        userRepo.register();
    }
}
