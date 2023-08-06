package com.example.telegrambotnbpcurrencyrates.service.currency;

import com.example.telegrambotnbpcurrencyrates.enums.CurrencyCode;
import com.example.telegrambotnbpcurrencyrates.model.currency.Currency;
import com.example.telegrambotnbpcurrencyrates.service.TelegramBotService;
import com.example.telegrambotnbpcurrencyrates.service.currency.exception.RequestApiException;
import com.example.telegrambotnbpcurrencyrates.service.util.TelegramBotUtils;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static com.example.telegrambotnbpcurrencyrates.enums.CurrencyCode.EUR;
import static com.example.telegrambotnbpcurrencyrates.enums.CurrencyCode.USD;

@Service
public class CurrencyService {
    private static final String INITIAL_MESSAGE = "Choose currency:";
    private static final String NO_SUCH_CURRENCY_MESSAGE = "We have not found such a currency." + "\n" +
            "Enter the currency whose official exchange rate" + "\n" +
            "you want to know in relation to PLN." + "\n" +
            "For example: USD";

    public SendMessage handleMessage(Long chatId, String inputMessage) {
        String outputMessage;
        if (isCurrencyCodesContainsMessage(inputMessage)) {
            outputMessage = getCurrencyRate(inputMessage);
        } else {
            outputMessage = NO_SUCH_CURRENCY_MESSAGE;
        }
        SendMessage sendMessage = TelegramBotUtils.createSendMessage(chatId, outputMessage);
        return TelegramBotUtils.addButtonsToMessage(sendMessage, getButtons());
    }

    public String getCurrencyRate(String message) {
        JSONObject currencyJson;
        try {
            currencyJson = getCurrencyJsonObject(message);
        } catch (IOException e) {
            throw new RequestApiException(e.getMessage());
        }
        Currency currency = convertFromJson(currencyJson);
        return "Official exchange rate of PLN to " + currency.getCode() + "\n" +
                "on date: " + currency.getEffectiveDate() + "\n" +
                "is: " + currency.getRate() + " PLN for 1 " + currency.getCode() + "\n" +
                "\n" +
                INITIAL_MESSAGE;

    }

    public SendMessage getInitSendMessage(Long chatId) {
        SendMessage sendMessage = TelegramBotUtils.createSendMessage(chatId, INITIAL_MESSAGE);
        return TelegramBotUtils.addButtonsToMessage(sendMessage, getButtons());
    }

    @NotNull
    private static Currency convertFromJson(JSONObject jsonObject) {
        Currency currency = new Currency();
        currency.setName(jsonObject.getString("currency"));
        currency.setCode(jsonObject.getString("code"));
        JSONArray jsonArray = jsonObject.getJSONArray("rates");
        JSONObject subObject = jsonArray.getJSONObject(0);
        currency.setEffectiveDate(LocalDate.parse(subObject.getString("effectiveDate"),
                DateTimeFormatter.ISO_LOCAL_DATE));
        currency.setRate(subObject.getDouble("mid"));
        return currency;
    }

    @NotNull
    private static JSONObject getCurrencyJsonObject(String message) throws IOException {
        URL url;
        if (message.equalsIgnoreCase(USD.name()) || message.equalsIgnoreCase(EUR.name())) {
            url = new URL("http://api.nbp.pl/api/exchangerates/rates/A/" + message);
        } else {
            url = new URL("http://api.nbp.pl/api/exchangerates/rates/B/" + message);
        }
        Scanner scanner = new Scanner((InputStream) url.getContent());
        StringBuilder result = new StringBuilder();
        while (scanner.hasNext()) {
            result.append(scanner.nextLine());
        }
        return new JSONObject(result.toString());
    }

    private static List<String> getButtons() {
        List<String> buttons = Arrays.stream(CurrencyCode.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        buttons.add(TelegramBotService.EXIT);
        return buttons;
    }

    private static boolean isCurrencyCodesContainsMessage(String inputMessage) {
        return Arrays.stream(CurrencyCode.values())
                .anyMatch(currencyCode -> currencyCode.name().equalsIgnoreCase(inputMessage));
    }
}
