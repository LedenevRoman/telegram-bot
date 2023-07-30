package com.example.telegrambotnbpcurrencyrates.dao.meal_calculator.mysql;

import com.example.telegrambotnbpcurrencyrates.dao.meal_calculator.UserDao;
import com.example.telegrambotnbpcurrencyrates.model.meal_calculator.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserDaoMySql extends CrudDaoMySql<User> implements UserDao {

    public UserDaoMySql() {
        setClazz(User.class);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        try {
            return Optional.ofNullable(getEntityManager().createQuery("from User user " +
                            "where user.email = :email", User.class)
                    .setParameter("email", email)
                    .getSingleResult());
        } catch (RuntimeException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findOneByFirstNameAndDailyCalories(String firstName) {
        try {
            return Optional.ofNullable(getEntityManager().createQuery("from User user " +
                            "where user.name = :name and user.dailyKilocalorieIntake is null", User.class)
                    .setParameter("name", firstName)
                    .getSingleResult());
        } catch (RuntimeException e) {
            return Optional.empty();
        }
    }
}
