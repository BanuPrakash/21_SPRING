package com.adobe.springdemo.repo;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;



@ConditionalOnMissingBean(name="userRepoMongoImpl")
@Repository
public class UserRepoDatabaseImpl implements UserRepo{
    @Override
    public void register() {
        System.out.println("Registered in Database!!!");
    }
}
