package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int USER_ID = START_SEQ;
    public static final int ADMIN_ID = START_SEQ + 1;
    public static final int MEAL_USER_1 = START_SEQ + 2;
    public static final int MEAL_USER_2 = START_SEQ + 3;
    public static final LocalDateTime DATE_TIME_OF_MEAL_USER_1 = LocalDateTime.of(2020, 12, 31, 11, 17);
    public static final LocalDate DATE_OF_MEAL_USER_1 = DATE_TIME_OF_MEAL_USER_1.toLocalDate();

    public static final Meal meal1 = new Meal(MEAL_USER_1, DATE_TIME_OF_MEAL_USER_1, "user meal 1", 500);
    public static final Meal meal2 = new Meal(MEAL_USER_2, LocalDateTime.of(2020, 12, 30, 16, 30), "user meal 2", 800);
    public static final Meal mealAdmin = new Meal(START_SEQ + 4, LocalDateTime.of(2021, 12, 31, 23, 20), "admin meal 1", 2000);
    public static final Meal meal3 = new Meal(START_SEQ + 5, LocalDateTime.of(2020, 12, 29, 9, 30), "user meal 3", 900);
    public static final Meal meal4 = new Meal(START_SEQ + 6, LocalDateTime.of(2020, 12, 30, 11, 30), "user meal 4", 2000);
    public static final Meal meal5 = new Meal(START_SEQ + 7, LocalDateTime.of(2020, 12, 31, 16, 30), "user meal 5", 800);
    public static final Meal meal6 = new Meal(START_SEQ + 8, LocalDateTime.of(2020, 12, 31, 20, 30), "user meal 6", 1500);
    public static final Meal meal7 = new Meal(START_SEQ + 9, LocalDateTime.of(2021, 6, 30, 16, 30), "user meal 7", 800);

    public static Meal getNew() {
        return new Meal(LocalDateTime.of(2019, 12, 30, 16, 30), "New", 3000);
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(meal1);
        updated.setDateTime(LocalDateTime.of(2021, 12, 30, 16, 30));
        updated.setDescription("user meal 1 Update");
        updated.setCalories(1000);
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingFieldByFieldElementComparator().isEqualTo(expected);
    }
}