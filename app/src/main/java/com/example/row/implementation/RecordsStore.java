package com.example.row.implementation;

import android.app.Application;

public class RecordsStore extends Application {
    private Records records;

    public Records getRecords() {
        return records;
    }

    public void setRecords(Records records) {
        this.records = records;
    }
}
