package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class MealDaoMock implements MealDao {
    private volatile AtomicLong id = new AtomicLong(0);
    private final Map<Long,Meal> map = new ConcurrentHashMap<>();;


    public MealDaoMock() {
        add(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500);
        add(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000);
        add(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500);
        add(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100);
        add(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000);
        add(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500);
        add(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410);
    }

    public void add(LocalDateTime dateTime, String description, int calories) {
        long id = this.id.incrementAndGet();
        map.put(
                id,new Meal(id,dateTime, description, calories)
        );
    }

    @Override
    public Meal get(long id) {
        return map.get(id);
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<Meal>(map.values());
    }

    @Override
    public void create(Meal meal) {
        add(meal.getDateTime(),meal.getDescription(),meal.getCalories());
        //map.putIfAbsent(meal.getId(),meal);
    }

    @Override
    public void update(Meal meal) {
        map.computeIfPresent(meal.getId(),(key, val)  -> meal);
    }

    @Override
    public void delete(long id) {
        map.remove(id);
    }

}
