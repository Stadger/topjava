package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import java.time.LocalDateTime;
import java.util.List;

public interface MealDao {
    Meal get(long id);

    List<Meal> getAll();

    void create(Meal meal);

    void update(Meal meal);

    void delete(long id);
}
