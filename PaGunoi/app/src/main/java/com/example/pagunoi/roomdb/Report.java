package com.example.pagunoi.roomdb;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Report {
    @Ignore
    public Report(String reportFilePath, String ownersEmail) {
        this.reportFilePath = reportFilePath;
        this.ownersEmail = ownersEmail;
    }

    public Report() {
    }

    @PrimaryKey(autoGenerate = true)
    int uid;
    @ColumnInfo(name = "file_path")
    String reportFilePath;
    @ColumnInfo(name = "email")
    String ownersEmail;

    @Ignore
    public int getUid() {
        return uid;
    }
    @Ignore
    public String getReportFilePath() {
        return reportFilePath;
    }
    @Ignore
    public String getOwnersEmail() {
        return ownersEmail;
    }

}
