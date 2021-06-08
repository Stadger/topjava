package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import java.time.LocalDateTime;
import java.util.List;

public interface MealDao {
    Meal get(Long id);

    List<MealTo> getAll();

    void save(LocalDateTime dateTime, String description, int calories);

    void update(long id, LocalDateTime dateTime, String description, int calories);

    void delete(Long id);
}
