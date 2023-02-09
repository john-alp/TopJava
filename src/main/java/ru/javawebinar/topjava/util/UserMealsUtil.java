package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 200),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 3410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        List<UserMealWithExcess> mealsToStream = filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);

        mealsTo.forEach(System.out::println);
        System.out.println("------------------");
        mealsToStream.forEach(System.out::println);

    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        /* группируем по дате и суммируем по дате калории */
        Map<LocalDate, Integer> caloriesPerDayMap = new HashMap<>();
        for (UserMeal meal : meals) {
            LocalDate localDate = meal.getDateTime().toLocalDate();
            caloriesPerDayMap.put(localDate, caloriesPerDayMap.getOrDefault(localDate, 0) + meal.getCalories());
        }
        /* создаем лист из заданного диапазона и выставляем статус превышения (excess) */
        List<UserMealWithExcess> userMealWithExcessList = new ArrayList<>();
        for (UserMeal meal : meals) {
            LocalTime localTime = meal.getDateTime().toLocalTime();
            if (TimeUtil.isBetweenInclusive(localTime, startTime, endTime)) {
                userMealWithExcessList.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(),
                        meal.getCalories(), caloriesPerDayMap.get(meal.getDateTime().toLocalDate()) > caloriesPerDay));
            }
        }
        return userMealWithExcessList;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> mealsList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO Implement by streams
        Map<LocalDate, Integer> caloriesSumByDay = mealsList.stream().collect(Collectors.groupingBy(userMeal -> userMeal.getDateTime().toLocalDate(),
                Collectors.summingInt(UserMeal::getCalories)));

        return mealsList
                .stream()
                .filter(userMeal -> TimeUtil.isBetweenInclusive(userMeal.getDateTime().toLocalTime(), startTime, endTime))
                .map(userMeal -> new UserMealWithExcess(userMeal.getDateTime(), userMeal.getDescription(),
                        userMeal.getCalories(), caloriesSumByDay.get(userMeal.getDateTime().toLocalDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }
}
