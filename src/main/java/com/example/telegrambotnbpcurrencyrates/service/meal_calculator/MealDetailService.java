package com.example.telegrambotnbpcurrencyrates.service.meal_calculator;

import com.example.telegrambotnbpcurrencyrates.model.meal_calculator.MealDetail;
import com.example.telegrambotnbpcurrencyrates.model.meal_calculator.Product;
import org.springframework.stereotype.Service;

@Service
public class MealDetailService {
    public MealDetail createNewMealDetail(Product product, Integer productWeight) {
        MealDetail mealDetail = new MealDetail();
        mealDetail.setProduct(product);
        mealDetail.setProductWeight(productWeight);
        return mealDetail;
    }
}
