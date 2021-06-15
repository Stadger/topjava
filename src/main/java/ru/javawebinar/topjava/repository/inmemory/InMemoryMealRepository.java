package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepository.class);
    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> save(meal, 1));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        log.info("save {} userId{}", meal, userId);
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userId);
            repository.put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        AtomicBoolean result = new AtomicBoolean(false);
        repository.computeIfPresent(meal.getId(), (id, oldMeal) -> {
            if (oldMeal.getUserId() == userId && meal.getUserId() == userId) {
                result.set(true);
                return meal;
            } else return oldMeal;
        });
        return result.get() ? meal : null;
    }

    @Override
    public boolean delete(int id, int userId) {
        log.info("delete {} userId{}", id, userId);
        AtomicBoolean result = new AtomicBoolean(false);
        repository.computeIfPresent(id, (idMail, oldMeal) -> {
            if (oldMeal.getUserId() == userId) {
                result.set(true);
                return null;
            } else return oldMeal;
        });
        return result.get();
    }

    @Override
    public Meal get(int id, int userId) {
        log.info("get {} userId{}", id, userId);
        Meal meal;
        return ((meal = repository.get(id)) != null && meal.getUserId() == userId) ?
                meal : null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.info("getall {} ", userId);
        return repository.values().stream().filter(meal -> meal.getUserId() == userId)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<Meal> getFilterDate(int userId, LocalDate startDate, LocalDate endDate) {
        log.info("getallFilterDate {} startTime{} endTime{} ", userId, startDate, endDate);
        return getAll(userId).stream().filter(meal -> DateTimeUtil.isBetweenHalfOpen(meal.getDate(), startDate, endDate)).collect(Collectors.toList());
    }
}

