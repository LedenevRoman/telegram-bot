package com.example.telegrambotnbpcurrencyrates.dao.meal_calculator.mysql;

import com.example.telegrambotnbpcurrencyrates.dao.meal_calculator.MealDetailDao;
import com.example.telegrambotnbpcurrencyrates.model.meal_calculator.MealDetail;
import org.springframework.stereotype.Repository;

@Repository
public class MealDetailDaoMySql extends CrudDaoMySql<MealDetail> implements MealDetailDao {
    public MealDetailDaoMySql() {
        setClazz(MealDetail.class);
    }
}
