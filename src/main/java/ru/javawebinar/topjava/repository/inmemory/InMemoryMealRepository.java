package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);
    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals1().forEach(meal -> save(meal, 1));
        MealsUtil.meals2().forEach(meal -> save(meal, 2));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        log.info("save {} userId{}", meal, userId);
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userId);
            repository.computeIfAbsent(userId, k -> new HashMap<>()).put(meal.getId(), meal);
            return meal;
        }
        AtomicBoolean result = new AtomicBoolean(false);
        repository.computeIfPresent(userId, (idUser, map) -> {
            if (map.computeIfPresent(meal.getId(), (id, oldMeal) -> meal) != null) {
                result.set(true);
            }
            return map;
        });
        return result.get() ? meal : null;
    }

    @Override
    public boolean delete(int id, int userId) {
        log.info("delete {} userId{}", id, userId);
        AtomicBoolean result = new AtomicBoolean(false);
        repository.computeIfPresent(userId, (idUser, map) -> {
            if (map.remove(id) != null) {
                result.set(true);
            }
            return map;
        });
        return result.get();
    }

    @Override
    public Meal get(int id, int userId) {
        log.info("get {} userId{}", id, userId);
        Map<Integer, Meal> map = repository.get(userId);
        return map != null ? map.get(id) : null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.info("getall {} ", userId);
        return filterByPredicate(userId, meal -> true);
    }

    @Override
    public List<Meal> getFilteredByDates(int userId, LocalDate startDate, LocalDate endDate) {
        log.info("getallFilterDate {} startTime{} endTime{} ", userId, startDate, endDate);
        return filterByPredicate(userId, meal -> DateTimeUtil.isBetweenClosed(meal.getDate(), startDate, endDate));
    }

    private List<Meal> filterByPredicate(int userId, Predicate<Meal> filter) {
        return repository
                .getOrDefault(userId, Collections.emptyMap())
                .values()
                .stream()
                .filter(filter)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }
}
