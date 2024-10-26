package org.bicev.WeatherBot.weather;

public class Current {

    private String observationTime;
    private int temperature;
    private int wind_speed;
    private int humidity;
    private int cloudcover;
    public String getObservationTime() {
        return observationTime;
    }
    public void setObservationTime(String observationTime) {
        this.observationTime = observationTime;
    }
    public int getTemperature() {
        return temperature;
    }
    public void setTemperature(int temprerature) {
        this.temperature = temprerature;
    }
    public int getWind_speed() {
        return wind_speed;
    }
    public void setWind_speed(int wind_speed) {
        this.wind_speed = wind_speed;
    }
    public int getHumidity() {
        return humidity;
    }
    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }
    public int getCloudcover() {
        return cloudcover;
    }
    public void setCloudcover(int cloudcover) {
        this.cloudcover = cloudcover;
    }

    

}
