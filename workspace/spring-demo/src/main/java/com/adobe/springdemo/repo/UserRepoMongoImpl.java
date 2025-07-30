package com.adobe.springdemo.repo;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;


@Repository
public class UserRepoMongoImpl implements UserRepo{
    @Override
    public void register() {
        System.out.println("Stored in MongoDB!!!");
    }
}
