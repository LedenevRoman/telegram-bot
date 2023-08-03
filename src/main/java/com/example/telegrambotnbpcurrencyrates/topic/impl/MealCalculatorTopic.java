package com.example.telegrambotnbpcurrencyrates.topic.impl;

import com.example.telegrambotnbpcurrencyrates.service.meal_calculator.MealCalculatorService;
import com.example.telegrambotnbpcurrencyrates.topic.Topic;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Getter
@Component
public class MealCalculatorTopic implements Topic {
    private final String name = "Meal Calculator";
    private final MealCalculatorService mealCalculatorService;

    public MealCalculatorTopic(MealCalculatorService mealCalculatorService) {
        this.mealCalculatorService = mealCalculatorService;
    }

    @Override
    public SendMessage handleMessage(Long chatId, Update update) {
        return mealCalculatorService.handleMessage(chatId, update.getMessage().getText(),
                update.getMessage().getChat().getFirstName());
    }

    @Override
    public SendMessage getInitSendMessage(Long chatId) {
        return mealCalculatorService.getInitSendMessage(chatId);
    }
}
