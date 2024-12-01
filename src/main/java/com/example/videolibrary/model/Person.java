package com.example.videolibrary.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person {
    private Long id;
    private String fullName;
    private LocalDate birthDate;
    private boolean isActor;
    private boolean isDirector;
}
