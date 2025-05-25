package com.example.row.implementation;

import java.util.HashMap;
import java.util.Map;
import java.time.LocalDate;

public class DayInfo {
    private Map <LocalDate, Flags> flag;
    private Map <LocalDate, Wind> wind;
    private Map <LocalDate, Weather> weather;

    public DayInfo() {
        flag = new HashMap<LocalDate, Flags>();
        wind = new HashMap<LocalDate, Wind>();
        weather = new HashMap<LocalDate, Weather>();
    }

    public void addInfo(LocalDate date, Flags flag, Wind wind, Weather weather) {
        this.flag.put(date, flag);
        this.wind.put(date, wind);
        this.weather.put(date, weather);
    }

    public Flags getFlag(LocalDate date) {
        return this.flag.get(date);
    }

    public Wind getWind(LocalDate date) {
        return this.wind.get(date);
    }

    public Weather getWeather(LocalDate date) {
        return this.weather.get(date);
    }
}
