package com.scottlogic.matchingengine.dao;

import java.util.List;
import java.util.Optional;

public interface DAO<T> {
     List<T> list();

     void create(T t);

     Optional<T> read(String id);

     void update(T t, String id);

     void delete(int id);
}
