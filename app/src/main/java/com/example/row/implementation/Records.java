package com.example.row.implementation;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.time.LocalDate;


public class Records implements Parcelable {
    ;
    private List<SingleRecord> records = new ArrayList<>(); // Newest first;
    int count = 0;

    public static final Creator<Records> CREATOR = new Creator<Records>() {
        @Override
        public Records createFromParcel(Parcel in) {
            return new Records(in);
        }

        @Override
        public Records[] newArray(int size) {
            return new Records[size];
        }
    };

    public Records() {

    }
    protected Records(Parcel in) {
        records = in.createTypedArrayList(SingleRecord.CREATOR);
        count = records != null ? records.size() : 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeTypedList(records);
    }

    public static class RecordOverlapException extends Exception {
        public RecordOverlapException(LocalDateTime dateTime) {
            super("Record overlaps at: " + dateTime);
        }
    }
    public static class RecordNotFoundException extends Exception {
        public RecordNotFoundException() {
            super("Record not found");
        }
    }


    public void newRecord(LocalDateTime dateTime, int distance, int timeTaken) throws RecordOverlapException { // Adds a new record, ensuring not on the same date and time as any other
        SingleRecord record = new SingleRecord(dateTime, distance, timeTaken);
        int current = 0;
        if (count == 0) { records.add(record);} // No records yet
        else{
            while (current < records.size() && dateTime.isBefore(records.get(current).getDateTime())) { // Make sure it is put after any newer records
                current++;
            }
            if (current != records.size() && dateTime.isEqual(records.get(current).getDateTime())){ // Record has an overlapping DateTime
                throw new RecordOverlapException(dateTime);
            }
            else{
                if (current == count){ records.add(record); } // Appending to the end of the list
                else { records.add(current, record); } // Inserting in time order
            }
        }
        count++;
    }

    public SingleRecord getRecord(LocalDateTime dateTime) throws RecordNotFoundException { // Returns a record identified by the date and time
        SingleRecord outRecord = null;
        for (SingleRecord record : records) { // Linearly search for the record
            if (record.getDateTime().equals(dateTime)) {
                outRecord = record;
            }
        }
        if (outRecord == null){ throw new RecordNotFoundException(); }
        else { return outRecord; }
    }

    public List<SingleRecord> getRecordsByDistance(int distanceMeters){ // Newest first
        List<SingleRecord> outList = new ArrayList<>();
        for (SingleRecord record : records) {
            if (record.getDistance() == distanceMeters){
                outList.add(record);
            }
        }
        return outList;
    }

    public List<SingleRecord> getRecordsChronologically(){ // Newest first
        return records;
    }

    public List<SingleRecord> getRecordsByWind(double windSpeed, double windAngle, DayInfo dayInfo){ // Newest first. Returns all records with wind angle and speed within 10% of current days data
        List<SingleRecord> outList = new ArrayList<>();
        for (SingleRecord record : records) {
            LocalDate day = record.getDateTime().toLocalDate();
            double daySpeed = dayInfo.getWind(day).getWindSpeed();
            double dayAngle = dayInfo.getWind(day).getWindDirection();
            if (Math.abs(1 - windSpeed / daySpeed) < 0.1 && Math.abs(1 - windAngle / dayAngle) < 0.1){
                outList.add(record);
            }
        }
        return outList;
    }

    public List<SingleRecord> getRecordsByTemp(int temp, DayInfo dayInfo){
        List<SingleRecord> outList = new ArrayList<>();
        for (SingleRecord record : records) {
            LocalDate day = record.getDateTime().toLocalDate();
            LocalTime time = record.getDateTime().toLocalTime();
            double dayTemp = dayInfo.getWeather(day).getExternalTemperatureData().get(time);
            if (Math.abs(1 - temp / dayTemp) < 0.1){ outList.add(record); }
        }
        return outList;
    }

}
