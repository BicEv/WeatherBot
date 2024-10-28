package org.bicev.WeatherBot.service;

import org.bicev.WeatherBot.weather.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * A service to get the current weather from the weatherstack API
 */
@Service
public class WeatherService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${weatherstack.api.key}")
    private String apiKey;

    /**
     * This method used to get the current weather by city's name
     * 
     * @param city the name of the city
     * @return {@link WeatherResponse} for a cuurent weather in the city
     */
    public WeatherResponse getWeather(String city) {
        String url = "http://api.weatherstack.com/current?access_key=" + apiKey + "&query=" + city.replace('-', ' ')
                + "&units=m";

        System.out.println("Sending request to: " + url);
        return restTemplate.getForObject(url, WeatherResponse.class);
    }

}
