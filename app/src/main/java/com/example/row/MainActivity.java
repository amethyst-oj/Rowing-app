package com.example.row;

import android.content.Intent;
import android.content.SharedPreferences;
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

import com.example.row.implementation.BundleData;
import com.example.row.implementation.BundleSingleton;
import com.example.row.implementation.CombinedWeatherSmile;
import com.example.row.implementation.Flags;
import com.bumptech.glide.Glide;
import com.example.row.implementation.Records;
import com.example.row.implementation.WindData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.*;

import org.w3c.dom.Text;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    Records records;
    BundleData bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bundle = BundleSingleton.getInstance();
        EdgeToEdge.enable(this);
        View mainRoot = findViewById(R.id.main);
        ImageButton prevDay = findViewById(R.id.previous_day);
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            bundle = BundleSingleton.getInstance();
            if (bundle != null && bundle.getUVValues() != null) {
                try {
                    toPrevDay(prevDay);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
                ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                    Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                    return insets;
                });
            }
        }, 20);
    }
    public void recordsTransition(View v) {
        Intent records_Intent = new Intent(this, RecordsActivity.class);
        startActivity(records_Intent);
    }

    public void mapTransition(View v) {
        Intent map_Intent = new Intent(this, MapActivity.class);
        startActivity(map_Intent);
    }

    /*public void toNextDay(View v) throws IOException {
     ImageButton nextDay = findViewById(R.id.next_day);
        ImageButton prevDay = findViewById(R.id.previous_day);
        TextView day = findViewById(R.id.day);
        nextDay.setVisibility(View.GONE);
        prevDay.setVisibility(View.VISIBLE);
        day.setText("TOMORROW");
        initializeData(LocalTime.now().plusDays(1));
    }*/
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
        LocalTime key = LocalTime.now().withMinute(0).withSecond(0).withNano(0);
        TextView sunriseTime = findViewById(R.id.sunriseTime);
        TextView sunsetTime = findViewById(R.id.sunsetTime);
        TextView rainChance = findViewById(R.id.rainChance);
        TextView uvValue = findViewById(R.id.currentUVValue);
        LineChart lineChart = findViewById(R.id.lineChart);
            Map<LocalTime, Double> uvValues = bundle.getUVValues();
            String finalFlagColor = bundle.getFlagColor();
            int uvVal= (int) Math.round(uvValues.get(key));

            Map<LocalTime, String> finalWeatherState = bundle.getWeatherState();
            new Handler(Looper.getMainLooper()).post(() -> {
                // Now safely update the UI here
                ImageView thermometer = findViewById(R.id.temperature);
                ImageView flagIcon = findViewById(R.id.flag);
                ImageView compass = findViewById(R.id.map);
                TextView temperatureText = findViewById(R.id.temperatureText);
                TextView windText = findViewById(R.id.windText);
                TextView flagText= findViewById(R.id.currentFlagValue);
                TextView UVText=findViewById(R.id.uvEvaluation);
                flagText.setText(finalFlagColor);
                uvValue.setText(String.valueOf(uvVal));
                uvGraph(lineChart, uvValues);
                ImageView[] whr= {weatherNow,weather1hr,weather2hr,weather3hr,weather4hr};
                int i=0;
                while ( finalWeatherState.get(key.plusHours(i)) != null){
                String iconURL = String.format("https:%s",finalWeatherState.get(key.plusHours(i)));
                Glide.with(this).load(iconURL).into(whr[i]);
                i++;
            };
                Map<LocalTime, Double> allTemperature = bundle.getTemperatureMap();
                int curTemperature = allTemperature.get(key).intValue();
                String thermoColor;
                if (curTemperature > 25) {
                    thermoColor = "red";
                } else if (curTemperature <= 10) {
                    thermoColor = "blue";
                } else {
                    thermoColor = "yellow";
                }

                String finalThermoColor = thermoColor;
                sunriseTime.setText(bundle.getSunrise());
                sunsetTime.setText(bundle.getSunset());
                UVText.setText(bundle.getUVInfo(uvVal));
                int cor= (bundle.getChanceOfRain());
                rainChance.setText("Chance of rain: "+String.valueOf(cor) + "%");
                int realThermoColor = Color.parseColor(finalThermoColor);
                int realFlagColor = Color.parseColor(finalFlagColor);
                thermometer.setImageTintList(ColorStateList.valueOf(realThermoColor));
                flagIcon.setImageTintList(ColorStateList.valueOf(realFlagColor));
                compass.setRotation(Math.round(-35 + bundle.getWindDirection()));
                windText.setText(String.format(Locale.getDefault(), "%dKM/H", Math.round(bundle.getWindSpeed())));
                temperatureText.setText(String.format(Locale.getDefault(), "%d°", curTemperature));

                TextView temp0 = findViewById(R.id.w0text);
                TextView temp1 = findViewById(R.id.w1text);
                TextView temp2 = findViewById(R.id.w2text);
                TextView temp3 = findViewById(R.id.w3text);
                TextView temp4 = findViewById(R.id.w4text);

                TextView time0 = findViewById(R.id.w0texttop);
                TextView time1 = findViewById(R.id.w1texttop);
                TextView time2 = findViewById(R.id.w2texttop);
                TextView time3 = findViewById(R.id.w3texttop);
                TextView time4 = findViewById(R.id.w4texttop);
                TextView[] tempUnderWeathers= {temp0,temp1,temp2,temp3,temp4};
                TextView[] timeOverWeathers= {time0,time1,time2,time3,time4};
                int j=0;
                while (allTemperature.get(key.plusHours(j)) != null){
                    String tempText = String.format("%d °", Math.round(allTemperature.get(key.plusHours(j))));
                    tempUnderWeathers[j].setText(tempText);
                    timeOverWeathers[j].setText(String.valueOf(key.plusHours(j)));
                    j++;

                }
                View mainRoot = findViewById(R.id.main);
                mainRoot.setVisibility(View.VISIBLE);
            });
        };


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
