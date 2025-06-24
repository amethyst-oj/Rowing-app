package com.example.row;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.*;

import com.example.row.implementation.Records;
import com.example.row.implementation.RecordsAdapter;
import com.example.row.implementation.RecordsStore;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class RecordsActivity extends AppCompatActivity {
    Records records;
    DatePicker datePicker;
    NumberPicker minutesPicker;
    NumberPicker secondsPicker;
    Button dateButton;
    Button timeButton;
    LocalDateTime selectedDate;
    String time;
    int minutes=0;
    int seconds=0;
    private RecordsAdapter recAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.records);
        datePicker= findViewById(R.id.datePicker);
        minutesPicker = findViewById(R.id.minutes_picker);
        secondsPicker= findViewById(R.id.seconds_picker);
        secondsPicker.setMinValue(0);
        secondsPicker.setMaxValue(59);
        minutesPicker.setMinValue(0);
        minutesPicker.setMaxValue(59);
        dateButton= findViewById(R.id.date_button);
        timeButton = findViewById(R.id.time_button);
        dateButton.setOnClickListener(view -> {
            LocalDate today = LocalDate.now();
            dateButton.setVisibility(View.INVISIBLE);
            datePicker.setVisibility(View.VISIBLE);
            datePicker.init(today.getYear(), today.getMonthValue() - 1, today.getDayOfMonth(), (datePicker, year, month, dayOfMonth) ->
                    selectedDate = LocalDateTime.of(year, month + 1, dayOfMonth, 0, 0));
        });
        timeButton.setOnClickListener(view -> {
                    timeButton.setVisibility(View.INVISIBLE);
                    minutesPicker.setVisibility(View.VISIBLE);
                    secondsPicker.setVisibility(View.VISIBLE);
                    minutesPicker.setOnValueChangedListener((picker, oldVal, newVal) -> {
                        minutes= newVal;
                    });
                    secondsPicker.setOnValueChangedListener((picker, oldVal, newVal) -> {
                        seconds=newVal;
                        time = String.format("%02d:%02d", minutes, seconds);
                    });
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RecordsStore store = (RecordsStore) getApplicationContext();
        records = store.getRecords();
        if (records==null) {
            try {
                records = new Records();
                records.newRecord(LocalDateTime.now(), 1000, "25:00");
                store.setRecords(records);
            } catch (Records.RecordOverlapException e) {
                throw new RuntimeException(e);
            }
        }
        RecyclerView recyclerView = findViewById(R.id.mlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recAdapter = new RecordsAdapter(records);
        recyclerView.setAdapter(recAdapter);
    }


    public void addNewRecord(View v) {
        View submitButton = findViewById(R.id.submit);
        Button dateButton = findViewById(R.id.date_button);
        View distInput = findViewById(R.id.dist_input);
        Button timeButton = findViewById(R.id.time_button);
        View recyclerView = findViewById(R.id.mlist);
        recyclerView.setVisibility(View.GONE);
        submitButton.setVisibility(View.VISIBLE);
        dateButton.setVisibility(View.VISIBLE);
        timeButton.setVisibility(View.VISIBLE);
        distInput.setVisibility(View.VISIBLE);
    }

    public void submitRecord (View v) throws Records.RecordOverlapException {
        View submitButton = findViewById(R.id.submit);
        EditText distInput = findViewById(R.id.dist_input);
        View recyclerView = findViewById(R.id.mlist);
        try {
            int distVal = Integer.parseInt(distInput.getText().toString().trim());
            records.newRecord(selectedDate,distVal,time);
            RecordsStore store = (RecordsStore) getApplicationContext();
            store.setRecords(records);
        }catch (DateTimeParseException nfe){
            Toast.makeText(this, "Invalid data input", Toast.LENGTH_SHORT).show();
        }

        submitButton.setVisibility(View.GONE);
        datePicker.setVisibility(View.GONE);
        minutesPicker.setVisibility(View.GONE);
        secondsPicker.setVisibility(View.GONE);
        distInput.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        recAdapter.refresh(records);
    }

    public void toMain(View v) {
        Intent main_Intent = new Intent(this, MainActivity.class);
        startActivity(main_Intent);
    }

    public void toGraph(View v) {
        Intent graphIntent = new Intent(this, RecordGraphActivity.class);

        startActivity(graphIntent);
    }
}