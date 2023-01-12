package com.java.controller;

import com.java.model.Film;
import com.java.model.Person;
import com.java.model.Ticket;
import com.java.service.*;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDate;
import java.util.Scanner;

@Slf4j
public class TicketController {

    private final Scanner scanner = new Scanner(System.in);
    private final FilmService filmService = new FilmServiceImpl();
    private final PersonService personService = new PersonServiceImpl();
    private final TicketService ticketService = new TicketServiceImpl();

    public void startUserTicketController(Person person) {
        PersonController personController = new PersonController();
        System.out.println("""
                \nВыберите действие:
                1 - Купить билет на фильм
                2 - Вернуть билет на фильм
                3 - Посмотреть активные билеты
                                
                0 - назад
                """);
        String action = scanner.next();
        switch (action) {
            case "0" -> personController.startUserActionList(person);
            case "1" -> {
                buyTicket(person);
                startUserTicketController(person);
            }
            case "2" -> {
                returnTicket(person);
                startUserTicketController(person);
            }
            case "3" -> {
                if (ticketService.readNotUsedTicket(person).isEmpty()) {
                    System.out.println("\nСписок активных билетов пуст!");
                    startUserTicketController(person);
                }
                ticketService.readNotUsedTicket(person).forEach(System.out::println);
                log.info("Пользователь " + person.getUsername() + " посмотрел информацию об активных билетах");
                startUserTicketController(person);
            }
            default -> {
                log.error("Пользователь ввел неверные данные.");
                System.err.println("\nВы ввели недействительное значение!");
                startUserTicketController(person);
            }
        }
    }

    public void startManagerTicketController(Person person) {
        PersonController personController = new PersonController();
        System.out.println("""
                \nВыберите действие:
                1 - Купить билет для пользователя
                2 - Вернуть билет пользователя
                                
                0 - назад
                """);
        String action = scanner.next();
        switch (action) {
            case "0" -> personController.startManagerActionList(person);
            case "1" -> {
                Person currentPerson = selectPerson(person);
                buyTicket(currentPerson);
                log.info("Пользователь " + person.getUsername() + " купил билет пользователю " + currentPerson.getUsername());
                startManagerTicketController(person);
            }
            case "2" -> {
                Person currentPerson = selectPerson(person);
                returnTicket(currentPerson);
                log.info("Пользователь " + person.getUsername() + " вернул билет пользователю " + currentPerson.getUsername());
                startManagerTicketController(person);
            }
            default -> {
                log.error("Пользователь ввел неверные данные.");
                System.err.println("Вы ввели недействительное значение!");
                startManagerTicketController(person);
            }
        }
    }

    private Person selectPerson(Person person) {
        System.out.println("Введите id пользователя:");
        String strId = scanner.next();
        if (!userIdCheck(strId))
            startManagerTicketController(person);
        long userId = Long.parseLong(strId);
        if (!personService.idCheck(userId))
            startManagerTicketController(person);
        return personService.read(userId);
    }

    private void buyTicket(Person person) {
        System.out.println("Введите id фильма:");
        String id = scanner.next();
        if (!filmIdCheck(id))
            return;
        Film film = filmService.read(Long.parseLong(id));
        LocalDate localDate = LocalDate.now();
        if (localDate.isAfter(film.getSession())) {
            log.error("Пользователь безуспешно попытался купить билет (ха-ха).");
            System.err.println("Показ фильма уже остановлен, невозможно купить билет!");
            return;
        }
        if (person.getCash() < film.getPrice()) {
            log.error("Пользователь безуспешно попытался купить билет (ха-ха).");
            System.err.println("\nНедостаточно средств на балансе аккаунта!");
            return;
        }
        if ((film.getCurrentTicketCount() - 1) < 0) {
            log.error("Пользователь безуспешно попытался купить билет (ха-ха).");
            System.err.println("На данный фильм закончились билеты!");
            return;
        }
        Integer newCash = person.getCash() - film.getPrice();
        person.setCash(newCash);
        personService.updateCashByTicketPrice(person);
        film.setCurrentTicketCount(film.getCurrentTicketCount() - 1);
        filmService.update(film);
        Ticket ticket = new Ticket();
        ticket.setOwnerId(person.getId());
        ticket.setCurrentFilm(film.getId());
        ticket.setPrice(film.getPrice());
        ticket.setIsUsed(false);
        ticket.setIsReturned(false);
        ticketService.buyTicket(ticket);
        log.info("Пользователь " + person.getUsername() + " купил билет на фильм " + film.getName());
    }

    private boolean filmIdCheck(String id) {
        try {
            Long.parseLong(id);
        } catch (Exception e) {
            log.error("Пользователь ввел неверные данные.");
            System.err.println("\nВы ввели недействительное значение id фильма!");
            return false;
        }
        return filmService.idCheck(Long.parseLong(id));
    }

    private void returnTicket(Person person) {
        System.out.println("Введите Id билета:");
        String ticketId = scanner.next();
        if (!ticketIdCheck(ticketId))
            return;
        Ticket ticket = ticketService.read(Long.parseLong(ticketId));
        if (ticket.getReturned()) {
            System.err.println("\nБилет уже возвращен!");
            return;
        }
        if (ticket.getIsUsed()) {
            System.err.println("\nНевозможно вернуть использованный билет!");
            return;
        }
        person.addCash(ticket.getPrice());
        personService.updateCashByTicketPrice(person);
        Film film = filmService.read(ticket.getCurrentFilm());
        LocalDate localDate = LocalDate.now();
        if (localDate.isAfter(film.getSession())) {
            System.err.println("Невозможно вернуть билет после приостановки показа фильма!");
            startUserTicketController(person);
        }
        film.setCurrentTicketCount(film.getCurrentTicketCount() + 1);
        filmService.update(film);
        ticket.setIsReturned(true);
        ticketService.update(ticket);
        log.info("Пользователь " + person.getUsername() + " вернул билет по id  " + ticketId);
    }

    private boolean ticketIdCheck(String id) {
        try {
            Long.parseLong(id);
        } catch (Exception e) {
            log.error("Пользователь ввел неверные данные.");
            System.err.println("\nВы ввели недействительное значение id фильма!");
            return false;
        }
        return ticketService.idCheck(Long.parseLong(id));
    }

    private boolean userIdCheck(String id) {
        try {
            Long.parseLong(id);
        } catch (Exception e) {
            log.error("Пользователь ввел неверные данные.");
            System.err.println("\nВы ввели недействительное значение id пользователя!");
            return false;
        }
        return true;
    }
}
