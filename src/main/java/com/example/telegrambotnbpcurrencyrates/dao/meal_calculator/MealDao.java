package com.example.telegrambotnbpcurrencyrates.dao.meal_calculator;

import com.example.telegrambotnbpcurrencyrates.model.meal_calculator.Meal;

import java.util.List;
import java.util.Optional;

public interface MealDao extends CrudDao<Meal> {
    Optional<Meal> getMealWithoutTimestampByUserEmail(String email);

    List<Meal> getTodayMealsOfUser(String email);
}
