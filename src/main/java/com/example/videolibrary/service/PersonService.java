package com.example.videolibrary.service;

import com.example.videolibrary.dao.PersonDao;
import com.example.videolibrary.dao.impl.PersonDaoImpl;
import com.example.videolibrary.model.Person;

import java.util.List;

public class PersonService {
    private static PersonService instance;
    private final PersonDao personDao;

    private PersonService() {
        this.personDao = new PersonDaoImpl();
    }

    public static PersonService getInstance() {
        if (instance == null) {
            instance = new PersonService();
        }
        return instance;
    }

    public Person addPerson(Person person) {
        return personDao.save(person);
    }

    public Person getPersonById(Long id) {
        return personDao.findById(id);
    }

    public List<Person> getAllPersons() {
        return personDao.findAll();
    }

    public List<Person> getAllActors() {
        return personDao.findActors();
    }

    public List<Person> getAllDirectors() {
        return personDao.findDirectors();
    }

    public void deletePerson(Long id) {
        personDao.delete(id);
    }

    public Person updatePerson(Person person) {
        return personDao.update(person);
    }
}
