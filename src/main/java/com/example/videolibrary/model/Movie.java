package com.example.videolibrary.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movie {
    private Long id;
    private String title;
    private List<Person> actors;
    private Person director;
    private LocalDate releaseDate;
    private String country;
    private String genre;
}
