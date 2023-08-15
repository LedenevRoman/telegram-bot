package com.example.telegrambotnbpcurrencyrates.service.meal_calculator.message_handler.impl;

import com.example.telegrambotnbpcurrencyrates.service.meal_calculator.UserService;
import com.example.telegrambotnbpcurrencyrates.service.meal_calculator.message_handler.MessageHandler;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Map;

import static com.example.telegrambotnbpcurrencyrates.service.util.TelegramBotUtils.*;

public class DailyIntakeHandler implements MessageHandler {
    private final Map<Long, String> chatIdEmailMap;
    private final UserService userService;

    public DailyIntakeHandler(Map<Long, String> chatIdEmailMap, UserService userService) {
        this.chatIdEmailMap = chatIdEmailMap;
        this.userService = userService;
    }
    @Override
    public SendMessage handleMessage(Long chatId, String inputMessage, String firstName) {
        String email = chatIdEmailMap.get(chatId);
        userService.updateDailyCaloriesOnUser(email, inputMessage);
        SendMessage sendMessage = createSendMessage(chatId, MEAL_PRODUCT_CHOOSE);
        return addButtonsToMessage(sendMessage, getMainMenuChooseButtons());
    }
}
