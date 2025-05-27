package com.example.row.implementation;

import com.example.row.R;

import java.util.HashMap;
import java.util.Map;

public class IconMap {
    private static Map<String, Integer> iconMap = new HashMap<String, Integer>() {{  //map for weather icons
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
    }};

    public static Map<String, Integer> getIconMap() {
        return iconMap;
    }
}
