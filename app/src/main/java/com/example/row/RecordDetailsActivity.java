package com.example.row;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.row.implementation.CombinedWeatherSmile;
import com.example.row.implementation.IconMap;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;

public class RecordDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_record_details);
        TextView timeDisplay = findViewById(R.id.timeVal);
        TextView distanceDisplay = findViewById(R.id.distanceVal);
        TextView speedDisplay = findViewById(R.id.speedVal);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //initialize(LocalDateTime.now());
        Bundle extras = getIntent().getExtras();
        if (extras!=null) {
            String time = extras.getString("time");
            String date = extras.getString("date");
            String distance = extras.getString("distance");
            timeDisplay.setText(time+"s");
            distanceDisplay.setText(distance+"m");
            speedDisplay.setText(Float.parseFloat(distance)/Float.parseFloat(time)+"m/s");
        }
    }
    public void recordsTransition(View v) {
        Intent records_Intent = new Intent(this, RecordsActivity.class);
        startActivity(records_Intent);
    }
    public void toMain(View v) {
        Intent main_Intent = new Intent(this, MainActivity.class);
        startActivity(main_Intent);
    }
    public void initialize(LocalDateTime currentTime) {
        CombinedWeatherSmile weather = new CombinedWeatherSmile();
        ImageView weatherNow = findViewById(R.id.weather0);         //finding views for widgets
        ImageView weather1hr = findViewById(R.id.weather1);
        ImageView weather2hr = findViewById(R.id.weather2);
        ImageView weather3hr = findViewById(R.id.weather3);
        ImageView weather4hr = findViewById(R.id.weather4);
        TextView sunriseTime = findViewById(R.id.sunriseTime);
        TextView sunsetTime = findViewById(R.id.sunsetTime);
        TextView rainChance = findViewById(R.id.rainChance);

        Map<LocalTime, String> weatherState = weather.getGeneralWeatherState();
        int[] weathers = new int[5];  //get weather states for next 4 hours
        for (int i=0; i<5;i++) {
            String icon = weatherState.get(currentTime.plusHours(i));
            int resID = IconMap.getIconMap().get(icon);
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
    }
}