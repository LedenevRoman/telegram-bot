package com.example.telegrambotnbpcurrencyrates.service.meal_calculator.message_handler.impl;

import com.example.telegrambotnbpcurrencyrates.model.meal_calculator.Meal;
import com.example.telegrambotnbpcurrencyrates.model.meal_calculator.MealDetail;
import com.example.telegrambotnbpcurrencyrates.model.meal_calculator.Product;
import com.example.telegrambotnbpcurrencyrates.service.meal_calculator.MealDetailService;
import com.example.telegrambotnbpcurrencyrates.service.meal_calculator.MealService;
import com.example.telegrambotnbpcurrencyrates.service.meal_calculator.ProductService;
import com.example.telegrambotnbpcurrencyrates.service.meal_calculator.message_handler.MessageHandler;
import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Map;
import java.util.Optional;

import static com.example.telegrambotnbpcurrencyrates.service.util.TelegramBotUtils.*;

public class AddProductToMeal implements MessageHandler {
    private static final int PRODUCT_NAME_INDEX = 0;
    private static final int WEIGHT_INDEX = 1;
    private final Map<Long, String> chatIdEmailMap;
    private final MealService mealService;
    private final MealDetailService mealDetailService;
    private final ProductService productService;

    public AddProductToMeal(Map<Long, String> chatIdEmailMap, MealService mealService,
                            MealDetailService mealDetailService, ProductService productService) {
        this.chatIdEmailMap = chatIdEmailMap;
        this.mealService = mealService;
        this.mealDetailService = mealDetailService;
        this.productService = productService;
    }

    @Override
    public SendMessage handleMessage(Long chatId, String inputMessage, String firstName) {
        String email = chatIdEmailMap.get(chatId);
        String[] productNameWeight = inputMessage.split("/");
        String productName = productNameWeight[PRODUCT_NAME_INDEX];
        Integer productWeight = Integer.parseInt(productNameWeight[WEIGHT_INDEX]);
        Optional<Product> productOptional = productService.getProductByName(productName);
        if (productOptional.isEmpty()) {
            return getProductMissingSendMessage(chatId);
        }
        Product product = productOptional.get();
        Meal meal;
        if (mealService.isUserHaveUnfinishedMeal(email)) {
            MealDetail mealDetail = mealDetailService.createNewMealDetail(product, productWeight);
            meal = mealService.addNewMealDetailToUserMeal(email, mealDetail);
        } else {
            meal = mealService.createNewMeal(email, product, productWeight);
        }

        String message = getMessage(meal);
        SendMessage sendMessage = createSendMessage(chatId, DONE + "\n" +
                message + "\n" +
                "\n" +
                ANOTHER_PRODUCT_OR_COMPLETE);
        return addButtonsToMessage(sendMessage, getAnotherProductOrCompleteButtons());
    }

    @NotNull
    private static String getMessage(Meal meal) {
        return "For now, your meal contains: "
                + meal.getEnergyKcal()
                + " kilocalories, "
                + meal.getProteins()
                + " proteins, "
                + meal.getFats()
                + " fats, "
                + meal.getCarbohydrates()
                + " carbohydrates";
    }

    private static SendMessage getProductMissingSendMessage(Long chatId) {
        return createSendMessage(chatId, PRODUCT_NOT_FOUND + "\n" + REGISTER_PRODUCT);
    }
}
