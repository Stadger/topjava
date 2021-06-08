package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class MealMock {
    /* объект с данными
    реализует синглтон
    cjplftncz d
     */
    private volatile AtomicLong id = new AtomicLong(0);
    private final List<Meal> list = Collections.synchronizedList(new LinkedList<>());
    private final int caloriesPerDay = 2000;

    private static volatile MealMock instance;

    public static MealMock getInstance() {
        MealMock localInstance = instance;
        if (localInstance == null) {
            synchronized (MealMock.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new MealMock();
                }
            }
        }
        return localInstance;
    }

    public int getCaloriesPerDay() {
        return this.caloriesPerDay;
    }

    public List<Meal> getList() {
        return this.list;
    }

    private MealMock() {
        list.addAll(Arrays.asList(
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500, id.incrementAndGet()),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000, id.incrementAndGet()),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500, id.incrementAndGet()),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100, id.incrementAndGet()),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000, id.incrementAndGet()),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500, id.incrementAndGet()),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410, id.incrementAndGet())
    ));
    }

    public void add(LocalDateTime dateTime, String description, int calories) {
        list.add(
                new Meal(dateTime, description, calories, id.incrementAndGet())
        );
    }
}
