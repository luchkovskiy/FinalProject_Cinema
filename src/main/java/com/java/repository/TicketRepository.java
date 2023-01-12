package com.java.repository;

import com.java.model.Person;
import com.java.model.Ticket;
import java.util.List;

public interface TicketRepository extends Repository<Ticket> {

    List<Ticket> readNotUsedTicket(Person person);

    boolean idCheck(Long id);

    List<Ticket> readAllById(Long id);

    List<Ticket> readUsedTicket(Long id);

    @Override
    default boolean delete(Ticket ticket) {
        return false;
    }
}
