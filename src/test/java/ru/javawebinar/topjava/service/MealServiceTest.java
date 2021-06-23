package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.List;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;


@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))

public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal meal1 = service.get(MEAL_USER_1, USER_ID);
        assertMatch(meal1, MealTestData.mealUser1);
    }

    @Test
    public void getOther() {
        assertThrows(NotFoundException.class, () ->
                service.get(MEAL_USER_1, ADMIN_ID));
    }

    @Test
    public void delete() {
        service.delete(MEAL_USER_1, USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(MEAL_USER_1, USER_ID));
    }

    @Test
    public void deleteOther() {
        assertThrows(NotFoundException.class, () ->
                service.delete(MEAL_USER_1, ADMIN_ID));
    }

    @Test
    public void getBetweenInclusive() {
        List<Meal> all = service.getBetweenInclusive(DATE_CHECK_BEETHIN, DATE_CHECK_BEETHIN, USER_ID);
        assertMatch(all, mealUser6, mealUser5, mealUser1);
    }

    @Test
    public void getBetweenInclusiveNullBorder() {
        List<Meal> all = service.getBetweenInclusive(null, null, USER_ID);
        assertMatch(all, mealUser7, mealUser6, mealUser5, mealUser1, mealUser2, mealUser4, mealUser3);
    }

    @Test
    public void getAll() {
        List<Meal> all = service.getAll(USER_ID);
        assertMatch(all, mealUser7, mealUser6, mealUser5, mealUser1, mealUser2, mealUser4, mealUser3);
    }

    @Test
    public void update() {
        Meal updated = getUpdated();
        service.update(updated, USER_ID);
        assertMatch(service.get(MEAL_USER_1, USER_ID), getUpdated());
    }

    @Test
    public void updateOther() {
        assertThrows(NotFoundException.class, () ->
                service.update(getUpdated(), ADMIN_ID));

    }

    @Test
    public void create() {
        Meal created = service.create(getNew(), USER_ID);
        Integer newId = created.getId();
        Meal newMeal = getNew();
        newMeal.setId(newId);
        assertMatch(created, newMeal);
        assertMatch(service.get(newId, USER_ID), newMeal);
    }

    @Test
    public void duplicateDateTimeCreate() {
        assertThrows(DataAccessException.class, () ->
                service.create(new Meal(null, mealUser1.getDateTime(), "duplicate dateTime", 20), USER_ID));
    }
}