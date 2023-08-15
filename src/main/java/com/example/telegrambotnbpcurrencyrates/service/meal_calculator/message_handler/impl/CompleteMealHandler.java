package com.example.telegrambotnbpcurrencyrates.service.meal_calculator.message_handler.impl;

import com.example.telegrambotnbpcurrencyrates.model.meal_calculator.Meal;
import com.example.telegrambotnbpcurrencyrates.service.meal_calculator.MealService;
import com.example.telegrambotnbpcurrencyrates.service.meal_calculator.message_handler.MessageHandler;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Map;

import static com.example.telegrambotnbpcurrencyrates.service.util.TelegramBotUtils.*;

public class CompleteMealHandler implements MessageHandler {
    private final Map<Long, String> chatIdEmailMap;
    private final MealService mealService;

    public CompleteMealHandler(Map<Long, String> chatIdEmailMap, MealService mealService) {
        this.chatIdEmailMap = chatIdEmailMap;
        this.mealService = mealService;
    }

    @Override
    public SendMessage handleMessage(Long chatId, String inputMessage, String firstName) {
        String email = chatIdEmailMap.get(chatId);
        Meal meal = mealService.completeMeal(email);
        String message = createReportMessage(firstName, MEAL_REPORT_MESSAGE, meal.getEnergyKcal(),
                meal.getProteins(), meal.getFats(), meal.getCarbohydrates());
        SendMessage sendMessage = createSendMessage(chatId, message);
        return addButtonsToMessage(sendMessage, getMainMenuChooseButtons());
    }
}
