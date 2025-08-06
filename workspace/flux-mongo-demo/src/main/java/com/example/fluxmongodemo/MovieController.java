package com.example.fluxmongodemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

@RestController

public class MovieController {

    @Autowired
    private MovieRepository repository;

    @PostMapping(value = "/movie")
    public Mono<String> addMovie(@RequestBody Movie m) {
        this.repository.save(m).subscribe();
        return Mono.just("Movie added!!!");
    }
    @GetMapping(value = "/movie", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Movie> getMovies(){
        return this.repository.findBy();
    }

}