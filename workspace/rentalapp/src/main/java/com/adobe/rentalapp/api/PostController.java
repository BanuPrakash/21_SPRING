package com.adobe.rentalapp.api;

import com.adobe.rentalapp.dto.Post;
import com.adobe.rentalapp.dto.User;
import com.adobe.rentalapp.service.AggregatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/posts")
@RequiredArgsConstructor
public class PostController {
    private  final AggregatorService service;

    record PostUserDTO(String title, String username){}

    @GetMapping
    public List<PostUserDTO> getPosts() {
        CompletableFuture<List<Post>> posts = service.getPosts(); // non-blocking
        CompletableFuture<List<User>> users = service.getUsers();  // non-blocking

        // barrier
        List<Post> postsList = posts.join(); //blocked till Posts Thread finishes
        List<User> userList = users.join(); // blocking code till Users thread finishes

        return postsList.stream().map(post -> {
            String username = userList.stream()
                    .filter(user -> user.id() == post.userId())
                    .findFirst().get().username();
            return new PostUserDTO(post.title(), username);
        }).collect(Collectors.toList());

    }

}
