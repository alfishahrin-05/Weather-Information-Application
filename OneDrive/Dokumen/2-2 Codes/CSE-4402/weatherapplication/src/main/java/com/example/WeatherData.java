package com.example;

public class WeatherData {
    private String city;
    private String country;
    private double temperature;
    private double feelsLike;
    private int humidity;
    private int cloudCover;
    private double precipitation;
    private String time;
    private double aqi;
    
    // Constructor
    public WeatherData(String city, String country, double temperature, double feelsLike, 
                      int humidity, int cloudCover, double precipitation, String time, double aqi) {
        this.city = city;
        this.country = country;
        this.temperature = temperature;
        this.feelsLike = feelsLike;
        this.humidity = humidity;
        this.cloudCover = cloudCover;
        this.precipitation = precipitation;
        this.time = time;
        this.aqi = aqi;
    }
    
    // Getters
    public String getCity() { return city; }
    public String getCountry() { return country; }
    public double getTemperature() { return temperature; }
    public double getFeelsLike() { return feelsLike; }
    public int getHumidity() { return humidity; }
    public int getCloudCover() { return cloudCover; }
    public double getPrecipitation() { return precipitation; }
    public String getTime() { return time; }
    public double getAqi() { return aqi; }
    
    @Override
    public String toString() {
        return String.format("%s, %s: %.1f°C (feels like %.1f°C), Humidity: %d%%, Precipitation: %.1fmm", 
                            city, country, temperature, feelsLike, humidity, precipitation);
    }
}