package org.bicev.WeatherBot.config;

import org.bicev.WeatherBot.bot.WeatherBot;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

/**
 * Configuration class for {@link TelegramBotsApi} and {@link RestTemplate}
 * <p>
 * This class is responsible for setting up and creating necessary beans for
 * interaction with Telegram API and sending HTTP-requests to a weather API
 * </p>
 */
@Configuration
public class WeatherBotConfiguration {

    /**
     * Configures and registers the bot in the Telegram API
     * <p>
     * Uses {@link DefaultBotSession} for setting a session with Telegram, registers
     * the {@link WeatherBot} by the {@link TelegramBotsApi}
     * </p>
     * 
     * @param weatherBot the bot to be registered
     * @return set up object {@link TelegramBotsApi} to work with Telegram API
     * @throws TelegramApiException if the registration was failed
     */
    @Bean
    public TelegramBotsApi telegramBotsApi(WeatherBot weatherBot) throws TelegramApiException {
        TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
        api.registerBot(weatherBot);
        return api;
    }

    /**
     * Creates a sets up REST-client for HTTP-requests
     * <p>
     * This method creates {@link RestTemplate} instance, which is used to send
     * HTTP-requests to the weather API.
     * </p>
     * 
     * @return instance of {@link RestTemplate} for executing HTTP-requests
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
