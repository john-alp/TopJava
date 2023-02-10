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
        List<UserMeal> userMealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 200),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),

                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 900),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );


        List<UserMealWithExcess> mealsTo = filteredByCycles(userMealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

    }

    /**
     * @param mealList
     * @param startTime
     * @param endTime
     * @param caloriesPerDay
     * @return filtered list with excess. Implement by cycles
     */
    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime,
                                                            int caloriesPerDay) {

        Map<LocalDate, Integer> dateCaloriesMap = new HashMap<>();

        for (UserMeal userMeal : mealList) {
            dateCaloriesMap.compute(userMeal.getDateTime().toLocalDate(),
                    (k, v) -> v == null ? userMeal.getCalories() : v + userMeal.getCalories());
        }

        List<UserMealWithExcess> result = new ArrayList<>();

        for (UserMeal userMeal : mealList) {
            if (TimeUtil.isBetween(userMeal.getDateTime().toLocalTime(), startTime, endTime)) {
                int caloriesByDate = dateCaloriesMap.get(userMeal.getDateTime().toLocalDate());
                UserMealWithExcess mealWithExceed = new UserMealWithExcess(userMeal.getDateTime(), userMeal.getDescription(),
                        userMeal.getCalories(), caloriesByDate > caloriesPerDay);
                result.add(mealWithExceed);
            }
        }
        return result;
    }


    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        Map<LocalDate, Integer> collect = mealList.stream().collect(Collectors.toMap(
                userMeal -> userMeal.getDateTime().toLocalDate(),
                UserMeal::getCalories,
                Integer::sum));

        return mealList.stream()
                .filter(userMeal -> TimeUtil.isBetween(userMeal.getDateTime().toLocalTime(), startTime, endTime))
                .map(userMeal -> new UserMealWithExcess(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(),
                        collect.get(userMeal.getDateTime().toLocalDate()) > caloriesPerDay)).collect(Collectors.toList());
    }
}










