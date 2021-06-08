package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class MealDaoMock implements MealDao {
    private final MealMock dataMock = MealMock.getInstance();


    @Override
    public Meal get(Long id) {
        return dataMock.getList().stream().filter(e -> e.getId() == id).findFirst().orElse(null);
    }

    @Override
    public List<MealTo> getAll() {
        return MealsUtil.filteredByStreams(dataMock.getList(), LocalTime.MIN, LocalTime.MAX, dataMock.getCaloriesPerDay());
    }

    @Override
    public void save(LocalDateTime dateTime, String description, int calories) {
        dataMock.add(dateTime, description, calories);
    }

    @Override
    public void update(long id, LocalDateTime dateTime, String description, int calories) {
        delete(id);
        save(dateTime, description, calories);
    }

    @Override
    public void delete(Long id) {
        dataMock.getList().remove(get(id));
    }
}
