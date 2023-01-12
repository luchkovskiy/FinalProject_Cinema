package com.java.repository;

import com.java.model.Person;
import com.java.model.Ticket;
import com.java.util.ConnectionManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TicketRepositoryImpl implements TicketRepository {

    @Override
    public Ticket create(Ticket ticket) {
        try (Connection connection = ConnectionManager.open()) {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO Ticket (ownerId, filmId, price, isUsed, isReturned) VALUES (?,?,?,?,?)");
            statement.setLong(1, ticket.getOwner());
            statement.setLong(2, ticket.getCurrentFilm());
            statement.setInt(3, ticket.getPrice());
            statement.setBoolean(4, ticket.getIsUsed());
            statement.setBoolean(5, ticket.getReturned());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка в методе create");
        }
        System.out.println("\nБилет успеешно куплен!");
        return ticket;
    }

    @Override
    public Ticket read(Long id) {
        Ticket ticket = new Ticket();
        try (Connection connection = ConnectionManager.open()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Ticket WHERE id = ?");
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                ticket.setId(resultSet.getLong("id"));
                ticket.setOwnerId(resultSet.getLong("ownerId"));
                ticket.setCurrentFilm(resultSet.getLong("filmId"));
                ticket.setPrice(resultSet.getInt("price"));
                ticket.setIsUsed(resultSet.getBoolean("isUsed"));
                ticket.setIsReturned(resultSet.getBoolean("isReturned"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка в методе read");
        }
        return ticket;
    }

    @Override
    public Ticket update(Ticket newT) {
        try (Connection connection = ConnectionManager.open()) {
            PreparedStatement statement = connection.prepareStatement("UPDATE Ticket SET isUsed = ?, isReturned = ? WHERE id = ?");
            statement.setBoolean(1, newT.getIsUsed());
            statement.setBoolean(2, newT.getReturned());
            statement.setLong(3, newT.getId());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка в методе update");
        }
        return newT;
    }

    @Override
    public boolean delete(Long id) {
        try (Connection connection = ConnectionManager.open()) {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM Ticket WHERE ownerId = ?");
            statement.setLong(1, id);
            statement.execute();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка в методе delete");
        }
    }

    @Override
    public List<Ticket> readNotUsedTicket(Person person) {
        List<Ticket> notUsedTickets = new ArrayList<>();
        try (Connection connection = ConnectionManager.open()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Ticket WHERE ownerId = ? AND isUsed = 0 AND isReturned = 0");
            statement.setLong(1, person.getId());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                Long ownerId = resultSet.getLong("ownerId");
                Long filmId = resultSet.getLong("filmId");
                Integer price = resultSet.getInt("price");
                Boolean isUsed = resultSet.getBoolean("isUsed");
                Boolean isReturned = resultSet.getBoolean("isReturned");
                Ticket ticket = new Ticket(id, ownerId, filmId, price, isUsed, isReturned);
                notUsedTickets.add(ticket);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка в методе readNotUsedTicket");
        }
        return notUsedTickets;
    }

    @Override
    public boolean idCheck(Long id) {
        try (Connection connection = ConnectionManager.open()) {
            PreparedStatement statement = connection.prepareStatement("SELECT id FROM Ticket");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long currentId = resultSet.getLong("id");
                if (currentId.equals(id)) {
                    return true;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка в методе idCheck");
        }
        System.err.println("\nБилет с таким id не найден!");
        return false;
    }

    @Override
    public List<Ticket> readAllById(Long id) {
        List<Ticket> tickets = new ArrayList<>();
        try (Connection connection = ConnectionManager.open()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Ticket WHERE ownerId = ?");
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long ticketId = resultSet.getLong("id");
                Long ownerId = resultSet.getLong("ownerId");
                Long filmId = resultSet.getLong("filmId");
                Integer price = resultSet.getInt("price");
                Boolean isUsed = resultSet.getBoolean("isUsed");
                Boolean isReturned = resultSet.getBoolean("isReturned");
                Ticket ticket = new Ticket(ticketId, ownerId, filmId, price, isUsed, isReturned);
                tickets.add(ticket);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка в методе readAllById");
        }
        return tickets;
    }

    @Override
    public List<Ticket> readUsedTicket(Long id) {
        List<Ticket> tickets = new ArrayList<>();
        try (Connection connection = ConnectionManager.open()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Ticket WHERE ownerId = ? AND isUsed = 1");
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long ticketId = resultSet.getLong("id");
                Long ownerId = resultSet.getLong("ownerId");
                Long filmId = resultSet.getLong("filmId");
                Integer price = resultSet.getInt("price");
                Boolean isUsed = resultSet.getBoolean("isUsed");
                Boolean isReturned = resultSet.getBoolean("isReturned");
                Ticket ticket = new Ticket(ticketId, ownerId, filmId, price, isUsed, isReturned);
                tickets.add(ticket);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка в методе readUsedTicket");
        }
        return tickets;
    }
}
