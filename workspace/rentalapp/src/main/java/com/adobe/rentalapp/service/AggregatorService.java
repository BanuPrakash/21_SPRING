package com.adobe.rentalapp.service;

import com.adobe.rentalapp.dto.Post;
import com.adobe.rentalapp.dto.User;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class AggregatorService {
    private  final PostService postService;
    private final UserService userService;

    @Async("posts-pool")
    public CompletableFuture<List<Post>> getPosts() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Getting Posts :" + Thread.currentThread());
       return CompletableFuture.completedFuture(postService.getPosts());
    }

    @Async("users-pool")
    public CompletableFuture<List<User>> getUsers() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Getting Users :" + Thread.currentThread());
        return CompletableFuture.completedFuture(userService.getUsers());
    }
}
