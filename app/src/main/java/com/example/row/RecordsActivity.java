package com.example.row;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.*;

import com.example.row.implementation.Records;
import com.example.row.implementation.RecordsAdapter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RecordsActivity extends AppCompatActivity {
    Records records;
    private RecordsAdapter recAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.records);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        RecyclerView recyclerView = findViewById(R.id.mlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        records = new Records();
        try {
            records.newRecord(LocalDateTime.now(), 0, 0);
        } catch (Records.RecordOverlapException e) {
            throw new RuntimeException(e);
        }
        recAdapter = new RecordsAdapter(records);
        recyclerView.setAdapter(recAdapter);
    }
    public void addNewRecord(View v) {
        View submitButton = findViewById(R.id.submit);
        EditText dateInput = findViewById(R.id.calendar);
        View timeTakenInput = findViewById(R.id.time_input);
        View distInput = findViewById(R.id.dist_input);
        View recyclerView = findViewById(R.id.mlist);
        recyclerView.setVisibility(View.GONE);
        submitButton.setVisibility(View.VISIBLE);
        dateInput.setVisibility(View.VISIBLE);
        timeTakenInput.setVisibility(View.VISIBLE);
        distInput.setVisibility(View.VISIBLE);
    }
    public void submitRecord (View v) throws Records.RecordOverlapException {
        View submitButton = findViewById(R.id.submit);
        EditText dateInput = findViewById(R.id.calendar);
        EditText timeTakenInput = findViewById(R.id.time_input);
        EditText distInput = findViewById(R.id.dist_input);
        View recyclerView = findViewById(R.id.mlist);
        int distVal = Integer.parseInt(distInput.getText().toString().trim());
        int timeVal = Integer.parseInt(timeTakenInput.getText().toString().trim());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate intermediateDate = LocalDate.parse(dateInput.getText().toString().trim(), formatter);
        LocalDateTime date = intermediateDate.atStartOfDay();;
        records.newRecord(date,distVal,timeVal);
        submitButton.setVisibility(View.GONE);
        dateInput.setVisibility(View.GONE);
        timeTakenInput.setVisibility(View.GONE);
        distInput.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        recAdapter.refresh();
;    }

    public void toMain(View v) {
        Intent main_Intent = new Intent(this, MainActivity.class);
        startActivity(main_Intent);
    }

    public void toGraph(View v) {
        Intent graphIntent = new Intent(this, RecordGraphActivity.class);
        graphIntent.putExtra("records",records);
        startActivity(graphIntent);
    }
}