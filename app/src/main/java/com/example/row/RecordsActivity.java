package com.example.row;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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
        RecordsStore store = (RecordsStore) getApplicationContext();
        records = store.getRecords();
        if (records==null) {
            try {
                records = new Records();
                records.newRecord(LocalDateTime.now(), 1000, 50);
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
        try {
            int distVal = Integer.parseInt(distInput.getText().toString().trim());
            int timeVal = Integer.parseInt(timeTakenInput.getText().toString().trim());
            LocalDate intermediateDate = LocalDate.parse(dateInput.getText().toString().trim());
            LocalDateTime date = intermediateDate.atStartOfDay();;
            records.newRecord(date,distVal,timeVal);
            RecordsStore store = (RecordsStore) getApplicationContext();
            store.setRecords(records);

        }catch (DateTimeParseException nfe){
            Toast.makeText(this, "Invalid data input", Toast.LENGTH_SHORT).show();
        }

        submitButton.setVisibility(View.GONE);
        dateInput.setVisibility(View.GONE);
        timeTakenInput.setVisibility(View.GONE);
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