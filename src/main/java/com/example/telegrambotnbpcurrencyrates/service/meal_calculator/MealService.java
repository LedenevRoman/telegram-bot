package com.example.telegrambotnbpcurrencyrates.service.meal_calculator;

import com.example.telegrambotnbpcurrencyrates.dao.meal_calculator.MealDao;
import com.example.telegrambotnbpcurrencyrates.model.meal_calculator.Meal;
import com.example.telegrambotnbpcurrencyrates.model.meal_calculator.MealDetail;
import com.example.telegrambotnbpcurrencyrates.model.meal_calculator.Product;
import com.example.telegrambotnbpcurrencyrates.model.meal_calculator.User;
import com.example.telegrambotnbpcurrencyrates.service.util.TelegramBotUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MealService {
    private static final String TOTAL_MEALS_MESSAGE = ", your total meals for today contains: ";
    private static final String TODAY_REMAINDER_MESSAGE = ", your remainder for today is: ";
    private static final BigDecimal TABLE_VALUE_OF_WEIGHT = new BigDecimal(100);
    private final UserService userService;
    private final MealDetailService mealDetailService;
    private final MealDao mealDao;


    public MealService(UserService userService, MealDetailService mealDetailService, MealDao mealDao) {
        this.userService = userService;
        this.mealDetailService = mealDetailService;
        this.mealDao = mealDao;
    }

    @Transactional
    public Optional<Meal> getMealWithoutTimestampByUserEmail(String email) {
        return mealDao.getMealWithoutTimestampByUserEmail(email);
    }

    @Transactional
    public Meal createNewMeal(String email, Product product, Integer productWeight) {
        User user = userService.getUserByEmail(email).orElseThrow();
        MealDetail mealDetail = mealDetailService.createNewMealDetail(product, productWeight);
        Meal meal = new Meal();
        meal.setUser(user);
        meal.addMealDetail(mealDetail);
        updateMealInfo(meal);
        mealDao.save(meal);
        return meal;
    }

    @Transactional
    public Meal addNewMealDetailToUserMeal(String email, MealDetail mealDetail) {
        Meal meal = getMealWithoutTimestampByUserEmail(email).orElseThrow();
        meal.addMealDetail(mealDetail);
        updateMealInfo(meal);
        mealDao.update(meal);
        return meal;
    }

    @Transactional
    public Meal completeMeal(String email) {
        Meal meal = getMealWithoutTimestampByUserEmail(email).orElseThrow();
        meal.setCreatedAt(LocalDateTime.now());
        mealDao.update(meal);
        return meal;
    }

    @Transactional
    public String getTodayTotalOfUserMessage(String email, String firstName) {
        TodayTotal todayTotal = getTodayTotal(email);

        StringBuilder message = new StringBuilder(TelegramBotUtils.createReportMessage(firstName, TOTAL_MEALS_MESSAGE,
                todayTotal.getCalories(), todayTotal.getProteins(), todayTotal.getFats(), todayTotal.getCarbohydrates()));

        todayTotal.getMeals().forEach(meal -> {
                    message.append(getTime(meal.getCreatedAt()))
                            .append("\n");
                    meal.getMealDetails().forEach(mealDetail -> addProductInfo(message, mealDetail)
                    );
                    message.append("\n");
                }
        );

        return message.toString();
    }

    @NotNull
    @Transactional
    public TodayTotal getTodayTotal(String email) {
        List<Meal> meals = mealDao.getTodayMealsOfUser(email);
        List<MealDetail> totalMealDetails = meals.stream()
                .flatMap(meal -> meal.getMealDetails().stream())
                .collect(Collectors.toList());

        return new TodayTotal(meals, calculateCalories(totalMealDetails),
                calculateProteins(totalMealDetails), calculateFats(totalMealDetails),
                calculateCarbohydrates(totalMealDetails));
    }

    @Transactional
    public String getTodayReminderOfUserMessage(String email, String firstName) {
        User user = userService.getUserByEmail(email).orElseThrow();
        TodayTotal todayTotal = getTodayTotal(email);
        BigDecimal calories = BigDecimal.valueOf(user.getDailyKilocalorieIntake()).subtract(todayTotal.getCalories());
        BigDecimal proteins = user.getDailyProteinsIntake().subtract(todayTotal.getProteins());
        BigDecimal fats = user.getDailyFatsIntake().subtract(todayTotal.getFats());
        BigDecimal carbohydrates = user.getDailyCarbohydratesIntake().subtract(todayTotal.getCarbohydrates());

        return TelegramBotUtils.createReportMessage(firstName, TODAY_REMAINDER_MESSAGE, calories, proteins, fats, carbohydrates);
    }

    @Transactional
    public boolean isUserHaveUnfinishedMeal(String email) {
        return getMealWithoutTimestampByUserEmail(email).isPresent();
    }

    private BigDecimal calculateCalories(List<MealDetail> mealDetails) {
        return mealDetails
                .stream()
                .map(mealDetail -> new BigDecimal(mealDetail.getProduct().getEnergyKcal())
                        .multiply(new BigDecimal(mealDetail.getProductWeight()))
                        .divide(TABLE_VALUE_OF_WEIGHT, 2, RoundingMode.HALF_UP))
                .reduce(new BigDecimal(0) ,BigDecimal::add);
    }

    private BigDecimal calculateProteins(List<MealDetail> mealDetails) {
        return mealDetails
                .stream()
                .map(mealDetail -> mealDetail.getProduct().getProteins()
                        .multiply(new BigDecimal(mealDetail.getProductWeight()))
                        .divide(TABLE_VALUE_OF_WEIGHT, 2, RoundingMode.HALF_UP))
                .reduce(new BigDecimal(0) ,BigDecimal::add);
    }

    private BigDecimal calculateFats(List<MealDetail> mealDetails) {
        return mealDetails
                .stream()
                .map(mealDetail -> mealDetail.getProduct().getFats()
                        .multiply(new BigDecimal(mealDetail.getProductWeight()))
                        .divide(TABLE_VALUE_OF_WEIGHT, 2, RoundingMode.HALF_UP))
                .reduce(new BigDecimal(0) ,BigDecimal::add);
    }

    private BigDecimal calculateCarbohydrates(List<MealDetail> mealDetails) {
        return mealDetails
                .stream()
                .map(mealDetail -> mealDetail.getProduct().getCarbohydrates()
                        .multiply(new BigDecimal(mealDetail.getProductWeight()))
                        .divide(TABLE_VALUE_OF_WEIGHT, 2, RoundingMode.HALF_UP))
                .reduce(new BigDecimal(0) ,BigDecimal::add);
    }

    private void updateMealInfo(Meal meal) {
        meal.setEnergyKcal(calculateCalories(meal.getMealDetails()));
        meal.setProteins(calculateProteins(meal.getMealDetails()));
        meal.setFats(calculateFats(meal.getMealDetails()));
        meal.setCarbohydrates(calculateCarbohydrates(meal.getMealDetails()));
    }

    private void addProductInfo(StringBuilder message, MealDetail mealDetail) {
        message.append(mealDetail.getProduct().getProductName())
                .append(" - [ ")
                .append(calculateCalories(List.of(mealDetail)))
                .append(" / ")
                .append(calculateProteins(List.of(mealDetail)))
                .append(" / ")
                .append(calculateFats(List.of(mealDetail)))
                .append(" / ")
                .append(calculateCarbohydrates(List.of(mealDetail)))
                .append(" ]")
                .append("\n");
    }

    private String getTime(LocalDateTime createdAt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return createdAt.format(formatter);
    }
}
