package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.inmemory.InMemoryUserRepository;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
public class MealRestController {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepository.class);

    @Autowired
    public MealRestController(MealService service) {
        this.service = service;
    }

    private MealService service;

    public List<MealTo> getAll() {
        log.info("getAll");
        return MealsUtil.getTos(service.getAll(SecurityUtil.authUserId()), SecurityUtil.authUserCaloriesPerDay());
    }

    public List<MealTo> getAllFilter(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
        log.info("getallFilter startDate{},startTime{}, endDate{},endTime{}", startDate, startTime, endDate, endTime);
        if (startDate == null) startDate = LocalDate.MIN;
        if (endDate == null) endDate = LocalDate.MAX;
        List<Meal> mealFilterDate = service.getFilterDate(SecurityUtil.authUserId(), startDate, endDate);
        if (startTime == null) startTime = LocalTime.MIN;
        if (endTime == null) endTime = LocalTime.MAX;
        return MealsUtil.getFilteredTos(mealFilterDate, SecurityUtil.authUserCaloriesPerDay(), startTime, endTime);
    }

    public void delete(int id) {
        log.info("delete {}", id);
        service.delete(id, SecurityUtil.authUserId());
    }

    public Meal get(int id) {
        log.info("get {}", id);
        return service.get(id, SecurityUtil.authUserId());
    }

    public Meal create(Meal meal) {
        log.info("create {}", meal);
        return service.create(meal, SecurityUtil.authUserId());
    }

    public void update(Meal meal, int id) {
        log.info("update {} with id={}", meal, id);
        service.update(meal, SecurityUtil.authUserId());
    }

}