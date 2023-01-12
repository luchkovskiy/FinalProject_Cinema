package com.java.repository;

public interface Repository<T> {

    T create(T t);

    T read(Long id);

    T update(T newT);

    boolean delete(Long id);

    boolean delete(T t);

}
