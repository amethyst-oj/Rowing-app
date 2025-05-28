package com.example.row;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.*;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.row.implementation.Coordinates;
import com.example.row.implementation.WindData;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_map);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ImageView compass = findViewById(R.id.compassDisplay);
        TextView windSpeed = findViewById(R.id.windSpeed);
        TextView windDirection = findViewById(R.id.windDirection);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            WindData wind = new WindData();
            new Handler(Looper.getMainLooper()).post(() -> {
                int angle = (int) Math.round(wind.getWindDirection());
                windDirection.setText(String.format("%d °", angle));
                compass.setRotation(-35 + angle);
                windSpeed.setText(String.format("%d km/h", Math.round(wind.getWindSpeed())));
                ;
            });
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        ArrayList<ArrayList<Double>> coords = Coordinates.getCoords();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        ArrayList<PolylineOptions> polylines = new ArrayList();
        executor.execute(() -> {
            WindData wind = new WindData();
            for (int i = 0; i < coords.size() - 1; i++) {
                ArrayList<Double> cur = coords.get(i);
                ArrayList<Double> next = coords.get(i + 1);
                String color = wind.getWindImpactMagnitudeColour(cur.get(0), cur.get(1), next.get(0), next.get(1));      //TODO Latitude, longitude double
                PolylineOptions line = new PolylineOptions()
                        .add(new LatLng(cur.get(1), cur.get(0)))
                        .add(new LatLng(next.get(1), next.get(0)))
                        .color(Color.parseColor(color));
                polylines.add(line);
            }
            new Handler(Looper.getMainLooper()).post(() -> {
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(52.214028, 0.129261), 13));
                for (PolylineOptions line : polylines) {
                    googleMap.addPolyline(line);
                }
            });
        });
    }

    public void toMain(View v) {
        Intent main_Intent = new Intent(this, MainActivity.class);
        startActivity(main_Intent);

    }
}