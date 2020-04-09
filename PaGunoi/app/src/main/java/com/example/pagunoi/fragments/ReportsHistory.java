package com.example.pagunoi.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pagunoi.R;
import com.example.pagunoi.adapters.ReportAdapter;
import com.example.pagunoi.roomdb.OnReportRepositoryActionListener;
import com.example.pagunoi.roomdb.Report;
import com.example.pagunoi.roomdb.ReportRepository;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;


public class ReportsHistory extends Fragment implements OnReportRepositoryActionListener {

    private RecyclerView recyclerView;
    private ReportAdapter reportAdapter;

    public ReportsHistory() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reports_history, container, false);
        recyclerView = view.findViewById(R.id.report_history_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        reportAdapter = new ReportAdapter(new ArrayList<Report>());
        ReportRepository reportRepository = new ReportRepository(getContext());
        reportRepository.getAllForEmailTask(this, FirebaseAuth.getInstance().getCurrentUser().getEmail());
        recyclerView.setAdapter(reportAdapter);

        return view;
    }

    @Override
    public void actionSuccess() {
        Log.d("DB","Getting report history was successful");
    }

    @Override
    public void actionFailed() {
        Log.e("DB-ERROR","Getting report history was unsuccessful");
    }

    @Override
    public void updateRecycler(List<Report> reports) {
        Log.d("DB", "Updating the dataset, size of the list :" + reports.size() + " ....");
        List<Report> dataSet = reportAdapter.getmDataSet();
        dataSet.clear();
        dataSet.addAll(reports);
        reportAdapter.notifyDataSetChanged();
    }
}
