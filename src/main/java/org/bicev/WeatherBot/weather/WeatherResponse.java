package org.bicev.WeatherBot.weather;

/**
 * This class is used to convert weatherstack API response to a Java object.
 */
public class WeatherResponse {

    private Current current;

    public Current getCurrent() {
        return current;
    }

    public void setCurrent(Current current) {
        this.current = current;
    }

}
