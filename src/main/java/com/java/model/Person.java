package com.java.model;

public class Person {
    private Long id;
    private String username;
    private String login;
    private String password;
    private String status;
    private Integer cash = 0;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCash() {
        return cash;
    }

    public void setCash(Integer newCash) {
        cash = newCash;
    }

    public void addCash(Integer count) {
        cash = cash + count;
    }

    public Person(Long id, String username, String login, String password, Integer cash, String status) {
        this.id = id;
        this.username = username;
        this.login = login;
        this.password = password;
        this.status = status;
        this.cash = cash;
    }

    public Person() {
    }

    @Override
    public String toString() {
        return "\nID пользователя: " + id + "\n"
                + "Username пользователя: " + username + "\n"
                + "Status пользователя: " + status + "\n"
                + "Login пользователя: " + login + "\n"
                + "Текущий денежный счет: " + cash + "\n";
    }
}
