package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.DateTimeUtil;

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
    public static final LocalDate DATE_OF_MEAL_USER_1 = LocalDate.of(2020, 12, 31);

    public static final Meal meal1 = new Meal(MEAL_USER_1, LocalDateTime.parse("2020-12-31 11:17", DateTimeUtil.DATE_TIME_FORMATTER), "user meal 1", 500);
    public static final Meal meal2 = new Meal(MEAL_USER_2, LocalDateTime.parse("2020-12-30 16:30", DateTimeUtil.DATE_TIME_FORMATTER), "user meal 2", 800);

    public static Meal getNew() {
        return new Meal(LocalDateTime.parse("2019-12-30 16:30", DateTimeUtil.DATE_TIME_FORMATTER), "New", 3000);
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(meal1);
        updated.setDateTime(LocalDateTime.parse("2021-12-30 16:30", DateTimeUtil.DATE_TIME_FORMATTER));
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
        assertThat(actual).isEqualTo(expected);
    }
}