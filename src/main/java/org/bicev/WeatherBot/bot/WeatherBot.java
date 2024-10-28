package org.bicev.WeatherBot.bot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bicev.WeatherBot.service.WeatherService;
import org.bicev.WeatherBot.weather.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * The class {@code WeatherBot} is a bot for getting a current weather.
 * <p>
 * This bot is processing user commands and messages to get a current weather in
 * selected cities. User can type "/weather" command and receive an answer with
 * the city's name request, or select one of buttoned cities.
 * </p>
 */
@Component
public class WeatherBot extends TelegramLongPollingBot {

    @Autowired
    private WeatherService weatherService;

    private Map<Long, Boolean> waitingForCity = new HashMap<>();

    @Value("${bot.username}")
    private String botUsername;

    /**
     * Constructor to create a bot with a given token
     * 
     * @param botToken Telegram bot token, gotten from application properties
     */
    public WeatherBot(@Value("${bot.token}") String botToken) {
        super(botToken);
    }

    /**
     * This method is processing incoming messages from Telegram such as text
     * messages and commands
     * <p>
     * - if user sent "/weather", bot asks to enter a city name
     * - if the city was selected, bot sends a current weather
     * - if the incoming message was other, bot sends possible options
     * </p>
     * 
     * @param update incoming update containing message information
     */
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();

            if (messageText.equals("Санкт-Петербург") || messageText.equals("Пушкин")
                    || messageText.equals("Зеленогорск")) {
                sendWeatherForecast(chatId, messageText);
            } else if ("/weather".equals(messageText)) {
                sendMessage(chatId, "Введите город:");
                waitingForCity.put(chatId, true);
            } else if (Boolean.TRUE.equals(waitingForCity.get(chatId))) {
                sendWeatherForecast(chatId, messageText);
                waitingForCity.put(chatId, false);
            } else {
                sendWeatherOptions(chatId);
            }
        }
    }

    /**
     * Sends a current weather in the city or error message, if the weather is not
     * present
     * 
     * @param chatId the ID of the current chat
     * @param city   the city's name
     */
    public void sendWeatherForecast(Long chatId, String city) {
        try {
            WeatherResponse weatherResponse = weatherService.getWeather(city);
            if (weatherResponse == null || weatherResponse.getCurrent() == null) {
                sendMessage(chatId, "Не удалось получить данные о погоде для " + city);
                return;
            }

            String forecast = String.format(
                    "Прогноз погоды в городе %s на сегодня:\nТемпература: %d°C\nСкорость ветра: %d м/с\nВлажность: %d%%\nОблачность: %d%%",
                    city,
                    weatherResponse.getCurrent().getTemperature(),
                    weatherResponse.getCurrent().getWind_speed(),
                    weatherResponse.getCurrent().getHumidity(),
                    weatherResponse.getCurrent().getCloudcover());
            sendMessage(chatId, forecast);

        } catch (Exception e) {
            e.printStackTrace();
            sendMessage(chatId, "Произошла ошибка при получении прогноза для города: " + city);
        }
    }

    /**
     * Creates a 2 buttons rows via {@link ReplyKeyboardMArkup},
     * {@link KeyboardRow} and {@link KeyboardButton}, and sends a message
     * {@link SendMessage} with basic bot instructions
     * 
     * @param chatId the ID of the current chat
     */
    public void sendWeatherOptions(Long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Выберите город или введите его после нажатия на /weather");

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton("Санкт-Петербург"));
        row1.add(new KeyboardButton("Пушкин"));
        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton("Зеленогорск"));
        row2.add(new KeyboardButton("/weather"));

        keyboard.add(row1);
        keyboard.add(row2);

        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);

        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * Private method used to send a message in the chat
     * 
     * @param chatId the ID of the chat
     * @param text   the text of the message
     */
    private void sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the name of the bot
     * 
     * @return the name of the bot
     */
    @Override
    public String getBotUsername() {
        return botUsername;
    }

}
