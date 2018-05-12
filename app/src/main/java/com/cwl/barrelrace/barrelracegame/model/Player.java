package com.cwl.barrelrace.barrelracegame.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

public class Player implements Parcelable, Comparable<Player> {
    private String name;
    private String time;

    public Player(String name, String time) {
        this.name = name;
        this.time = time;
    }

    public Player(Parcel parcel) {
        name = parcel.readString();
        time = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(time);
    }

    public static final Parcelable.Creator<Player> CREATOR = new Parcelable.Creator<Player>() {
        public Player createFromParcel(Parcel parcel) {
            return new Player(parcel);
        }

        public Player[] newArray(int size) {
            return new Player[size];
        }
    };

    @Override
    public int compareTo(@NonNull Player player) {
        return this.time.compareTo(player.time);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
