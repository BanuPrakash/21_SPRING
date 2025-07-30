package com.adobe.springdemo.service;

import com.adobe.springdemo.repo.UserRepo;
import com.adobe.springdemo.repo.UserRepoDatabaseImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Service
public class AppService {
    @Autowired
    private UserRepo userRepo; // wiring is done by Spring Container

    @Autowired
    DataSource dataSource;

    public void newUser() {
        try {
            Connection con = dataSource.getConnection();
            System.out.println(con);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        userRepo.register();
    }
}
