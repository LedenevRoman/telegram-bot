package com.example.telegrambotnbpcurrencyrates.service.meal_calculator.message_handler.impl;

import com.example.telegrambotnbpcurrencyrates.service.meal_calculator.message_handler.MessageHandler;
import com.example.telegrambotnbpcurrencyrates.service.util.TelegramBotUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static com.example.telegrambotnbpcurrencyrates.service.util.TelegramBotUtils.createSendMessage;

public class NewProductMessageHandler implements MessageHandler {

    @Override
    public SendMessage handleMessage(Long chatId, String inputMessage, String firstName) {
        return createSendMessage(chatId, TelegramBotUtils.REGISTER_PRODUCT);
    }
}
