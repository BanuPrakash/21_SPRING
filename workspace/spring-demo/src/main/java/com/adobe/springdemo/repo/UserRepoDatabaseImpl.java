package com.adobe.springdemo.repo;

import org.springframework.stereotype.Repository;

@Repository
public class UserRepoDatabaseImpl implements UserRepo{
    @Override
    public void register() {
        System.out.println("Registered in Database!!!");
    }
}
