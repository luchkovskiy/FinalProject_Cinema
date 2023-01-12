package com.java.repository;

import com.java.model.Film;
import java.util.List;

public interface FilmRepository extends Repository<Film> {

    boolean idCheck(Long id);

    List<Film> readAll();

    @Override
    default boolean delete(Film film) {
        return false;
    }
}
