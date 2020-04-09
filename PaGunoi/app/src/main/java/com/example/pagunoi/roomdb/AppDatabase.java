package com.example.pagunoi.roomdb;
import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Report.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ReportDao reportDao();
}
