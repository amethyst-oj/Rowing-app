package com.example.row.implementation;

import android.os.Parcel;
import android.os.Parcelable;

import java.time.LocalDateTime;

public class SingleRecord implements Parcelable {
    private LocalDateTime time;
    private int distance;
    private int timeTaken;
    private double speed;

    public SingleRecord(LocalDateTime time, int distance, int timeTaken) {
        this.time = time;
        this.distance = distance;
        this.timeTaken = timeTaken;
        this.speed = (double) distance / ((double) timeTaken); // Speed = distance over time
    }

    public LocalDateTime getDateTime() { return time; }
    public int getDistance() { return distance; } // Commonly 500, 1000, 2000
    public int getTimeTaken() { return timeTaken; } // in seconds
    public double getSpeed() { return speed; }

    protected SingleRecord(Parcel in) {
        time = LocalDateTime.parse(in.readString());
        distance = in.readInt();
        timeTaken = in.readInt();
        speed = (double) distance / timeTaken;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(time.toString());
        dest.writeInt(distance);
        dest.writeInt(timeTaken);
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
