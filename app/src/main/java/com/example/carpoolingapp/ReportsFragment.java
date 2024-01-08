package com.example.carpoolingapp;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.karumi.dexter.BuildConfig;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ReportsFragment extends Fragment {

    TableLayout tableLayout;
    Button downloadReports;
    DatabaseHelper dbHelper;
    List<Paymeant> payingList;

    public ReportsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reports, container, false);

        tableLayout = view.findViewById(R.id.tables);
        downloadReports = view.findViewById(R.id.downloadBtn);
        dbHelper = new DatabaseHelper(getActivity());

        payingList = dbHelper.getPayments();

        for(int i = 0; i < payingList.size(); i++){
            TableRow row = new TableRow(getActivity());

            Paymeant paymeant = payingList.get(i);

            TextView drivers = new TextView(getActivity());
            drivers.setText(paymeant.getDrive());
            row.addView(drivers);

            TextView passing = new TextView(getActivity());
            passing.setText(paymeant.getPassenger());
            row.addView(passing);

            TextView prices = new TextView(getActivity());
            prices.setText(String.valueOf(paymeant.getPricings()));
            row.addView(prices);

            tableLayout.addView(row);
        }

        downloadReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exportToCsv();
            }
        });


        return view;
    }

    private void exportToCsv() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tableLayout.getChildCount(); i++) {
            TableRow row = (TableRow) tableLayout.getChildAt(i);
            for (int j = 0; j < row.getChildCount(); j++) {
                TextView cell = (TextView) row.getChildAt(j);
                sb.append(cell.getText().toString());
                if (j < row.getChildCount() - 1) {
                    sb.append(",");
                }
            }
            sb.append("\n");
        }
        String csvData = sb.toString();

        File file = new File(Environment.getExternalStorageDirectory(), "table_data.csv");
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(csvData);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Uri contentUri = FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID + ".provider", file);

        DownloadManager downloadManager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(contentUri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "my_downloads/table_data.csv");
        request.allowScanningByMediaScanner();
        downloadManager.enqueue(request);

    }
}