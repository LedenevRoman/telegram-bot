package com.example.telegrambotnbpcurrencyrates.service;

import com.example.telegrambotnbpcurrencyrates.service.util.TelegramBotUtils;
import com.example.telegrambotnbpcurrencyrates.topic.Topic;
import com.example.telegrambotnbpcurrencyrates.topic.impl.CurrencyTopic;
import com.example.telegrambotnbpcurrencyrates.topic.impl.MealCalculatorTopic;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class TelegramBotService {
    public static final String EXIT = "Exit";
    private static final String UNKNOWN_INPUT_MESSAGE = "Sorry, I don't know how to handle such command yet :(";
    private static final String START = "/start";
    private final CurrencyTopic currencyTopic;
    private final MealCalculatorTopic mealCalculatorTopic;
    private final Map<Long, Topic> chatIdTopicMap = new HashMap<>();
    private final Map<String, Topic> nameTopicMap = new HashMap<>();

    @PostConstruct
    private void init() {
        nameTopicMap.put(currencyTopic.getName(), currencyTopic);
        nameTopicMap.put(mealCalculatorTopic.getName(), mealCalculatorTopic);
    }

    public SendMessage handleUpdate(Update update) {
        long chatId = update.getMessage().getChatId();
        if(checkMessageExists(update)){
            String messageText = update.getMessage().getText();

            if (messageText.equals(START)) {
                return startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
            }
            if (messageText.equals(EXIT)) {
                chatIdTopicMap.put(chatId, null);
                return exitTopicMessage(chatId, update.getMessage().getChat().getFirstName());
            }
            if (chatIdTopicMap.get(chatId) != null) {
                Topic topic = chatIdTopicMap.get(chatId);
                return topic.handleMessage(chatId, update);
            }
            if (nameTopicMap.containsKey(messageText)) {
                Topic topic = nameTopicMap.get(messageText);
                chatIdTopicMap.put(chatId, topic);
                return topic.getInitSendMessage(chatId);
            }
        }
        return TelegramBotUtils.createSendMessage(chatId, UNKNOWN_INPUT_MESSAGE);
    }


    private static boolean checkMessageExists(Update update) {
        return update.hasMessage() && update.getMessage().hasText();
    }

    private SendMessage getSelectTopicMessage(Long chatId, String message) {
        List<String> buttons = new ArrayList<>(nameTopicMap.keySet());
        message = message + "\n" + "Choose topic:";
        SendMessage sendMessage = TelegramBotUtils.createSendMessage(chatId, message);
        return TelegramBotUtils.addButtonsToMessage(sendMessage, buttons);
    }

    private SendMessage startCommandReceived(Long chatId, String name) {
        String response = "Hi, " + name + ", nice to meet you!" + "\n" +
                "How can i help you?" + "\n" +
                "Here is a list of topics i can help with :)";
        return getSelectTopicMessage(chatId, response);
    }

    private SendMessage exitTopicMessage(Long chatId, String name) {
        String response = "Dear, " + name + ", I hope I've been helpful for you!" + "\n" +
                "Is there anything else I can do to help? :)";
        return getSelectTopicMessage(chatId, response);
    }
}
