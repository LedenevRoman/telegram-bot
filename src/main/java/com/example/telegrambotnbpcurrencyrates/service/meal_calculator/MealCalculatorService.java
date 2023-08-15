package com.example.telegrambotnbpcurrencyrates.service.meal_calculator;

import com.example.telegrambotnbpcurrencyrates.service.meal_calculator.message_handler.MessageHandler;
import com.example.telegrambotnbpcurrencyrates.service.meal_calculator.message_handler.impl.*;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.example.telegrambotnbpcurrencyrates.service.util.TelegramBotUtils.*;

@Service
public class MealCalculatorService {
    private static final String INCORRECT_INPUT = "Sorry, something went wrong, check your message is correct";
    private static final Map<String, MessageHandler> REGEXP_MESSAGE_HANDLER_MAP = new HashMap<>();
    private final UserService userService;
    private final ProductService productService;
    private final MealService mealService;
    private final MealDetailService mealDetailService;
    private final Map<Long, String> chatIdEmailMap = new HashMap<>();

    public MealCalculatorService(UserService userService, ProductService productService, MealService mealService,
                                 MealDetailService mealDetailService) {
        this.userService = userService;
        this.productService = productService;
        this.mealService = mealService;
        this.mealDetailService = mealDetailService;
    }

    @PostConstruct
    private void init() {
        REGEXP_MESSAGE_HANDLER_MAP.put("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
                new EmailHandler(chatIdEmailMap, userService));
        REGEXP_MESSAGE_HANDLER_MAP.put("\\d+/\\d+\\.?\\d*/\\d+\\.?\\d*/\\d+\\.?\\d*",
                new DailyIntakeHandler(chatIdEmailMap, userService));
        REGEXP_MESSAGE_HANDLER_MAP.put(NEW_PRODUCT,
                new NewProductMessageHandler());
        REGEXP_MESSAGE_HANDLER_MAP.put(".+/\\d+/\\d+\\.?\\d*/\\d+\\.?\\d*/\\d+\\.?\\d*",
                new RegisterProductHandler(productService));
        REGEXP_MESSAGE_HANDLER_MAP.put(NEW_MEAL,
                new AddProductMessage());
        REGEXP_MESSAGE_HANDLER_MAP.put(ADD_ANOTHER_PRODUCT,
                new AddProductMessage());
        REGEXP_MESSAGE_HANDLER_MAP.put(".+/\\d+",
                new AddProductToMeal(chatIdEmailMap, mealService, mealDetailService, productService));
        REGEXP_MESSAGE_HANDLER_MAP.put(COMPLETE_MEAL, new CompleteMealHandler(chatIdEmailMap, mealService));
        REGEXP_MESSAGE_HANDLER_MAP.put(TODAY_TOTAL, new TodayTotalHandler(chatIdEmailMap, mealService));
        REGEXP_MESSAGE_HANDLER_MAP.put(TODAY_REMAINDER, new TodayReminderHandler(chatIdEmailMap, mealService));
    }

    public SendMessage getInitSendMessage(Long chatId) {
        return createSendMessage(chatId, ENTER_YOUR_EMAIL);
    }

    public SendMessage handleMessage(Long chatId, String inputMessage, String firstName) {
        Optional<String> optionalKey = REGEXP_MESSAGE_HANDLER_MAP.keySet().stream()
                .filter(inputMessage::matches)
                .findFirst();
        if (optionalKey.isPresent()) {
            return REGEXP_MESSAGE_HANDLER_MAP.get(optionalKey.get()).handleMessage(chatId, inputMessage, firstName);
        } else {
            return createSendMessage(chatId, INCORRECT_INPUT);
        }
    }
}
