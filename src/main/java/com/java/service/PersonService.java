package com.java.service;

import com.java.model.Person;
import java.util.List;

public interface PersonService {

    Person create(Person person);

    Person read(Long CurrentId);

    void update(Long id, Person person);

    Person update(Person person);

    boolean delete(Long id);

    boolean delete(Person person);

    Person entrance(Person person);

    boolean idCheck(Long id);

    List<Person> getAllPersons();

    boolean checkLoginValid(String login);

    boolean checkUsernameValid(String username);

    void updateCashByTicketPrice(Person person);

    void updateCash(Person person);
}




