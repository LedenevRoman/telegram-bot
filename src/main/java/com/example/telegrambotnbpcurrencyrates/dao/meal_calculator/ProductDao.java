package com.example.telegrambotnbpcurrencyrates.dao.meal_calculator;

import com.example.telegrambotnbpcurrencyrates.model.meal_calculator.Product;

import java.util.Optional;

public interface ProductDao extends CrudDao<Product> {
    Optional<Product> findByName(String productName);
}
