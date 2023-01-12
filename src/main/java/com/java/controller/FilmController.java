package com.java.controller;

import com.java.model.Film;
import com.java.model.Person;
import com.java.model.Ticket;
import com.java.service.FilmService;
import com.java.service.FilmServiceImpl;
import com.java.service.TicketService;
import com.java.service.TicketServiceImpl;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

@Slf4j
public class FilmController {

    private final Scanner scanner = new Scanner(System.in);
    private final FilmService filmService = new FilmServiceImpl();
    private final TicketService ticketService = new TicketServiceImpl();

    public void startUserFilmController(Person person) {
        PersonController personController = new PersonController();
        System.out.println("""
                \nВыберите действие:
                1 - Посмотреть фильм
                2 - Отобразить список доступных фильмов
                                
                0 - назад
                """);
        String action = scanner.next();
        switch (action) {
            case "0" -> personController.startUserActionList(person);
            case "1" -> {
                seeMovie(person);
                startUserFilmController(person);
            }
            case "2" -> {
                List<Film> films = filmService.readAll();
                for (Film film : films) {
                    if (film.getSession().isBefore(LocalDate.now())) {
                        continue;
                    }
                    System.out.println(film);
                }
                log.info("Пользователь " + person.getUsername() + " просмотрел список доступных фильмов");
                startUserFilmController(person);
            }
            default -> {
                log.error("Пользователь ввел неверные данные.");
                System.err.println("Вы ввели недействительное значение!");
                startUserFilmController(person);
            }
        }
    }

    public void startManagerFilmController(Person person) {
        PersonController personController = new PersonController();
        System.out.println("""
                \nВыберите действие:
                1 - Редактировать информацию о фильме
                2 - Отобразить список доступных фильмов
                                
                0 - назад
                """);
        String action = scanner.next();
        switch (action) {
            case "0" -> personController.startManagerActionList(person);
            case "1" -> {
                String id = getStringId();
                if (!idCheck(id))
                    startManagerFilmController(person);
                int number = 1;
                startFilmChanger(person, Long.parseLong(id), number);
                log.info("Пользователь " + person.getUsername() + " отредактировал информацию о фильме");
                startManagerFilmController(person);
            }
            case "2" -> {
                filmService.readAll().forEach(System.out::println);
                log.info("Пользователь " + person.getUsername() + " просмотрел список доступных фильмов");
                startManagerFilmController(person);
            }
            default -> {
                log.error("Пользователь ввел неверные данные.");
                System.err.println("Вы ввели недействительное значение!");
                startManagerFilmController(person);
            }
        }
    }

    public void startAdminFilmController(Person person) {
        PersonController personController = new PersonController();
        System.out.println("""
                \nВыберите действие:
                1 - Создать фильм
                2 - Редактировать информацию о фильме
                3 - Удалить фильм
                4 - Отобразить информацию о фильме
                5 - Отобразить список созданных фильмов
                                
                0 - назад
                """);
        String action = scanner.next();
        switch (action) {
            case "0" -> personController.startAdminActionList(person);
            case "1" -> {
                Film film = new Film();
                setName(film);
                boolean isSessionValid = setSession(film);
                if (!isSessionValid)
                    startAdminFilmController(person);
                boolean isCountValid = setCurrentTicketCount(film);
                if (!isCountValid)
                    startAdminFilmController(person);
                boolean isPriceValid = setPrice(film);
                if (!isPriceValid)
                    startAdminFilmController(person);
                filmService.create(film);
                log.info("Пользователь " + person.getUsername() + " создал фильм " + film.getName());
                startAdminFilmController(person);
            }
            case "2" -> {
                String id = getStringId();
                if (!idCheck(id))
                    startAdminFilmController(person);
                int number = 2;
                log.info("Пользователь " + person.getUsername() + " отредактировал информацию о фильме по id " + id);
                startFilmChanger(person, Long.parseLong(id), number);
            }
            case "3" -> {
                String id = getStringId();
                if (!idCheck(id))
                    startAdminFilmController(person);
                filmService.delete(Long.parseLong(id));
                log.info("Пользователь " + person.getUsername() + " удалил фильм по id " + id);
                startAdminFilmController(person);
            }
            case "4" -> {
                String id = getStringId();
                if (!idCheck(id))
                    startAdminFilmController(person);
                System.out.println(filmService.read(Long.parseLong(id)));
                log.info("Пользователь " + person.getUsername() + " отбразил информацию информацию о фильме по id " + id);
                startAdminFilmController(person);
            }
            case "5" -> {
                filmService.readAll().forEach(System.out::println);
                log.info("Пользователь " + person.getUsername() + " вывел информацию о всех доступных фильмах");
                startAdminFilmController(person);
            }
            default -> {
                log.error("Пользователь ввел неверные данные.");
                System.err.println("Вы ввели недействительное значение!");
                startAdminFilmController(person);
            }
        }
    }

    private String getStringId() {
        System.out.println("Введите id фильма:");
        return scanner.next();
    }

    public boolean setSession(Film film) {
        System.out.println("\n(info) Ввод данных о крайней дате показа фильма.");
        LocalDate nowDate = LocalDate.of(0, 1, 1);
        System.out.println("Введите год:");
        String strYear = scanner.next();
        if (!yearCheck(strYear))
            return false;
        int year = Integer.parseInt(strYear);
        int yearDifference = year - LocalDate.now().getYear();
        System.out.println("Введите порядковый номер месяца:");
        String strMonth = scanner.next();
        if (!monthCheck(strMonth))
            return false;
        int month = Integer.parseInt(strMonth);
        int monthDifference = month - LocalDate.now().getMonthValue();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -yearDifference);
        calendar.add(Calendar.MONTH, +monthDifference);
        int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        System.out.println("Введите число:");
        String strDate = scanner.next();
        if (!dateCheck(strDate))
            return false;
        int date = Integer.parseInt(strDate);
        if (date < 1 || date > maxDay) {
            log.error("Пользователь ввел неверные данные.");
            System.err.println("\nЧисло для выбранного месяца должно находиться в диапозоне от 1 до " + maxDay);
            return false;
        }
        nowDate = nowDate.plusYears(year);
        nowDate = nowDate.plusMonths(month - 1);
        nowDate = nowDate.plusDays(date - 1);
        film.setSession(nowDate);
        return true;
    }

    public void setName(Film film) {
        System.out.println("Введите название фильма:");
        String name = scanner.next();
        film.setName(name);
    }

    public boolean setCurrentTicketCount(Film film) {
        System.out.println("Введите доступное количество билетов на фильм:");
        String maxTicketCount = scanner.next();
        if (!checkInt(maxTicketCount))
            return false;
        if (Integer.parseInt(maxTicketCount) < 0) {
            log.error("Пользователь ввел неверные данные.");
            System.err.println("\nВы ввели отрицательное значение количества билетов!");
            return false;
        }
        film.setCurrentTicketCount(Integer.parseInt(maxTicketCount));
        return true;
    }

    public boolean setPrice(Film film) {
        System.out.println("Введите цену на билет:");
        String price = scanner.next();
        if (!checkInt(price))
            return false;
        if (Integer.parseInt(price) < 0) {
            log.error("Пользователь ввел неверные данные.");
            System.err.println("\nВы ввели отрицательное значение цены билета!");
            return false;
        }
        film.setPrice(Integer.parseInt(price));
        return true;
    }

    private void seeMovie(Person person) {
        System.out.println("Введите id фильма:");
        String id = scanner.next();
        if (!idCheck(id))
            startUserFilmController(person);
        Film film = filmService.read(Long.parseLong(id));
        if (LocalDate.now().isAfter(film.getSession())) {
            log.error("Пользователь безуспешно попытался посмотреть фильм (ха-ха).");
            System.err.println("Невозможно посмотреть фильм, показ уже завершен!");
            startUserFilmController(person);
        }
        List<Ticket> tickets = ticketService.readNotUsedTicket(person);
        if (tickets.isEmpty()) {
            log.error("Пользователь безуспешно попытался посмотреть фильм (ха-ха).");
            System.err.println("\nУ вас не куплен билет на данный фильм!\n");
            startUserFilmController(person);
        }
        Ticket currentTicket = new Ticket();
        for (Ticket ticket : tickets) {
            if (ticket.getCurrentFilm().equals(Long.parseLong(id))) {
                currentTicket.setId(ticket.getId());
                currentTicket.setOwnerId(ticket.getOwner());
                currentTicket.setCurrentFilm(ticket.getCurrentFilm());
                currentTicket.setPrice(ticket.getPrice());
                currentTicket.setIsUsed(true);
                currentTicket.setIsReturned(ticket.getReturned());
                break;
            }
        }
        ticketService.update(currentTicket);
        System.out.println("\nВы посмотрели фильм!\n");
        log.info("Пользователь " + person.getUsername() + " посмотрел фильм " + film.getName());
    }

    private boolean idCheck(String id) {
        try {
            Long.parseLong(id);
        } catch (Exception e) {
            log.error("Пользователь ввел неверные данные.");
            System.err.println("\nВы ввели недействительное значение id фильма!\n");
            return false;
        }
        return filmService.idCheck(Long.parseLong(id));
    }

    private void startFilmChanger(Person person, Long filmId, int number) {
        Film film = filmService.read(filmId);
        System.out.println("""
                \nВыберите изменяемый параметр:
                1 - Название
                2 - Дата последнего сеанса
                3 - Количество доступных билетов
                4 - Цена
                                
                0 - назад
                """);
        String action = scanner.next();
        switch (action) {
            case "0" -> {
                if (number == 1)
                    startManagerFilmController(person);
                else
                    startAdminFilmController(person);
            }
            case "1" -> {
                setName(film);
                filmService.update(film);
                startFilmChanger(person, filmId, number);
            }
            case "2" -> {
                boolean isValid = setSession(film);
                if (!isValid)
                    startFilmChanger(person, filmId, number);
                filmService.update(film);
                startFilmChanger(person, filmId, number);
            }
            case "3" -> {
                boolean isValid = setCurrentTicketCount(film);
                if (!isValid)
                    startFilmChanger(person, filmId, number);
                filmService.update(film);
                startFilmChanger(person, filmId, number);
            }
            case "4" -> {
                boolean isValid = setPrice(film);
                if (!isValid)
                    startFilmChanger(person, filmId, number);
                filmService.update(film);
                startFilmChanger(person, filmId, number);
            }
            default -> {
                log.error("Пользователь ввел неверные данные.");
                System.err.println("\nВы ввели недействительное значение!");
                startFilmChanger(person, filmId, number);
            }
        }
    }

    private boolean yearCheck(String year) {
        try {
            Integer.parseInt(year);
        } catch (Exception e) {
            log.error("Пользователь ввел неверные данные.");
            System.err.println("\nВы ввели недействительное значение года!\n");
            return false;
        }
        if (Integer.parseInt(year) < LocalDate.now().getYear()) {
            log.error("Пользователь ввел неверные данные.");
            System.err.println("\nЗначение года не может быть меньше текущего!\n");
            return false;
        }
        return true;
    }

    private boolean monthCheck(String month) {
        try {
            Integer.parseInt(month);
        } catch (Exception e) {
            log.error("Пользователь ввел неверные данные.");
            System.err.println("\nВы ввели недействительное значение месяца!\n");
            return false;
        }
        if (Integer.parseInt(month) < 1 || Integer.parseInt(month) > 12) {
            log.error("Пользователь ввел неверные данные.");
            System.err.println("\nЗначение месяца находится в диапозоне от 1 до 12!\n");
            return false;
        }
        return true;
    }

    private boolean dateCheck(String date) {
        try {
            Integer.parseInt(date);
        } catch (Exception e) {
            log.error("Пользователь ввел неверные данные.");
            System.err.println("\nВы ввели недействительное значение числа!\n");
            return false;
        }
        return true;
    }

    private boolean checkInt(String value) {
        try {
            Integer.parseInt(value);
        } catch (Exception e) {
            log.error("Пользователь ввел неверные данные.");
            System.err.println("\nВы ввели недействительное значение значение!\n");
            return false;
        }
        return true;
    }
}
