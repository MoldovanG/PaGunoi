package com.example.pagunoi.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.pagunoi.R;
import com.example.pagunoi.roomdb.OnReportRepositoryActionListener;
import com.example.pagunoi.roomdb.Report;
import com.example.pagunoi.roomdb.ReportRepository;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class ReportsHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports_history);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                        .replace(R.id.history_fragment_holder,new com.example.pagunoi.fragments.ReportsHistory())
                        .commit();

    }
}
