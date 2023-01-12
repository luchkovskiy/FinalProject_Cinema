package com.java.model;

import java.time.LocalDate;
import java.util.Objects;

public class Film {

    private Long id;
    private String name;
    private LocalDate sessionClosingDate;
    private Integer currentTicketCount;
    private Integer price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getSession() {
        return sessionClosingDate;
    }

    public void setSession(LocalDate sessionClosingDate) {
        this.sessionClosingDate = sessionClosingDate;
    }

    public Integer getCurrentTicketCount() {
        return currentTicketCount;
    }

    public void setCurrentTicketCount(Integer count) {
        currentTicketCount = count;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Film(Long id, String name, LocalDate session, Integer currentTicketCount, Integer price) {
        this.id = id;
        this.name = name;
        this.sessionClosingDate = session;
        this.currentTicketCount = currentTicketCount;
        this.price = price;
    }

    public Film() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Film film = (Film) o;
        return Objects.equals(id, film.id) && Objects.equals(name, film.name) && Objects.equals(sessionClosingDate, film.sessionClosingDate) && Objects.equals(currentTicketCount, film.currentTicketCount) && Objects.equals(price, film.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, sessionClosingDate, currentTicketCount, price);
    }

    @Override
    public String toString() {
        return "\nId фильма: " + id + "\n" +
                "Название фильма: " + name + "\n" +
                "Цена за просмотр: " + price + "\n" +
                "Дата прекращения показа: " + sessionClosingDate + "\n" +
                "Количество оставшихся билетов: " + currentTicketCount + "\n";
    }
}
