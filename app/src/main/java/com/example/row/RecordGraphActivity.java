package com.example.row;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.row.implementation.*;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.time.*;
import java.util.*;

public class RecordGraphActivity extends AppCompatActivity {
    int curDist;
    Records records;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_record_graph);
        SeekBar seekBar = findViewById(R.id.seekBar);
        TextView distanceText = findViewById(R.id.distance_display);
        LineChart linechart = findViewById(R.id.lineChartTime);

        Bundle extras = getIntent().getExtras();
        if (!extras.isEmpty()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                records = extras.getParcelable("records", Records.class);
            }
            graph(linechart, records);
        }

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                switch(progress) {
                    case 0:
                        curDist = 500; break;
                    case 1:
                        curDist = 1000; break;
                    case 2:
                        curDist = 2000; break;
                }
                distanceText.setText(Integer.toString(curDist));
                graph(linechart, records);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public void recordsTransition(View v) {
        Intent records_Intent = new Intent(this, RecordsActivity.class);
        startActivity(records_Intent);
    }
    public void toMain(View v) {
        Intent main_Intent = new Intent(this, MainActivity.class);
        startActivity(main_Intent);
    }

    public void graph(LineChart lineChart, Records records) {
        List<Entry> entries = new ArrayList<>();
        List<SingleRecord> recordList = records.getRecordsByDistance(curDist);
        for (SingleRecord rec: recordList) {  //converting recordList to entry list
            entries.add(new Entry(rec.getDateTime().atZone(ZoneId.systemDefault()).toEpochSecond(), rec.getTimeTaken()));
        }
        LineDataSet data = new LineDataSet(entries, "Date");
        data.setColor(Color.BLUE);
        LineData lineData = new LineData(data);
        lineChart.setData(lineData);
        lineChart.invalidate();         //refresh
    }
}