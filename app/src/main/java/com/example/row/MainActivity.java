package com.example.row;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.*;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.row.implementation.CombinedWeatherSmile;
import com.example.row.implementation.Flags;
import com.example.row.implementation.IconMap;
import com.example.row.implementation.WindData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.*;

import java.io.IOException;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

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
            e.printStackTrace();
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
        //initializeData(LocalTime.MAX);
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
        int[] weathers = new int[5];
        // Set hourly weather icons
        ImageView weatherNow = findViewById(R.id.weather0);
        ImageView weather1hr = findViewById(R.id.weather1);
        ImageView weather2hr = findViewById(R.id.weather2);
        ImageView weather3hr = findViewById(R.id.weather3);
        ImageView weather4hr = findViewById(R.id.weather4);

        TextView sunriseTime = findViewById(R.id.sunriseTime);
        TextView sunsetTime = findViewById(R.id.sunsetTime);
        TextView rainChance = findViewById(R.id.rainChance);
        TextView uvValue = findViewById(R.id.currentUVValue);
        LineChart lineChart = findViewById(R.id.lineChart);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            CombinedWeatherSmile weather = new CombinedWeatherSmile();  // Does network call inside
            LocalTime key = currentTime.withMinute(0).withSecond(0).withNano(0);
            int curTemperature = (int) Math.round(weather.getExternalTemperatureData().get(key));
            String thermoColor;
            if (curTemperature > 25) {
                thermoColor = "red";
            } else if (curTemperature <= 10) {
                thermoColor = "blue";
            } else {
                thermoColor = "yellow";
            }

            String finalThermoColor = thermoColor;
            String flagColor;
            try {
                flagColor = Flags.getFlagColour();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }

            if (Objects.equals(flagColor, "Red/Yellow")) {
                flagColor = "orange";
            }

            WindData wind = new WindData();
            double windDirection = wind.getWindDirection();
            double windSpeed = wind.getWindSpeed();

            Map<LocalTime, String> weatherState = null;
            try {
                String apiKey = "c283fac38f0347adb3b154902252705";
                String location = "Cambridge";
                weatherState = weather.getGeneralWeatherState(apiKey,location);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            Map<LocalTime, Double> uvValues = weather.getUVData();
            String finalFlagColor = flagColor;
            int uvVal= (int) Math.round(uvValues.get(key));

            Map<LocalTime, String> finalWeatherState = weatherState;
            new Handler(Looper.getMainLooper()).post(() -> {
                // Now safely update the UI here
                ImageView thermometer = findViewById(R.id.temperature);
                ImageView flagIcon = findViewById(R.id.flag);
                ImageView compass = findViewById(R.id.compass);
                TextView temperatureText = findViewById(R.id.temperatureText);
                TextView windText = findViewById(R.id.windText);

                uvValue.setText(String.valueOf(uvVal));
                uvGraph(lineChart, uvValues);
                ImageView[] whr= {weatherNow,weather1hr,weather2hr,weather3hr,weather4hr};
                int i=0;
                while ( finalWeatherState.get(key.plusHours(i)) != null){
                String icon = finalWeatherState.get(key.plusHours(i));
                int resID = IconMap.getIconMap().get(icon);
                weathers[i] = resID;
                whr[i].setImageResource(weathers[i]);
                i++;
            };

                sunriseTime.setText(weather.getSunrise());
                sunsetTime.setText(weather.getSunset());
                int cor= (weather.getChanceOfRain());
                rainChance.setText(String.valueOf(cor));
                int realThermoColor = Color.parseColor(finalThermoColor);
                int realFlagColor = Color.parseColor(finalFlagColor);
                thermometer.setImageTintList(ColorStateList.valueOf(realThermoColor));
                flagIcon.setImageTintList(ColorStateList.valueOf(realFlagColor));
                compass.setRotation(Math.round(-35 + windDirection));
                windText.setText(String.format(Locale.getDefault(), "%dKM/H", Math.round(windSpeed)));
                temperatureText.setText(String.format(Locale.getDefault(), "%d°", curTemperature));

            });
        });
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