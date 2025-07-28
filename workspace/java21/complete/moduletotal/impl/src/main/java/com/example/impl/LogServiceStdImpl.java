package com.example.impl;

import com.example.api.LogService;

public class LogServiceStdImpl implements LogService {
    @Override
    public void log(String msg) {
        System.out.println("Logging Std Impl: " + msg);
    }
}
