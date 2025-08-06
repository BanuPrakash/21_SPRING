package com.example.fluxmongodemo;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Test {
    public static void main(String[] args) throws Exception{
        Movie movie = new Movie();
        movie.setTitle("Some Movie");
        movie.setGenre("Comedy");
        ObjectMapper mapper = new ObjectMapper();
        System.out.println(mapper.writeValueAsString(movie));

    }
}
