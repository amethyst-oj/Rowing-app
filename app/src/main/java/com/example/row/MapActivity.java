package com.example.row;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.row.implementation.Coordinates;
import com.example.row.implementation.WindData;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.ArrayList;

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
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.compass);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        ArrayList<ArrayList<Double>> coords = Coordinates.getCoords();
        WindData wind = new WindData();
        for (int i = 0; i < coords.size() - 1; i++) {
            ArrayList<Double> cur = coords.get(i);
            ArrayList<Double> next = coords.get(i+1);
            //String color =  wind.getWindImpactMagnitudeColour(cur.get(0),cur.get(1),next.get(0),next.get(1))      //TODO Latitude, longitude double
            //
        }
    }
    public void toMain(View v) {
        Intent main_Intent = new Intent(this, MainActivity.class);
        startActivity(main_Intent);
    }
}