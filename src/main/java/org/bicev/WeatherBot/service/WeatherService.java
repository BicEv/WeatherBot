package org.bicev.WeatherBot.service;

import org.bicev.WeatherBot.weather.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {

    @Autowired
    private RestTemplate restTemplate;

    public WeatherResponse getWeather(String city) {
        String apiKey = "7ac18eb2ff80ffad0b3e35693508eb3a";
        String url = "http://api.weatherstack.com/current?access_key=" + apiKey + "&query=" + city.replace('-', ' ')
                + "&units=m";

        System.out.println("Sending request to: " + url);
        return restTemplate.getForObject(url, WeatherResponse.class);
    }

}
