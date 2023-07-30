package com.example.telegrambotnbpcurrencyrates.dao.meal_calculator;

import com.example.telegrambotnbpcurrencyrates.model.meal_calculator.User;

import java.util.Optional;

public interface UserDao extends CrudDao<User> {

    Optional<User> findByEmail(String email);

    Optional<User> findOneByFirstNameAndDailyCalories(String firstName);
}

