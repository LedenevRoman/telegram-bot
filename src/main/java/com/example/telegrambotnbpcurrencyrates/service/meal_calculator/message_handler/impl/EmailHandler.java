package com.example.telegrambotnbpcurrencyrates.service.meal_calculator.message_handler.impl;

import com.example.telegrambotnbpcurrencyrates.service.meal_calculator.UserService;
import com.example.telegrambotnbpcurrencyrates.service.meal_calculator.message_handler.MessageHandler;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Map;

import static com.example.telegrambotnbpcurrencyrates.service.util.TelegramBotUtils.*;

public class EmailHandler implements MessageHandler {
    private static final String ENTER_YOUR_DAILY_CALORIES_INTAKE = "Please, enter your daily calories/proteins/fats/" +
            "carbohydrates intake" + "\n" +
            "For example: 1234/132.5/97/201.2";
    private final Map<Long, String> chatIdEmailMap;
    private final UserService userService;

    public EmailHandler(Map<Long, String> chatIdEmailMap, UserService userService) {
        this.chatIdEmailMap = chatIdEmailMap;
        this.userService = userService;
    }

    @Override
    public SendMessage handleMessage(Long chatId, String inputMessage, String firstName) {
        chatIdEmailMap.put(chatId, inputMessage);
        if (userService.isUserRegistered(inputMessage)) {
            SendMessage sendMessage = createSendMessage(chatId, MEAL_PRODUCT_CHOOSE);
            return addButtonsToMessage(sendMessage, getMainMenuChooseButtons());
        } else {
            userService.registerNewUser(firstName, inputMessage);
            return createSendMessage(chatId, ENTER_YOUR_DAILY_CALORIES_INTAKE);
        }
    }
}
