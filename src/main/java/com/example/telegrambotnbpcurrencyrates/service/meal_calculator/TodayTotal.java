package com.example.telegrambotnbpcurrencyrates.service.meal_calculator;

import com.example.telegrambotnbpcurrencyrates.model.meal_calculator.Meal;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
public class TodayTotal {
    private final List<Meal> meals;
    private final BigDecimal calories;
    private final BigDecimal proteins;
    private final BigDecimal fats;
    private final BigDecimal carbohydrates;

    public TodayTotal(List<Meal> meals, BigDecimal calories, BigDecimal proteins, BigDecimal fats, BigDecimal carbohydrates) {
        this.meals = meals;
        this.calories = calories;
        this.proteins = proteins;
        this.fats = fats;
        this.carbohydrates = carbohydrates;
    }
}
