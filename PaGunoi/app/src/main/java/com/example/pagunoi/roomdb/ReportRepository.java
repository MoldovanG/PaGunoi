package com.example.pagunoi.roomdb;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import com.example.pagunoi.ApplicationController;

import java.util.List;

public class ReportRepository {
    private AppDatabase appDatabase;

    public ReportRepository(Context context) {
        appDatabase = ApplicationController.getAppDatabase();
    }

    public void insertTask(final Report report,
                           final OnReportRepositoryActionListener listener) {
        new InsertTask(listener).execute(report);
    }

    public void getAllForEmailTask(final OnReportRepositoryActionListener listener,
                                   String email){
        new GetAllForEmailTask(listener).execute(email);
    }

    public void deleteTask(int uid,
                           final OnReportRepositoryActionListener listener){
        new DeleteTask(listener).execute(uid);
    }

    private class InsertTask extends AsyncTask<Report, Void, List<Report>> {
        OnReportRepositoryActionListener listener;
        InsertTask(OnReportRepositoryActionListener listener) {
            this.listener = listener;
        }
        @Override
        protected List<Report> doInBackground(Report... reports) {

            appDatabase.reportDao().insertAll(reports[0]);
            Log.d("DB","Inserted the following report :" + reports[0].ownersEmail);
            return appDatabase.reportDao().findByEmail(reports[0].ownersEmail);
        }
        @Override
        protected void onPostExecute(List<Report> reports) {
            super.onPostExecute(reports);
            listener.actionSuccess();
            listener.updateRecycler(reports);
        }
    }

    private class DeleteTask extends AsyncTask<Integer, Void, List<Report>> {
        OnReportRepositoryActionListener listener;
        DeleteTask(OnReportRepositoryActionListener listener) {
            this.listener = listener;
        }
        @Override
        protected List<Report> doInBackground(Integer... ids) {

            Report report = appDatabase.reportDao().findById(ids[0]);
            appDatabase.reportDao().delete(report);
            return appDatabase.reportDao().findByEmail(report.ownersEmail);
        }
        @Override
        protected void onPostExecute(List<Report> reports) {
            super.onPostExecute(reports);
            listener.actionSuccess();
            listener.updateRecycler(reports);
        }
    }

    private class GetAllForEmailTask extends AsyncTask<String, Void, List<Report>> {
        OnReportRepositoryActionListener listener;
        GetAllForEmailTask(OnReportRepositoryActionListener listener) {
            this.listener = listener;
        }

        @Override
        protected List<Report> doInBackground(String... emails) {
            return appDatabase.reportDao().findByEmail(emails[0]);
        }

        @Override
        protected void onPostExecute(List<Report> reports) {
            super.onPostExecute(reports);
            listener.actionSuccess();
            listener.updateRecycler(reports);
        }
    }
}
