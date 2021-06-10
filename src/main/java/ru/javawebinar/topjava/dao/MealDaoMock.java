package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class MealDaoMock implements MealDao {
    private final AtomicLong idCount = new AtomicLong(0);
    private final Map<Long, Meal> data = new ConcurrentHashMap<>();

    public MealDaoMock() {
        create(new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
        create(new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
        create(new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500));
        create(new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
        create(new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
        create(new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
        create(new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
    }

    @Override
    public Meal get(long id) {
        return data.get(id);
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Meal create(Meal meal) {
        if (meal.getId() == null) {
            long id = this.idCount.incrementAndGet();
            meal = new Meal(id, meal.getDateTime(), meal.getDescription(), meal.getCalories());
            data.put(id, meal);
            return meal;
        } else {
            return null;
        }
    }

    @Override
    public Meal update(Meal meal) {
        return data.computeIfPresent(meal.getId(), (key, val) -> meal);
    }

    @Override
    public void delete(long id) {
        data.remove(id);
    }

}
