package com.example.pagunoi.roomdb;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ReportDao {
    @Query("SELECT * FROM report WHERE uid LIKE :uid LIMIT 1")
    Report findById(Integer uid);

    @Query("SELECT * FROM report WHERE email LIKE :email")
    List<Report> findByEmail(String email);

    @Insert
    void insertAll(Report... reports);

    @Delete
    void delete(Report report);
}