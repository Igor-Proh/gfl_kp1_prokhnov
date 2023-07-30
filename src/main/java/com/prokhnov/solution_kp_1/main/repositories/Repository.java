package com.prokhnov.solution_kp_1.main.repositories;

import com.prokhnov.solution_kp_1.main.exceptions.ItemNotFoundException;

import java.util.List;

public interface Repository<T> {
    void add(T t);

    void edit(T t) throws ItemNotFoundException;

    void delete(String name) throws ItemNotFoundException;

    void loadDataFromFile();

    void saveDataToFile();

    List<T> getListOfAllData();
}
