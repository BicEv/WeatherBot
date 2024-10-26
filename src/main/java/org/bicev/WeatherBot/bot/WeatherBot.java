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

@Component
public class WeatherBot extends TelegramLongPollingBot {

    @Autowired
    private WeatherService weatherService;
    private Map<Long, Boolean> waitingForCity = new HashMap<>();

    public WeatherBot(@Value("${bot.token}") String botToken) {
        super(botToken);
    }

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

    @Override
    public String getBotUsername() {
        return "BicFirst_bot";
    }

}
