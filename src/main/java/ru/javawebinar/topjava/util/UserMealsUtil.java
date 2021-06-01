package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        List<UserMealWithExcess> mealsTo4 = filteredByCyclesOptional2(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo4.forEach(System.out::println);

        List<UserMealWithExcess> mealsTo2 = filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo2.forEach(System.out::println);

        List<UserMealWithExcess> mealsTo3 = filteredByStreamsOption2(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo3.forEach(System.out::println);

    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMealWithExcess> result = new ArrayList<>();
        Map<LocalDate, Integer> caloriesPerDayMap = new HashMap<>();
        meals.forEach(meal -> {
            caloriesPerDayMap.merge(meal.getDate(), meal.getCalories(), Integer::sum);
        });
        meals.forEach(meal -> {
            if (TimeUtil.isBetweenHalfOpen(meal.getTime(), startTime, endTime)) {
                result.add(
                       createUserMealExcess(meal,caloriesPerDayMap.get(meal.getDate()) > caloriesPerDay)
                );
            }
        });
        return result;
    }

        public static List<UserMealWithExcess> filteredByCyclesOptional2(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay){
            Map<LocalDate, Integer> caloriesPerDayMap = new HashMap<>();
            List<Callable<Void>> tasks = new ArrayList<>();
            List<UserMealWithExcess> mealsTo = Collections.synchronizedList(new ArrayList<>());

            meals.forEach(meal -> {
                caloriesPerDayMap.merge(meal.getDate(), meal.getCalories(), Integer::sum);
                if (TimeUtil.isBetweenHalfOpen(meal.getTime(), startTime, endTime)) {
                    tasks.add(() -> {
                        mealsTo.add(createUserMealExcess(meal, caloriesPerDayMap.get(meal.getDate()) > caloriesPerDay));
                        return null;
                    });
                }
            });
            ExecutorService executorService = Executors.newCachedThreadPool();
            try {
                executorService.invokeAll(tasks);
            } catch (InterruptedException e) {

            }
            executorService.shutdown();
            return mealsTo;
    }

    private static UserMealWithExcess createUserMealExcess(UserMeal meal, boolean excess) {
        return new UserMealWithExcess(meal.getDateTime(),
                meal.getDescription(),
                meal.getCalories(),
                excess);
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesPerDayMap = meals.stream()
               .collect(Collectors.groupingBy(UserMeal::getDate ,Collectors.summingInt(UserMeal::getCalories)));

        return meals.stream()
                .filter(meal -> TimeUtil.isBetweenHalfOpen(meal.getTime(), startTime, endTime))
                .map(
                        meal -> createUserMealExcess(meal, caloriesPerDayMap.get(meal.getDate()) > caloriesPerDay)
                )
                .collect(Collectors.toList());
    }

    public static List<UserMealWithExcess> filteredByStreamsOption2(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {


        class MealCollector implements Collector<UserMeal, List<UserMeal>, List<UserMealWithExcess>>{
            private final LocalTime startTime;
            private final LocalTime endTime;
            private final int caloriesPerDay;
            final Map<LocalDate, Integer> caloriesPerDayMap = new HashMap<>();

            public MealCollector(LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
                this.startTime = startTime;
                this.endTime = endTime;
                this.caloriesPerDay = caloriesPerDay;
            }

            @Override
            public Supplier<List<UserMeal>> supplier() {
                return LinkedList::new;
            }

            @Override
            public BiConsumer<List<UserMeal>, UserMeal> accumulator() {
                return (array,meal) ->{
                    caloriesPerDayMap.merge(meal.getDate(), meal.getCalories(), Integer::sum);
                    if (TimeUtil.isBetweenHalfOpen(meal.getTime(), startTime, endTime))
                           array.add(meal);
                };
            }

            @Override
            public BinaryOperator<List<UserMeal>> combiner() {
                   return (l, r) -> {
                   l.addAll(r); return l; };
            }

            @Override
            public Function<List<UserMeal>, List<UserMealWithExcess>> finisher() {
                return s ->  s.stream()
                            .map
                                    (meal -> createUserMealExcess(meal,caloriesPerDayMap.get(meal.getDate()) > caloriesPerDay))
                            .collect(Collectors.toList());
                }

            @Override
            public Set<Characteristics> characteristics() {
                return EnumSet.of(Characteristics.UNORDERED);
            }
        }
        return meals.stream()
                .collect(new MealCollector(startTime, endTime, caloriesPerDay));

    }
}
