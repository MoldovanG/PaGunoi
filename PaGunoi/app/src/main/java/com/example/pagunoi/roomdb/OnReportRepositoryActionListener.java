package com.example.pagunoi.roomdb;

import java.util.List;

public interface OnReportRepositoryActionListener {
    void actionSuccess();
    void actionFailed();
    void updateRecycler(List<Report> users);
}
