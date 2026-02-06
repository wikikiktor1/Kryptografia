package org.example.model;

import java.util.List;

public interface Dao<T> extends AutoCloseable {
    T read(String name) throws WrongInputException;

    void write(String name, T obj) throws WrongInputException;

    List<String> names() throws WrongInputException;
}
