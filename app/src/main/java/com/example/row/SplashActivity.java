package com.example.row;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.row.implementation.BundleData;
import com.example.row.implementation.BundleSingleton;
import com.example.row.implementation.CombinedWeatherSmile;
import com.example.row.implementation.Flags;
import com.example.row.implementation.Weather;
import com.example.row.implementation.WindData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.mikephil.charting.charts.LineChart;

import java.io.IOException;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            CombinedWeatherSmile weather = new CombinedWeatherSmile();  // Does network call inside
            LocalTime key = LocalTime.now().withMinute(0).withSecond(0).withNano(0);
            String flagColor;
            String flagInfo;
            try {
                flagColor = Flags.getFlagColour();
                flagInfo= Flags.getFlagInfo();
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

            HashMap<LocalTime, String> weatherState = null;
            HashMap<LocalTime, Double> tempState = null;
            try {
                String apiKey = "c283fac38f0347adb3b154902252705";
                String location = "Cambridge";
                weatherState = weather.getGeneralWeatherState(apiKey,location);
                tempState= weather.getExternalTemperatureData();
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            HashMap<LocalTime, Double> uvValues = weather.getUVData();
            String finalFlagColor = flagColor;

            HashMap<LocalTime, String> finalWeatherState = weatherState;
            BundleData bundle=  new BundleData(finalFlagColor, windDirection, windSpeed,
                    finalWeatherState, uvValues,tempState, weather.getSunrise(), weather.getSunset(),
                    weather.getChanceOfRain(), flagInfo);

            BundleSingleton.getInstance(bundle);
            new Handler(Looper.getMainLooper()).postDelayed(()-> {
                    this.runOnUiThread(() -> {
                    Intent intent= new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                });
            },3000);
        });

    }
}
