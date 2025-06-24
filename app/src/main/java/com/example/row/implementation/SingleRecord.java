package com.example.row.implementation;

import android.os.Parcel;
import android.os.Parcelable;

import java.time.LocalDateTime;

public class SingleRecord implements Parcelable {
    private LocalDateTime time;
    private int distance;
    private String timeTaken;
    private int timeInSeconds;
    private double speed;

    public SingleRecord(LocalDateTime time, int distance, String timeTaken) {
        this.time = time;
        this.distance = distance;
        this.timeTaken = timeTaken;
        String[] parts = timeTaken.split(":");
        timeInSeconds = Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]); // Convert to seconds
        this.speed = (double) distance / ((double) timeInSeconds); // Speed = distance over time
    }

    public LocalDateTime getDateTime() { return time; }
    public int getDistance() { return distance; } // Commonly 500, 1000, 2000
    public String getTimeTaken() { return timeTaken; } // in seconds

    public int getTimeInSeconds() { return timeInSeconds; } // in seconds
    public double getSpeed() { return speed; }

    protected SingleRecord(Parcel in) {
        time = LocalDateTime.parse(in.readString());
        distance = in.readInt();
        timeTaken = in.readString();
        speed = (double) distance / timeInSeconds;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(time.toString());
        dest.writeInt(distance);
        dest.writeString(timeTaken);
        dest.writeInt(timeInSeconds);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SingleRecord> CREATOR = new Creator<SingleRecord>() {
        @Override
        public SingleRecord createFromParcel(Parcel in) {
            return new SingleRecord(in);
        }

        @Override
        public SingleRecord[] newArray(int size) {
            return new SingleRecord[size];
        }
    };

}
