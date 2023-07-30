package com.example.telegrambotnbpcurrencyrates.topic.impl;

import com.example.telegrambotnbpcurrencyrates.service.currency.CurrencyService;
import com.example.telegrambotnbpcurrencyrates.topic.Topic;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class CurrencyTopic implements Topic {
    private final String name = "Currency rates";
    private final CurrencyService currencyService;

    public String getName() {
        return name;
    }

    public CurrencyTopic(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @Override
    public SendMessage handleMessage(Long chatId, Update update) {
        return currencyService.handleMessage(chatId, update.getMessage().getText());
    }

    @Override
    public SendMessage getInitSendMessage(Long chatId) {
        return currencyService.getInitSendMessage(chatId);
    }
}
