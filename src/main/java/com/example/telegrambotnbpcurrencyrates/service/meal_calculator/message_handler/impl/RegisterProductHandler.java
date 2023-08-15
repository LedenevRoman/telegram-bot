package com.example.telegrambotnbpcurrencyrates.service.meal_calculator.message_handler.impl;

import com.example.telegrambotnbpcurrencyrates.service.meal_calculator.ProductService;
import com.example.telegrambotnbpcurrencyrates.service.meal_calculator.message_handler.MessageHandler;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static com.example.telegrambotnbpcurrencyrates.service.util.TelegramBotUtils.*;

public class RegisterProductHandler implements MessageHandler {
    private final ProductService productService;

    public RegisterProductHandler(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public SendMessage handleMessage(Long chatId, String inputMessage, String firstName) {
        productService.registerNewProduct(inputMessage);
        SendMessage sendMessage = createSendMessage(chatId, DONE + "\n" +
                MEAL_PRODUCT_CHOOSE);
        return addButtonsToMessage(sendMessage, getMainMenuChooseButtons());
    }
}
