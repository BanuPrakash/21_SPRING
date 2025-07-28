package com.adobe.service;

import com.adobe.repo.UserRepo;

import java.util.logging.Logger;

public class AppService {
    Logger logger = Logger.getLogger("appService");
    UserRepo userRepo = new UserRepo();
    public void doTask() {
        logger.info("Called Service...");
        userRepo.addUser();
    }
}
