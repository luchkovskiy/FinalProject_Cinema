package com.java.service;

import com.java.model.Person;
import com.java.model.Ticket;
import com.java.repository.Repository;
import com.java.repository.TicketRepository;
import com.java.repository.TicketRepositoryImpl;
import java.util.List;

public class TicketServiceImpl implements TicketService {

    private final Repository<Ticket> repository = new TicketRepositoryImpl();

    private final TicketRepository ticketRepository = new TicketRepositoryImpl();

    @Override
    public void buyTicket(Ticket ticket) {
        repository.create(ticket);
    }

    @Override
    public Ticket read(Long id) {
        return repository.read(id);
    }

    @Override
    public Ticket update(Ticket ticket) {
        return repository.update(ticket);
    }

    @Override
    public boolean delete(Long id) {
        return repository.delete(id);
    }

    @Override
    public List<Ticket> readNotUsedTicket(Person person) {
        return ticketRepository.readNotUsedTicket(person);
    }

    @Override
    public boolean idCheck(Long id) {
        return ticketRepository.idCheck(id);
    }

    @Override
    public List<Ticket> readAllById(Long id) {
        return ticketRepository.readAllById(id);
    }

    @Override
    public List<Ticket> readUsedTicket(Long id) {
        return ticketRepository.readUsedTicket(id);
    }
}
