package com.skyairlines.dao.api;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface GenericDAO<T, ID> {
    Optional<T> findById(ID id) throws SQLException;
    List<T> findAll() throws SQLException;
    T save(T entity) throws SQLException;
    T update(T entity) throws SQLException;
    boolean deleteById(ID id) throws SQLException;
    boolean existsById(ID id) throws SQLException;
    long count() throws SQLException;
}