package com.example.telegrambotnbpcurrencyrates.topic;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface Topic {

    SendMessage handleMessage(Long chatId, Update update);

    SendMessage getInitSendMessage(Long chatId);
}
