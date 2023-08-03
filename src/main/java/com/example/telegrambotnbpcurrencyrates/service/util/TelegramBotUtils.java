package com.example.telegrambotnbpcurrencyrates.service.util;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public final class TelegramBotUtils {

    private TelegramBotUtils() {
    }

    public static SendMessage createSendMessage(Long chatId, String textToSend) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(textToSend);
        return sendMessage;
    }

    public static SendMessage addButtonsToMessage(SendMessage sendMessage, List<String> buttons) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(true);
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();

        for (String button : buttons) {
            row.add(new KeyboardButton(button));
        }
        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);

        sendMessage.setReplyMarkup(keyboardMarkup);
        return sendMessage;
    }

    public static String createReportMessage(String firstName, String message, BigDecimal calories,
                                             BigDecimal proteins, BigDecimal fats, BigDecimal carbohydrates) {
        return "Dear, " + firstName +
                message +
                calories +
                " kilocalories, " +
                proteins +
                " proteins, " +
                fats +
                " fats, " +
                carbohydrates +
                " carbohydrates" +
                "\n" +
                "\n";
    }
}
