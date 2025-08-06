package com.example.securitydemo;


import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class SampleController {
    UserDetailsService filter;
    @GetMapping
    public String sayHello() {
        return "Hello World!!!";
    }

    @GetMapping("/user")
    public String sayHelloUser() {
        return "Hello User!!!";
    }

    @GetMapping("/admin")
    public String sayHelloAdmin() {
        return "Hello Admin!!!";
    }
}
