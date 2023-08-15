package com.example.telegrambotnbpcurrencyrates.service.meal_calculator.message_handler;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface MessageHandler {

    SendMessage handleMessage(Long chatId, String inputMessage, String firstName);
}
