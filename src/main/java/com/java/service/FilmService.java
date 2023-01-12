package com.java.service;

import com.java.model.Film;
import java.util.List;

public interface FilmService {

    Film create(Film film);

    Film read(Long id);

    Film update(Film film);

    boolean delete(Long id);

    boolean idCheck(Long id);

    List<Film> readAll();
}
