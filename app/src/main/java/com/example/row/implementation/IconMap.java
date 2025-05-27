package com.example.row.implementation;

import com.example.row.R;

import java.util.HashMap;
import java.util.Map;

public class IconMap {
    private static final Map<String, Integer> iconMap = new HashMap<String, Integer>() {{  //map for weather icons
        put("clear_night", R.drawable.clear_night);
        put("cloudy", R.drawable.cloudy);
        put("drizzle", R.drawable.drizzle);
        put("heavy_rain", R.drawable.heavy_rain);
        put("mostly_cloudy_day", R.drawable.mostly_cloudy_day);
        put("blowing_snow", R.drawable.blowing_snow);
        put("flurries", R.drawable.flurries);
        put("haze_fog_dust_smoke", R.drawable.haze_fog_dust_smoke);
        put("heavy_snow", R.drawable.heavy_snow);
        put("isolated_scattered_tstorms_day", R.drawable.isolated_scattered_tstorms_day);
        put("isolated_scattered_tstorms_night", R.drawable.isolated_scattered_tstorms_night);
        put("mostly_clear_night", R.drawable.mostly_clear_night);
        put("mostly_cloudy_night", R.drawable.mostly_cloudy_night);
        put("mostly_sunny", R.drawable.mostly_sunny);
        put("partly_cloudy", R.drawable.partly_cloudy);
        put("partly_cloudy_night", R.drawable.partly_cloudy_night);
        put("scattered_showers_day", R.drawable.scattered_showers_day);
        put("scattered_showers_night", R.drawable.scattered_showers_night);
        put("showers_rain", R.drawable.showers_rain);
        put("sleet_hail", R.drawable.sleet_hail);
        put("snow_showers_snow", R.drawable.snow_showers_snow);
        put("strong_tstorms", R.drawable.strong_tstorms);
        put("sunny", R.drawable.sunny);
        put("tornado", R.drawable.tornado);
        put("wintry_mix_rain_snow", R.drawable.wintry_mix_rain_snow);

        put("Sunny",R.drawable.sunny);
        put("Clear",R.drawable.clear_night);
        put("Partly cloudy", R.drawable.partly_cloudy);
        put("Cloudy", R.drawable.cloudy);
        put("Overcast",R.drawable.partly_cloudy);
        put("Mist",R.drawable.heavy_snow);
        put("Patchy rain possible",R.drawable.showers_rain);
        put("Patchy snow possible",R.drawable.snow_showers_snow);
        put("Patchy sleet possible",R.drawable.sleet_hail);
        put("Patchy freezing drizzle possible",R.drawable.drizzle);
        put("Thundery outbreaks possible",R.drawable.thunder);
        put("Blowing snow", R.drawable.blowing_snow);
        put("Blizzard",R.drawable.snow_showers_snow);
        put("Fog",R.drawable.haze_fog_dust_smoke);
        put("Freezing fog",R.drawable.wintry_mix_rain_snow);
        put("Patchy light drizzle",R.drawable.drizzle);
        put("Light drizzle",R.drawable.drizzle);
        put("Freezing drizzle",R.drawable.drizzle);
        put("Heavy freezing drizzle",R.drawable.drizzle);
        put("Patchy light rain",R.drawable.rain);
        put("Light rain", R.drawable.showers_rain);
        put("Moderate rain at times",R.drawable.rain);
        put("Moderate rain",R.drawable.rain);
        put("Heavy rain at times",R.drawable.heavy_rain);
        put("Heavy rain",R.drawable.heavy_rain);
        put("Light freezing rain",R.drawable.drizzle);
        put("Moderate or heavy freezing rain",R.drawable.heavy_rain);
        put("Light sleet",R.drawable.sleet);
        put("Moderate or heavy sleet",R.drawable.sleet_hail);
        put("Patchy light snow",R.drawable.snow_showers_snow);
        put("Light snow",R.drawable.snow_showers_snow);
        put("Moderate snow",R.drawable.snow_showers_snow);
        put("Patchy heavy snow",R.drawable.snow_showers_snow);
        put("Heavy snow",R.drawable.heavy_snow);
        put("Ice pellets",R.drawable.hail);
        put("Light rain shower",R.drawable.drizzle);
        put("Moderate or heavy rain shower",R.drawable.heavy_rain);
        put("Torrential rain shower",R.drawable.heavy_rain);
        put("Light sleet showers",R.drawable.sleet);
        put("Moderate or heavy sleet showers",R.drawable.sleet_hail);
        put("Light snow showers",R.drawable.snow_showers_snow);
        put("Moderate or heavy snow showers",R.drawable.snow_showers_snow);
        put("Light showers of ice pellets",R.drawable.sleet);
        put("Moderate or heavy showers of ice pellets",R.drawable.sleet_hail);
        put("Patchy light rain with thunder",R.drawable.showers_rain);
        put("Moderate or heavy rain with thunder",R.drawable.thunder);
        put("Patchy light snow with thunder",R.drawable.snow_showers_snow);
        put("Moderate or heavy snow with thunder",R.drawable.thunder);

    }};

    public static Map<String, Integer> getIconMap() {
        return iconMap;
    }
}
