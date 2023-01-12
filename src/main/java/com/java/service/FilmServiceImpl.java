package com.java.service;

import com.java.model.Film;
import com.java.repository.FilmRepository;
import com.java.repository.FilmRepositoryImpl;
import com.java.repository.Repository;
import java.util.List;

public class FilmServiceImpl implements FilmService {

    private final Repository<Film> repository = new FilmRepositoryImpl();

    private final FilmRepository filmRepository = new FilmRepositoryImpl();

    @Override
    public Film create(Film film) {
        return repository.create(film);
    }

    @Override
    public Film read(Long id) {
        return repository.read(id);
    }

    @Override
    public Film update(Film film) {
        return repository.update(film);
    }

    @Override
    public boolean delete(Long id) {
        return repository.delete(id);
    }

    @Override
    public boolean idCheck(Long id) {
        return filmRepository.idCheck(id);
    }

    @Override
    public List<Film> readAll() {
        return filmRepository.readAll();
    }
}
