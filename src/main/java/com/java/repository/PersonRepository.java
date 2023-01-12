package com.java.repository;

import com.java.model.Person;
import java.util.List;

public interface PersonRepository extends Repository<Person> {

    List<Person> getAllPersons();

    Person entrance(Person person);

    Person update(Long id, Person person);

    boolean idCheck(Long id);

    boolean checkLoginValid(String login);

    boolean checkUsernameValid(String username);

    void updateCashByTicketPrice(Person person);

    void updateCash(Person person);
}
