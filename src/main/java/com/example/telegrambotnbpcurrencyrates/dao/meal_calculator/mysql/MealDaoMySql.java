package com.example.telegrambotnbpcurrencyrates.dao.meal_calculator.mysql;

import com.example.telegrambotnbpcurrencyrates.dao.meal_calculator.MealDao;
import com.example.telegrambotnbpcurrencyrates.model.meal_calculator.Meal;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MealDaoMySql extends CrudDaoMySql<Meal> implements MealDao {
    public MealDaoMySql() {
        setClazz(Meal.class);
    }

    @Override
    public Optional<Meal> getMealWithoutTimestampByUserEmail(String email) {
        try {
            return Optional.ofNullable(getEntityManager().createQuery("SELECT meal FROM Meal meal " +
                            "JOIN meal.user user " +
                            "WHERE user.email = :email " +
                            "AND meal.createdAt IS NULL", Meal.class)
                    .setParameter("email", email)
                    .getSingleResult());
        } catch (RuntimeException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Meal> getTodayMealsOfUser(String email) {
        return getEntityManager().createQuery("SELECT meal FROM Meal meal " +
                        "JOIN meal.user user " +
                        "WHERE user.email = :email " +
                        "AND DAY(meal.createdAt) = DAY(CURRENT_DATE)", Meal.class)
                .setParameter("email", email)
                .getResultList();
    }
}
