package com.example.pagunoi.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.pagunoi.R;
import com.example.pagunoi.activities.ReportsHistoryActivity;
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
    private SearchView searchView;

    public ReportsHistory() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.history_search_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = new SearchView(((ReportsHistoryActivity) this.getContext()).getSupportActionBar().getThemedContext());
        // MenuItemCompat.setShowAsAction(item, //MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | //MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        //  MenuItemCompat.setActionView(item, searchView);
        // These lines are deprecated in API 26 use instead
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItem.SHOW_AS_ACTION_IF_ROOM);
        item.setActionView(searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                reportAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                reportAdapter.getFilter().filter(query);
                return false;
            }
        });
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
        setHasOptionsMenu(true);

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
