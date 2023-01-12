package com.java.model;

import java.util.Objects;

public class Ticket {

    private Long id;
    private Long owner_id;
    private Long filmId;
    private Integer price;
    private Boolean isUsed;
    private Boolean isReturned;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOwner() {
        return owner_id;
    }

    public void setOwnerId(Long person_id) {
        this.owner_id = person_id;
    }

    public Long getCurrentFilm() {
        return filmId;
    }

    public void setCurrentFilm(Long filmId) {
        this.filmId = filmId;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Boolean getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(Boolean isUsed) {
        this.isUsed = isUsed;
    }

    public Boolean getReturned() {
        return isReturned;
    }

    public void setIsReturned(Boolean returned) {
        isReturned = returned;
    }

    @Override
    public String toString() {
        return "Id билета: " + id + "\n" +
                "Id пользователя : " + owner_id + "\n" +
                "Id фильма : " + filmId + "\n" +
                "Цена билета : " + price + "\n" +
                "Использован ли билет: " + isUsed + "\n" +
                "Возвращен ли билет : " + isReturned + "\n";
    }

    public Ticket(Long id, Long owner_id, Long filmId, Integer price, Boolean isUsed, Boolean isReturned) {
        this.id = id;
        this.owner_id = owner_id;
        this.filmId = filmId;
        this.price = price;
        this.isUsed = isUsed;
        this.isReturned = isReturned;
    }

    public Ticket() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return Objects.equals(id, ticket.id) && Objects.equals(owner_id, ticket.owner_id) && Objects.equals(filmId, ticket.filmId) && Objects.equals(price, ticket.price) && Objects.equals(isUsed, ticket.isUsed) && Objects.equals(isReturned, ticket.isReturned);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, owner_id, filmId, price, isUsed, isReturned);
    }
}
