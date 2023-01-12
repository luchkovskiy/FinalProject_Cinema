package com.java.controller;

import com.java.model.Film;
import com.java.model.Person;
import com.java.model.Ticket;
import com.java.service.*;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.Scanner;

@Slf4j
public class PersonController {

    private final Scanner scanner = new Scanner(System.in);
    private final TicketService ticketService = new TicketServiceImpl();
    private final PersonService personService = new PersonServiceImpl();
    private final FilmService filmService = new FilmServiceImpl();

    public void startUserController(Person person) {
        log.info("Пользователь " + person.getUsername() + " в главном меню аккаунта");
        System.out.println("""
                \nВыберите список действий для доступных вам уровней доступа:
                1 - список действий пользователя
                                
                0 - выйти из аккаунта
                -1 - выйти из приложения
                """);
        String action = scanner.next();
        switch (action) {
            case "-1" -> exit();
            case "0" -> accountExit();
            case "1" -> startUserActionList(person);
            default -> {
                log.error("Пользователь ввел неверные данные.");
                System.err.println("\nВы ввели недействительное значение!");
                startUserController(person);
            }
        }
    }

    public void startManagerController(Person person) {
        log.info("Пользователь " + person.getUsername() + " в главном меню аккаунта");
        System.out.println("""
                \nВыберите список действий для доступных вам уровней доступа:
                1 - список действий пользователя
                2 - список действий менеджера
                                
                0 - выйти из аккаунта
                -1 - выйти из приложения
                """);
        String action = scanner.next();
        switch (action) {
            case "-1" -> exit();
            case "0" -> accountExit();
            case "1" -> startUserActionList(person);
            case "2" -> startManagerActionList(person);
            default -> {
                log.error("Пользователь ввел неверные данные.");
                System.err.println("\nВы ввели недействительное значение!");
                startManagerController(person);
            }
        }
    }

    public void startAdminController(Person person) {
        log.info("Пользователь " + person.getUsername() + " в главном меню аккаунта");
        System.out.println("""
                \nВыберите список действий для доступных вам уровней доступа:
                1 - список действий пользователя
                2 - список действий менеджера
                3 - список действий админа
                                
                0 - выйти из аккаунта
                -1 - выйти из приложения
                """);
        String action = scanner.next();
        switch (action) {
            case "-1" -> exit();
            case "0" -> accountExit();
            case "1" -> startUserActionList(person);
            case "2" -> startManagerActionList(person);
            case "3" -> startAdminActionList(person);
            default -> {
                log.error("Пользователь ввел неверные данные.");
                System.err.println("\nВы ввели недействительное значение!");
                startAdminController(person);
            }
        }
    }

    public void startUserActionList(Person person) {
        FilmController film = new FilmController();
        TicketController ticket = new TicketController();
        System.out.println("""
                \nВыберите действие:
                1 - Действия с фильмами
                2 - Действия с билетами
                3 - Действия с аккаунтом
                                
                0 - назад
                """);
        String action = scanner.next();
        switch (action) {
            case "1" -> film.startUserFilmController(person);
            case "2" -> ticket.startUserTicketController(person);
            case "3" -> startAccountActionList(person);
            case "0" -> {
                if (person.getStatus().equals("USER")) {
                    startUserController(person);
                } else if (person.getStatus().equals("MANAGER")) {
                    startManagerController(person);
                } else {
                    startAdminController(person);
                }
            }
            default -> {
                log.error("Пользователь ввел неверные данные.");
                System.err.println("\nВы ввели недействительное значение!");
                startUserActionList(person);
            }
        }
    }

    public void startManagerActionList(Person person) {
        FilmController film = new FilmController();
        TicketController ticket = new TicketController();
        System.out.println("""
                \nВыберите действие:
                1 - Действия с фильмами
                2 - Действия с билетами
                                
                0 - назад
                """);
        String action = scanner.next();
        switch (action) {
            case "1" -> film.startManagerFilmController(person);
            case "2" -> ticket.startManagerTicketController(person);
            case "0" -> {
                if (person.getStatus().equals("MANAGER")) {
                    startManagerController(person);
                } else {
                    startAdminController(person);
                }
            }
            default -> {
                log.error("Пользователь ввел неверные данные.");
                System.err.println("Вы ввели недействительное значение!");
                startManagerActionList(person);
            }
        }
    }

    public void startAdminActionList(Person person) {
        FilmController film = new FilmController();
        System.out.println("""
                \nВыберите действие:
                1 - Действия с пользователями
                2 - Действия с фильмами
                                
                0 - назад
                """);
        String action = scanner.next();
        switch (action) {
            case "1" -> startAdminPersonActionList(person);
            case "2" -> film.startAdminFilmController(person);
            case "0" -> startAdminController(person);
            default -> {
                log.error("Пользователь ввел неверные данные.");
                System.err.println("Вы ввели недействительное значение!");
                startAdminActionList(person);
            }
        }
    }

    public boolean cashValidCheck(String cash) {
        try {
            int intCash = Integer.parseInt(cash);
            if (intCash < 1 || intCash > 10000) {
                log.error("Пользователь ввел неверные данные.");
                System.err.println("\nМинимальное вводимое значение - 1, максимальное - 10000!\n");
                return false;
            }
        } catch (Exception e) {
            log.error("Пользователь ввел неверные данные.");
            System.err.println("\nВы ввели нецелочисленное значение денежных средств!");
            return false;
        }
        return true;
    }

    private void startAdminPersonActionList(Person person) {
        SecurityController securityController = new SecurityController();
        System.out.println("""
                \nВыберите действие:
                1 - создать пользователя
                2 - изменить данные пользователя
                3 - удалить пользователя
                4 - посмотреть данные о пользователе
                5 - посмотреть данные о всех пользователях
                                
                0 - назад
                """);
        String action = scanner.next();
        switch (action) {
            case "0" -> startAdminActionList(person);
            case "1" -> {
                Person regPerson = securityController.setRegistrationData(new Person());
                personService.create(regPerson);
                log.info("Пользователь " + person.getUsername() + " создал нового пользователя: " + regPerson.getUsername());
                startAdminPersonActionList(person);
            }
            case "2" -> {
                String id = getStringId();
                if (!idCheck(id))
                    startAdminPersonActionList(person);
                log.info("Пользователь " + person.getUsername() + " изменил данные пользователя по id: " + id);
                changeAccountData(Long.parseLong(id), person);
            }
            case "3" -> {
                String id = getStringId();
                if (!idCheck(id))
                    startAdminPersonActionList(person);
                if (Long.parseLong(id) == 1) {
                    System.err.println("\nВы не можете удалить пользователя со статусом ADMIN");
                    startAdminPersonActionList(person);
                }
                if (Long.parseLong(id) == 2) {
                    System.err.println("\nВы не можете удалить пользователя со статусом MANAGER");
                    startAdminPersonActionList(person);
                }
                personService.delete(Long.parseLong(id));
                ticketService.delete(Long.parseLong(id));
                log.info("Пользователь " + person.getUsername() + " удалил пользователя по id: " + id);
                startAdminPersonActionList(person);
            }
            case "4" -> {
                String id = getStringId();
                if (!idCheck(id))
                    startAdminPersonActionList(person);
                System.out.println(personService.read(Long.parseLong(id)));
                log.info("Пользователь " + person.getUsername() + " просмотрел данные пользователя по id: " + id);
                startAdminPersonActionList(person);
            }
            case "5" -> {
                personService.getAllPersons().forEach(System.out::println);
                log.info("Пользователь " + person.getUsername() + " просмотрел данные всех пользователей");
                startAdminPersonActionList(person);
            }
            default -> {
                log.error("Пользователь ввел неверные данные.");
                System.err.println("Вы ввели недействительное значение!");
                startAdminPersonActionList(person);
            }
        }
    }

    private String getStringId() {
        System.out.println("Введите id пользователя:");
        return scanner.next();
    }

    private boolean idCheck(String id) {
        try {
            Long.parseLong(id);
        } catch (Exception e) {
            log.error("Пользователь ввел неверные данные.");
            System.err.println("\nВы ввели нецелочисленное значение id!");
            return false;
        }
        return personService.idCheck(Long.parseLong(id));
    }

    private void startAccountActionList(Person person) {
        System.out.println("""
                \nВыберите действие:
                1 - информация об аккаунте
                2 - изменить данные аккаунта
                3 - история купленных билетов
                4 - история просмотра фильмов
                5 - добавить денежные средства на счет
                6 - удалить аккаунт
                                        
                0 - назад
                """);
        String action = scanner.next();
        switch (action) {
            case "0" -> startUserActionList(person);
            case "1" -> {
                System.out.println(person);
                log.info("Пользователь " + person.getUsername() + " просмотрел данные о своем аккаунте");
                startAccountActionList(person);
            }
            case "2" -> {
                Person newPerson = changeAccountData(person);
                log.info("Пользователь " + person.getUsername() + " изменил данные своего аккаунта");
                startAccountActionList(newPerson);
            }
            case "3" -> {
                if (ticketService.readAllById(person.getId()).isEmpty()) {
                    System.out.println("\nСписок купленных билетов пуст!");
                    log.info("Пользователь " + person.getUsername() + " просмотрел историю купленных билетов");
                    startAccountActionList(person);
                }
                ticketService.readAllById(person.getId()).forEach(System.out::println);
                startAccountActionList(person);
            }
            case "4" -> {
                List<Ticket> tickets = ticketService.readUsedTicket(person.getId());
                if (tickets.isEmpty()) {
                    System.out.println("\nИстория просмотра фильмов пуста!");
                    log.info("Пользователь " + person.getUsername() + " открыл историю просмотра фильмов");
                    startAccountActionList(person);
                }
                for (Ticket ticket : tickets) {
                    Film film = filmService.read(ticket.getCurrentFilm());
                    System.out.println("Id билета: " + ticket.getId() + "\n" +
                            "Название фильма: " + film.getName() + "\n" +
                            "Цена билета: " + ticket.getPrice() + "\n");
                }
                startAccountActionList(person);
            }
            case "5" -> {
                System.out.println("Введите значение денежных средств:");
                String cash = scanner.next();
                if (!cashValidCheck(cash)) {
                    startAccountActionList(person);
                }
                person.setCash(Integer.parseInt(cash));
                personService.updateCash(person);
                log.info("Пользователь " + person.getUsername() + " добавил денежные средства на счет");
                startAccountActionList(person);
            }
            case "6" -> {
                boolean isSystem = personService.delete(person);
                if (!isSystem) {
                    startAccountActionList(person);
                }
                ticketService.delete(person.getId());
                log.info("Пользователь " + person.getUsername() + " удалил свой аккаунт");
                accountExit();
            }
            default -> {
                log.error("Пользователь ввел неверные данные.");
                System.err.println("\nВы ввели недействительное значение!");
                startAccountActionList(person);
            }
        }
    }

    private Person changeAccountData(Person person) {
        SecurityController securityController = new SecurityController();
        System.out.println("""
                \nВыберите изменяемый параметр:
                1 - Имя пользователя
                2 - Логин
                3 - Пароль
                                        
                0 - назад
                """);
        String action = scanner.next();
        switch (action) {
            case "0" -> startAccountActionList(person);
            case "1" -> {
                securityController.setUsername(person);
                personService.update(person);
                changeAccountData(person);
            }
            case "2" -> {
                securityController.setLogin(person);
                personService.update(person);
                changeAccountData(person);
            }
            case "3" -> {
                securityController.setPassword(person);
                personService.update(person);
                changeAccountData(person);
            }
            default -> {
                log.error("Пользователь ввел неверные данные.");
                System.err.println("\nВы ввели недействительное значение!");
                changeAccountData(person);
            }
        }
        return person;
    }

    private void changeAccountData(Long id, Person person) {
        SecurityController securityController = new SecurityController();
        Person upPerson = new Person();
        upPerson.setId(id);
        System.out.println("""
                \nВыберите изменяемый параметр:
                1 - Имя пользователя
                2 - Логин
                3 - Пароль
                4 - Денежные средства
                                        
                0 - назад
                """);
        String action = scanner.next();
        switch (action) {
            case "0" -> startAdminPersonActionList(person);
            case "1" -> {
                securityController.setUsername(upPerson);
                personService.update(id, upPerson);
                changeAccountData(id, person);
            }
            case "2" -> {
                securityController.setLogin(upPerson);
                personService.update(id, upPerson);
                changeAccountData(id, person);
            }
            case "3" -> {
                securityController.setPassword(upPerson);
                personService.update(id, upPerson);
                changeAccountData(id, person);
            }
            case "4" -> {
                System.out.println("Введите значение денежных средств:");
                String cash = scanner.next();
                if (!cashValidCheck(cash))
                    changeAccountData(id, person);
                upPerson.setCash(Integer.parseInt(cash));
                personService.update(id, upPerson);
                changeAccountData(id, person);
            }
            default -> {
                log.error("Пользователь ввел неверные данные.");
                System.err.println("Вы ввели недействительное значение!");
                changeAccountData(id, person);
            }
        }
    }

    private void accountExit() {
        SecurityController securityController = new SecurityController();
        System.out.println("\nВы вышли со своего аккаунта!\n");
        log.info("Пользователь вышел со своего аккаунта");
        securityController.start();
    }

    private static void exit() {
        System.out.println("До свидания!");
        log.info("Работа приложения прекращена");
        System.exit(0);
    }
}
