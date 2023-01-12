package com.java.service;

import com.java.model.Person;
import com.java.model.Ticket;
import java.util.List;

public interface TicketService {

    void buyTicket(Ticket ticket);

    Ticket read(Long id);

    Ticket update(Ticket ticket);

    boolean delete(Long id);

    List<Ticket> readNotUsedTicket(Person person);

    boolean idCheck(Long id);

    List<Ticket> readAllById(Long id);

    List<Ticket> readUsedTicket(Long id);

}
