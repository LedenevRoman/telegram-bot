package com.example.telegrambotnbpcurrencyrates.service.util;

import com.example.telegrambotnbpcurrencyrates.service.TelegramBotService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public final class TelegramBotUtils {
    public static final String ENTER_YOUR_EMAIL = "Please, enter your email:";
    public static final String MEAL_REPORT_MESSAGE = ", your meal contains: ";
    public static final String DONE = "Done :)";
    public static final String REGISTER_PRODUCT = "Please, enter information about 100 grams or milliliters of " +
            "product (name/calories/proteins/fats/carbohydrates)" + "\n" +
            "For example: CocaCola/123/4.5/6.7/8.901";
    public static final String ADD_PRODUCT_TO_MEAL = "Please, enter the name of the product and the amount of " +
            "grams or milliliters (product name/grams)" + "\n" +
            "For example: CocaCola/123";
    public static final String PRODUCT_NOT_FOUND = "Sorry :( I don't found this product. Please, could you add this product?";
    public static final String ANOTHER_PRODUCT_OR_COMPLETE = "Add another product or complete meal?";
    public static final String MEAL_PRODUCT_CHOOSE = "Please, select what you want to enter:";
    public static final String NEW_MEAL = "New meal";
    public static final String NEW_PRODUCT = "New product";
    public static final String TODAY_TOTAL = "Today total";
    public static final String TODAY_REMAINDER = "Today remainder";
    public static final String ADD_ANOTHER_PRODUCT = "Add another product";
    public static final String COMPLETE_MEAL = "Complete";

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

    public static List<String> getMainMenuChooseButtons() {
        return List.of(NEW_MEAL, NEW_PRODUCT, TODAY_TOTAL, TODAY_REMAINDER, TelegramBotService.EXIT);
    }

    public static List<String> getAnotherProductOrCompleteButtons() {
        return List.of(ADD_ANOTHER_PRODUCT, COMPLETE_MEAL);
    }
}
