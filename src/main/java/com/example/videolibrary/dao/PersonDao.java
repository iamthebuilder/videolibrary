package com.example.videolibrary.dao;

import com.example.videolibrary.model.Person;
import java.util.List;

public interface PersonDao {
    Person save(Person person);
    Person findById(Long id);
    List<Person> findAll();
    List<Person> findActors();
    List<Person> findDirectors();
    void delete(Long id);
    Person update(Person person);
}
