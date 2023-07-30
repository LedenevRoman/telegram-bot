package com.example.telegrambotnbpcurrencyrates.service.meal_calculator;

import com.example.telegrambotnbpcurrencyrates.dao.meal_calculator.UserDao;
import com.example.telegrambotnbpcurrencyrates.model.meal_calculator.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class UserService {
    private static final Integer CALORIES_INDEX = 0;
    private static final Integer PROTEINS_INDEX = 1;
    private static final Integer FATS_INDEX = 2;
    private static final Integer CARBOHYDRATES_INDEX = 3;
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Transactional
    public void registerNewUser(String firstName, String inputMessage) {
        User user = new User();
        user.setName(firstName);
        user.setEmail(inputMessage);
        userDao.save(user);
    }

    @Transactional
    public void updateDailyCaloriesOnUser(String email, String inputMessage) {
        User user = getUserByEmail(email).orElseThrow(RuntimeException::new);
        String[] userData = inputMessage.split("/");
        user.setDailyKilocalorieIntake(Integer.parseInt(userData[CALORIES_INDEX]));
        user.setDailyProteinsIntake(new BigDecimal(userData[PROTEINS_INDEX]));
        user.setDailyFatsIntake(new BigDecimal(userData[FATS_INDEX]));
        user.setDailyCarbohydratesIntake(new BigDecimal(userData[CARBOHYDRATES_INDEX]));

        userDao.update(user);
    }

    @Transactional
    public Optional<User> getUserByEmail(String email) {
        return userDao.findByEmail(email);
    }

    public boolean isUserRegistered(String email) {
        return getUserByEmail(email).isPresent();
    }
}
