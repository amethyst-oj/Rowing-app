package com.example.row;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.*;

import com.example.row.implementation.Records;
import com.example.row.implementation.RecordsAdapter;

import java.time.LocalDateTime;

public class RecordsActivity extends AppCompatActivity {

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
        Records temp = new Records(); //TODO TEMPORARY
        try {
            temp.newRecord(LocalDateTime.now(), 50, 50);
        } catch (Records.RecordOverlapException e) {
            throw new RuntimeException(e);
        }
        RecyclerView.Adapter recAdapter = new RecordsAdapter(temp);
        recyclerView.setAdapter(recAdapter);
    }
    public void toMain(View v) {
        Intent main_Intent = new Intent(this, MainActivity.class);
        startActivity(main_Intent);
    }
    public void addNewRecord(View v) {
        View submitButton = findViewById(R.id.submit);
        View dateInput = findViewById(R.id.date_input);
        View timeTakenInput = findViewById(R.id.time_input);
        View distInput = findViewById(R.id.dist_input);
        View recyclerView = findViewById(R.id.mlist);
        recyclerView.setVisibility(View.GONE);
        submitButton.setVisibility(View.VISIBLE);
        dateInput.setVisibility(View.VISIBLE);
        timeTakenInput.setVisibility(View.VISIBLE);
        distInput.setVisibility(View.VISIBLE);
    }
    public void submitRecord (View v) {
        View submitButton = findViewById(R.id.submit);
        EditText dateInput = findViewById(R.id.date_input);
        EditText timeTakenInput = findViewById(R.id.time_input);
        EditText distInput = findViewById(R.id.dist_input);
        View recyclerView = findViewById(R.id.mlist);
        String[] recordDetails = new String[] {
                dateInput.getText().toString(), distInput.getText().toString(), timeTakenInput.getText().toString()};
        //TODO add new record with inputs, clean inputs
        submitButton.setVisibility(View.GONE);
        dateInput.setVisibility(View.GONE);
        timeTakenInput.setVisibility(View.GONE);
        distInput.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    public void toGraph(View v) {
        Intent graphIntent = new Intent(this, RecordGraphActivity.class);
        // graphIntent.putExtra(); // TODO put record info
        startActivity(graphIntent);
    }
}