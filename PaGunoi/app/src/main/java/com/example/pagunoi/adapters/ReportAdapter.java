package com.example.pagunoi.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pagunoi.R;
import com.example.pagunoi.roomdb.Report;

import org.w3c.dom.Text;

import java.io.File;
import java.lang.reflect.Method;
import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ViewHolder>  {


    private List<Report> mDataSet;

    public ReportAdapter(List<Report> mDataSet) {
        this.mDataSet = mDataSet;
    }

    public List<Report> getmDataSet() {
        return mDataSet;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.report_view_holder, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Report report = mDataSet.get(position);
        TextView pathTextView = holder.view.findViewById(R.id.view_holder_path_textView);
        TextView uidTextView = holder.view.findViewById(R.id.view_holder_uid_textView);
        pathTextView.setText(report.getReportFilePath());
        uidTextView.setText("Id: " + String.valueOf(report.getUid()));
        holder.view.findViewById(R.id.open_pdf_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayPdf(report.getReportFilePath(), holder.view.getContext());
            }
        });

    }
    private void displayPdf(String pdfPath, Context context) {
        File file = new File(pdfPath);
        Intent target = new Intent(Intent.ACTION_VIEW);
        if(Build.VERSION.SDK_INT>=24){
            try{
                //For API's > 24, runtime exception occurs when a URI is exposed BEYOND this particular app that you are writing (AKA when user attempts to open in device/emulator
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        target.setDataAndType(Uri.fromFile(file),"application/pdf");
        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        Intent intent = Intent.createChooser(target, "Open File");

         context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public View view;
        public ViewHolder(View v) {
            super(v);
            view = v;
        }
    }
}
