package com.example.telegrambotnbpcurrencyrates.topic.impl;

import com.example.telegrambotnbpcurrencyrates.service.currency.CurrencyService;
import com.example.telegrambotnbpcurrencyrates.topic.Topic;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Getter
public class CurrencyTopic implements Topic {
    private final String name = "Currency rates";
    private final CurrencyService currencyService;

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
