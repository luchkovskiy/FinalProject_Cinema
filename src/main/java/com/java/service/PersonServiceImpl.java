package com.java.service;

import com.java.model.Person;
import com.java.repository.PersonRepository;
import com.java.repository.PersonRepositoryImpl;
import com.java.repository.Repository;
import java.util.List;

public class PersonServiceImpl implements PersonService {

    private final Repository<Person> repository = new PersonRepositoryImpl();
    private final PersonRepository personRepository = new PersonRepositoryImpl();

    @Override
    public Person create(Person person) {
        return repository.create(person);
    }

    @Override
    public Person read(Long currentId) {
        return repository.read(currentId);
    }

    @Override
    public void update(Long id, Person person) {
        personRepository.update(id, person);
    }

    @Override
    public Person update(Person person) {
        return repository.update(person);
    }

    @Override
    public boolean delete(Long id) {
        return repository.delete(id);
    }

    @Override
    public boolean delete(Person person) {
        return repository.delete(person);
    }

    @Override
    public Person entrance(Person person) {
        return personRepository.entrance(person);
    }

    @Override
    public boolean idCheck(Long id) {
        return personRepository.idCheck(id);
    }

    @Override
    public List<Person> getAllPersons() {
        return personRepository.getAllPersons();
    }

    @Override
    public boolean checkLoginValid(String login) {
        return personRepository.checkLoginValid(login);
    }

    public boolean checkUsernameValid(String username) {
        return personRepository.checkUsernameValid(username);
    }

    @Override
    public void updateCashByTicketPrice(Person person) {
        personRepository.updateCashByTicketPrice(person);
    }

    @Override
    public void updateCash(Person person) {
        personRepository.updateCash(person);
    }
}
