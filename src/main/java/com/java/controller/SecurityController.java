package com.java.controller;

import com.java.model.Person;
import com.java.model.PersonStatus;
import com.java.service.PersonService;
import com.java.service.PersonServiceImpl;
import lombok.extern.slf4j.Slf4j;
import java.util.Scanner;

@Slf4j
public class SecurityController {
    private final Scanner scanner = new Scanner(System.in);
    private final PersonService service = new PersonServiceImpl();
    private final PersonController controller = new PersonController();

    static {
        System.out.println("\nДобро пожаловать в приложение 'Онлайн-кинотеатр'!");
        log.info("Приложение запущено.");
    }

    public void start() {
        System.out.println("""
                \nВыберите действие:
                1 - войти в систему
                2 - зарегистрироваться
                                
                0 - выйти из приложения
                """);
        String action = scanner.next();
        switch (action) {
            case "0" -> exit();
            case "1" -> entrance();
            case "2" -> registration();
            default -> {
                log.error("Пользователь ввел неверные данные.");
                System.err.println("Вы ввели недействительное значение!");
                start();
            }
        }
    }

    public Person setRegistrationData(Person person) {
        log.info("Регистрация пользователя.");
        setLogin(person);
        setPassword(person);
        setUsername(person);
        setStatus(person);
        return person;
    }

    public void setUsername(Person person) {
        System.out.println("Придумайте имя пользователя:");
        String username = scanner.next();
        boolean isValid = service.checkUsernameValid(username);
        if (!isValid)
            setUsername(person);
        else {
            person.setUsername(username);
        }
    }

    public void setLogin(Person person) {
        System.out.println("Введите логин:");
        String login = scanner.next();
        boolean isValid = service.checkLoginValid(login);
        if (!isValid)
            setLogin(person);
        else
            person.setLogin(login);
    }

    public void setPassword(Person person) {
        System.out.println("Введите пароль:");
        String strPassword = scanner.next();
        String password = String.valueOf(strPassword.hashCode());
        person.setPassword(password);
    }

    public void setEntranceLogin(Person person) {
        System.out.println("Введите логин:");
        String login = scanner.next();
        person.setLogin(login);
    }

    public void setStatus(Person person) {
        String status = String.valueOf(PersonStatus.USER);
        person.setStatus(status);
    }

    private void entrance() {
        log.info("Пользователь входит в систему.");
        Person entPerson = setEntranceData(new Person());
        Person person = service.entrance(entPerson);
        chooseRoleController(person);
    }

    private Person setEntranceData(Person person) {
        setEntranceLogin(person);
        setPassword(person);
        return person;
    }

    private void registration() {
        Person regPerson = setRegistrationData(new Person());
        Person person = service.create(regPerson);
        chooseRoleController(person);
    }

    private void exit() {
        System.out.println("\nДо свидания!");
        log.info("Работа приложения прекращена");
        System.exit(0);
    }

    private void chooseRoleController(Person person) {
        switch (person.getStatus()) {
            case "USER" -> controller.startUserController(person);
            case "MANAGER" -> controller.startManagerController(person);
            case "ADMIN" -> controller.startAdminController(person);
            default -> System.out.println("Ошибка в выборе роли");
        }
    }
}
