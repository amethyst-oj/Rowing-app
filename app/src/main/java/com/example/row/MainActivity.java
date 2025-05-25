package com.example.row;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.row.implementation.CombinedWeatherSmile;
import com.example.row.implementation.Flags;
import com.example.row.implementation.WindData;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.*;

import java.io.IOException;
import java.time.LocalTime;
import java.util.*;

public class MainActivity extends AppCompatActivity {

    Map<String, Integer> iconMap = new HashMap<String, Integer>() {{  //map for weather icons
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ImageButton prevDay = findViewById(R.id.previous_day);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        try {
            toPrevDay(prevDay);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void recordsTransition(View v) {
        Intent records_Intent = new Intent(this, RecordsActivity.class);
        startActivity(records_Intent);
    }

    public void mapTransition(View v) {
        Intent map_Intent = new Intent(this, MapActivity.class);
        startActivity(map_Intent);
    }

    public void toNextDay(View v) throws IOException {
        ImageButton nextDay = findViewById(R.id.next_day);
        ImageButton prevDay = findViewById(R.id.previous_day);
        TextView day = findViewById(R.id.day);
        nextDay.setVisibility(View.GONE);
        prevDay.setVisibility(View.VISIBLE);
        day.setText("TOMORROW");
        initializeData(LocalTime.MAX);
    }
    public void toPrevDay(View v) throws IOException {
        ImageButton nextDay = findViewById(R.id.next_day);
        ImageButton prevDay = findViewById(R.id.previous_day);
        TextView day = findViewById(R.id.day);
        nextDay.setVisibility(View.VISIBLE);
        prevDay.setVisibility(View.GONE);
        day.setText("TODAY");
        initializeData(LocalTime.now());
    }

    public void initializeData(LocalTime currentTime) throws IOException {
        ImageView thermometer = findViewById(R.id.temperature);  //finding views
        ImageView flagIcon = findViewById(R.id.flag);
        ImageView compass = findViewById(R.id.compass);
        TextView temperatureText = findViewById(R.id.temperatureText);
        TextView windText = findViewById(R.id.windText);

        CombinedWeatherSmile weather = new CombinedWeatherSmile();      //using Dhruv's weather class
        long curTemperature = Math.round(weather.getExternalTemperatureData().get(currentTime));
        String thermoColor;
        if (curTemperature > 25) {
            thermoColor = "red";
        } else if (curTemperature <= 10) {
            thermoColor = "blue";
        } else {
            thermoColor = "yellow";
        }
        Flags.getFlagColour();
        String flagColor = Flags.getFlag();
        if (flagColor == "Red/Yellow") {
            flagColor = "orange";
        }
        WindData wind = new WindData();
        double windDirection = wind.getWindDirection();
        double windSpeed = wind.getWindSpeed();

        compass.setRotation(Math.round(-35 + windDirection));
        windText.setText((int) Math.round(windSpeed) + "KM/H");
        int realThermoColor = Color.parseColor(thermoColor);
        int realFlagColor = Color.parseColor(flagColor);
        thermometer.setImageTintList(ColorStateList.valueOf(realThermoColor));
        flagIcon.setImageTintList(ColorStateList.valueOf(realFlagColor));
        temperatureText.setText((int) curTemperature + "°");        //initialized 3 big icon color, orientation and text

        ImageView weatherNow = findViewById(R.id.weather0);         //finding views for widgets
        ImageView weather1hr = findViewById(R.id.weather1);
        ImageView weather2hr = findViewById(R.id.weather2);
        ImageView weather3hr = findViewById(R.id.weather3);
        ImageView weather4hr = findViewById(R.id.weather4);
        TextView sunriseTime = findViewById(R.id.sunriseTime);
        TextView sunsetTime = findViewById(R.id.sunsetTime);
        TextView rainChance = findViewById(R.id.rainChance);
        TextView uvValue = findViewById(R.id.currentUVValue);
        LineChart lineChart = findViewById(R.id.lineChart);

        Map<LocalTime, String> weatherState = weather.getGeneralWeatherState();
        Map<LocalTime, Double> uvValues = weather.getUVData();

        int[] weathers = new int[5];  //get weather states for next 4 hours
        for (int i=0; i<5;i++) {
            String icon = weatherState.get(currentTime.plusHours(i));
            int resID = iconMap.get(icon);
            weathers[i] = resID;
        }
        weatherNow.setImageResource(weathers[0]);  //set weather icons
        weather1hr.setImageResource(weathers[1]);
        weather2hr.setImageResource(weathers[2]);
        weather3hr.setImageResource(weathers[3]);
        weather4hr.setImageResource(weathers[4]);

        sunriseTime.setText(weather.getSunrise());  //set widget data
        sunsetTime.setText(weather.getSunset());
        rainChance.setText(weather.getChanceOfRain());
        uvValue.setText((int) Math.round(uvValues.get(currentTime)));

        uvGraph(lineChart,uvValues);  //draw graph
    }

    public void uvGraph(LineChart lineChart, Map<LocalTime, Double> uvValues) {
        List<Entry> entries = new ArrayList<>();
        for (Map.Entry<LocalTime, Double> entry : uvValues.entrySet()) {  //converting map to entry list
            entries.add(new Entry(entry.getKey().getHour(), entry.getValue().floatValue()));
        }
        LineDataSet data = new LineDataSet(entries, "UV by hour");
        data.setColor(Color.BLUE);
        LineData lineData = new LineData(data);
        lineChart.setData(lineData);
        lineChart.invalidate();         //refresh
    }
}