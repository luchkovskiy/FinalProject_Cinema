package com.java.repository;

import com.java.model.Film;
import com.java.util.ConnectionManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FilmRepositoryImpl implements FilmRepository {

    @Override
    public Film create(Film film) {
        try (Connection connection = ConnectionManager.open()) {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO Film (name, sessionClosingDate, remainingTickets, price) VALUES (?,?,?,?)");
            statement.setString(1, film.getName());
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String date = film.getSession().format(dateTimeFormatter);
            statement.setString(2, date);
            statement.setInt(3, film.getCurrentTicketCount());
            statement.setInt(4, film.getPrice());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка в методе getPersonById");
        }
        System.out.println("\nФильм успешно создан!\n");
        return film;
    }

    @Override
    public Film read(Long id) {
        Film film = new Film();
        try (Connection connection = ConnectionManager.open()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Film WHERE id = ?");
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                film.setId(resultSet.getLong("id"));
                film.setName(resultSet.getString("name"));
                String date = resultSet.getString("sessionClosingDate");
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate localDate = LocalDate.parse(date, dateTimeFormatter);
                film.setSession(localDate);
                film.setCurrentTicketCount(resultSet.getInt("remainingTickets"));
                film.setPrice(resultSet.getInt("price"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка в методе read");
        }
        return film;
    }

    @Override
    public Film update(Film newT) {
        try (Connection connection = ConnectionManager.open()) {
            PreparedStatement statement = connection.prepareStatement("UPDATE Film SET name = ?," +
                    " sessionClosingDate = ?, remainingTickets = ?, price = ? WHERE id = ?");
            statement.setString(1, newT.getName());
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String date = newT.getSession().format(dateTimeFormatter);
            statement.setString(2, date);
            statement.setInt(3, newT.getCurrentTicketCount());
            statement.setInt(4, newT.getPrice());
            statement.setLong(5, newT.getId());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка в методе update");
        }
        System.out.println("\nДанные успешно изменены!");
        return newT;
    }

    @Override
    public boolean delete(Long id) {
        try (Connection connection = ConnectionManager.open()) {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM Film WHERE id = ?");
            statement.setLong(1, id);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка в методе delete");
        }
        System.out.println("\nФильм успешно удален!");
        return true;
    }

    @Override
    public boolean idCheck(Long id) {
        boolean isIdExist = false;
        try (Connection connection = ConnectionManager.open()) {
            PreparedStatement statement = connection.prepareStatement("SELECT id FROM Film");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long currentId = resultSet.getLong("id");
                if (id.equals(currentId)) {
                    isIdExist = true;
                }
            }
            if (!isIdExist) {
                System.err.println("\nФильм с таким id не найден!");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка в методе idCheck");
        }
        return isIdExist;
    }

    @Override
    public List<Film> readAll() {
        List<Film> allFilms = new ArrayList<>();
        try (Connection connection = ConnectionManager.open()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Film");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String date = resultSet.getString("sessionClosingDate");
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate localDate = LocalDate.parse(date, dateTimeFormatter);
                Integer ticket = resultSet.getInt("remainingTickets");
                Integer price = resultSet.getInt("price");
                Film film = new Film(id, name, localDate, ticket, price);
                allFilms.add(film);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка в методе readAll");
        }
        return allFilms;
    }
}
