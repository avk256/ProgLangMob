package com.example.lab6;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {
    @PrimaryKey
    public int uid;

    @ColumnInfo(name = "email")
    public String email;

    @ColumnInfo(name = "result")
    public int result;

    @ColumnInfo(name = "difficulty")
    public String difficulty;

    @ColumnInfo(name = "time")
    public String time;

}
