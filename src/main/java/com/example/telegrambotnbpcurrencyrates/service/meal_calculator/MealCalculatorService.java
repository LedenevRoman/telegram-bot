package com.example.telegrambotnbpcurrencyrates.service.meal_calculator;

import com.example.telegrambotnbpcurrencyrates.model.meal_calculator.Meal;
import com.example.telegrambotnbpcurrencyrates.model.meal_calculator.MealDetail;
import com.example.telegrambotnbpcurrencyrates.model.meal_calculator.Product;
import com.example.telegrambotnbpcurrencyrates.service.TelegramBotService;
import com.example.telegrambotnbpcurrencyrates.service.util.TelegramBotUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class MealCalculatorService {
    private static final String ENTER_YOUR_EMAIL = "Please, enter your email:";
    private static final String ENTER_YOUR_DAILY_CALORIES_INTAKE = "Please, enter your daily calories/proteins/fats/" +
            "carbohydrates intake" + "\n" +
            "For example: 1234/132.5/97/201.2";
    private static final String NEW_MEAL = "New meal";
    private static final String NEW_PRODUCT = "New product";
    private static final String TODAY_TOTAL = "Today total";
    private static final String TODAY_REMAINDER = "Today remainder";
    private static final String MEAL_REPORT_MESSAGE = ", your meal contains: ";
    private static final String DONE = "Done :)";
    private static final String REGISTER_PRODUCT = "Please, enter information about 100 grams or milliliters of " +
            "product (name/calories/proteins/fats/carbohydrates)" + "\n" +
            "For example: CocaCola/123/4.5/6.7/8.901";
    private static final String ADD_PRODUCT_TO_MEAL = "Please, enter the name of the product and the amount of " +
            "grams or milliliters (product name/grams)" + "\n" +
            "For example: CocaCola/123";
    private static final String PRODUCT_NOT_FOUND = "Sorry :( I don't found this product. Please, could you add this product?";
    private static final String ANOTHER_PRODUCT_OR_COMPLETE = "Add another product or complete meal?";
    private static final String ADD_ANOTHER_PRODUCT = "Add another product";
    private static final String COMPLETE_MEAL = "Complete";
    private static final String MEAL_PRODUCT_CHOOSE = "Please, select what you want to enter:";
    private static final String INCORRECT_INPUT = "Sorry, something went wrong, check your message is correct";
    private static final int PRODUCT_NAME_INDEX = 0;
    private static final int WEIGHT_INDEX = 1;
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

    public SendMessage getInitSendMessage(Long chatId) {
        return TelegramBotUtils.createSendMessage(chatId, ENTER_YOUR_EMAIL);
    }

    public SendMessage handleMessage(Long chatId, String inputMessage, String firstName) {
        if (inputMessage.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            chatIdEmailMap.put(chatId, inputMessage);
            if (userService.isUserRegistered(inputMessage)) {
                SendMessage sendMessage = TelegramBotUtils.createSendMessage(chatId, MEAL_PRODUCT_CHOOSE);
                return TelegramBotUtils.addButtonsToMessage(sendMessage, getMainMenuChooseButtons());
            } else {
                userService.registerNewUser(firstName, inputMessage);
                return TelegramBotUtils.createSendMessage(chatId, ENTER_YOUR_DAILY_CALORIES_INTAKE);
            }
        }
        if (inputMessage.matches("\\d+/\\d+\\.?\\d*/\\d+\\.?\\d*/\\d+\\.?\\d*")) {
            String email = chatIdEmailMap.get(chatId);
            userService.updateDailyCaloriesOnUser(email, inputMessage);
            SendMessage sendMessage = TelegramBotUtils.createSendMessage(chatId, MEAL_PRODUCT_CHOOSE);
            return TelegramBotUtils.addButtonsToMessage(sendMessage, getMainMenuChooseButtons());
        }
        if (inputMessage.equals(NEW_PRODUCT)) {
            return TelegramBotUtils.createSendMessage(chatId, REGISTER_PRODUCT);
        }
        if (inputMessage.matches(".+/\\d+/\\d+\\.?\\d*/\\d+\\.?\\d*/\\d+\\.?\\d*")) {
            productService.registerNewProduct(inputMessage);
            SendMessage sendMessage = TelegramBotUtils.createSendMessage(chatId, DONE + "\n" +
                    MEAL_PRODUCT_CHOOSE);
            return TelegramBotUtils.addButtonsToMessage(sendMessage, getMainMenuChooseButtons());
        }
        if (inputMessage.matches(NEW_MEAL) || inputMessage.matches(ADD_ANOTHER_PRODUCT)) {
            return TelegramBotUtils.createSendMessage(chatId, ADD_PRODUCT_TO_MEAL);
        }
        if (inputMessage.matches(".+/\\d+")) {
            String email = chatIdEmailMap.get(chatId);
            String[] productNameWeight = inputMessage.split("/");
            String productName = productNameWeight[PRODUCT_NAME_INDEX];
            Integer productWeight = Integer.parseInt(productNameWeight[WEIGHT_INDEX]);
            Optional<Product> productOptional = productService.getProductByName(productName);
            if (productOptional.isEmpty()) {
                return getProductMissingSendMessage(chatId);
            }
            Product product = productOptional.get();
            Meal meal;
            if (mealService.isUserHaveUnfinishedMeal(email)) {
                MealDetail mealDetail = mealDetailService.createNewMealDetail(product, productWeight);
                meal = mealService.addNewMealDetailToUserMeal(email, mealDetail);
            } else {
                meal = mealService.createNewMeal(email, product, productWeight);
            }

            String message = "For now, your meal contains: " + meal.getEnergyKcal() +
                    " kilocalories, " + meal.getProteins() + " proteins, " + meal.getFats() + " fats, " +
                    meal.getCarbohydrates() + " carbohydrates";
            SendMessage sendMessage = TelegramBotUtils.createSendMessage(chatId, DONE + "\n" +
                    message + "\n" +
                    "\n" +
                    ANOTHER_PRODUCT_OR_COMPLETE);
            return TelegramBotUtils.addButtonsToMessage(sendMessage, getAnotherProductOrCompleteButtons());
        }
        if (inputMessage.equals(COMPLETE_MEAL)) {
            String email = chatIdEmailMap.get(chatId);
            Meal meal = mealService.completeMeal(email);
            String message = TelegramBotUtils.createReportMessage(firstName, MEAL_REPORT_MESSAGE, meal.getEnergyKcal(),
                    meal.getProteins(), meal.getFats(), meal.getCarbohydrates());
            SendMessage sendMessage = TelegramBotUtils.createSendMessage(chatId, message);
            return TelegramBotUtils.addButtonsToMessage(sendMessage, getMainMenuChooseButtons());
        }
        if (inputMessage.equals(TODAY_TOTAL)) {
            String email = chatIdEmailMap.get(chatId);
            String message = mealService.getTodayTotalOfUserMessage(email, firstName);
            SendMessage sendMessage = TelegramBotUtils.createSendMessage(chatId, message);
            return TelegramBotUtils.addButtonsToMessage(sendMessage, getMainMenuChooseButtons());
        }
        if (inputMessage.equals(TODAY_REMAINDER)) {
            String email = chatIdEmailMap.get(chatId);
            String message = mealService.getTodayReminderOfUserMessage(email, firstName);
            SendMessage sendMessage = TelegramBotUtils.createSendMessage(chatId, message);
            return TelegramBotUtils.addButtonsToMessage(sendMessage, getMainMenuChooseButtons());
        }
        return TelegramBotUtils.createSendMessage(chatId, INCORRECT_INPUT);
    }

    @NotNull
    private static SendMessage getProductMissingSendMessage(Long chatId) {
        return TelegramBotUtils.createSendMessage(chatId,
                PRODUCT_NOT_FOUND + "\n" + REGISTER_PRODUCT);
    }

    private List<String> getAnotherProductOrCompleteButtons() {
        return List.of(ADD_ANOTHER_PRODUCT, COMPLETE_MEAL);
    }

    private List<String> getMainMenuChooseButtons() {
        return List.of(NEW_MEAL, NEW_PRODUCT, TODAY_TOTAL, TODAY_REMAINDER, TelegramBotService.EXIT);
    }

}
