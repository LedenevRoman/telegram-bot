package com.example.telegrambotnbpcurrencyrates.service.meal_calculator.message_handler.impl;

import com.example.telegrambotnbpcurrencyrates.service.meal_calculator.message_handler.MessageHandler;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static com.example.telegrambotnbpcurrencyrates.service.util.TelegramBotUtils.ADD_PRODUCT_TO_MEAL;
import static com.example.telegrambotnbpcurrencyrates.service.util.TelegramBotUtils.createSendMessage;

public class AddProductMessage implements MessageHandler {
    @Override
    public SendMessage handleMessage(Long chatId, String inputMessage, String firstName) {
        return createSendMessage(chatId, ADD_PRODUCT_TO_MEAL);
    }
}
