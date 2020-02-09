package com.opatan.e_parking_user.fragments;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.opatan.e_parking_user.R;
import com.opatan.e_parking_user.activities.MainActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class StatistikFragment extends Fragment {

    private NotificationManager mNotificationManager;
    private int[] yData = {100,100,100};
    private String[] xData = {"Tepat Waktu","Terlambat","Tidak Masuk"};
    private long[] pattern = {500,500,500,500,500,500,500,500,500};
    PieChart pieChart;

    public StatistikFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_statistik, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Button btn = view.findViewById(R.id.btn);

        pieChart = view.findViewById(R.id.pieChart);
        pieChart.setRotationEnabled(true);
        pieChart.setHoleRadius(50f);
        pieChart.setCenterTextSize(16);
        pieChart.setUsePercentValues(true);
        pieChart.setCenterText("Statistik");
        pieChart.setDescription(null);
        pieChart.setDrawEntryLabels(true);
        addDataSet();
        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Toast.makeText(getContext().getApplicationContext(), xData[2], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNotification("10 Menit Lagi Menuju Masuk Sekolah", "Segera Masuk Sekolah");
            }
        });
    }

    private void addDataSet() {
        ArrayList<PieEntry> yEntrys = new ArrayList<>();
        ArrayList<String> xEntrys = new ArrayList<>();

        for (int y = 0; y < yData.length; y++)
        {
            yEntrys.add(new PieEntry(yData[y], y));
        }

        for (int x =0; x < xData.length; x++)
        {
            xEntrys.add(xData[x]);
        }
        PieDataSet pieDataSet = new PieDataSet(yEntrys, "asdasd");
        pieDataSet.setSliceSpace(3);
        pieDataSet.setSelectionShift(3);
        pieDataSet.setValueTextSize(15);

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.rgb(197, 255, 140));
        colors.add(Color.rgb(255,215,132));
        colors.add(Color.rgb(255,145,148));
        pieDataSet.setColors(colors);

        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setXEntrySpace(7);
        legend.setYEntrySpace(5);

        PieData pieData = new PieData(pieDataSet);
        pieData.setValueFormatter(new PercentFormatter(pieChart));
        pieDataSet.setValueFormatter(new PercentFormatter(pieChart));
        pieChart.setData(pieData);
        pieChart.invalidate();

    }

    @SuppressLint("ResourceAsColor")
    public void addNotification(String title, String content)
    {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getContext().getApplicationContext(), "notify_001");
        Intent ii = new Intent(getContext().getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, ii, 0);

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.setBigContentTitle(title);
        bigText.bigText(content);
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.drawable.message_bg);
        mBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        mBuilder.setCategory(NotificationCompat.CATEGORY_MESSAGE);
        mBuilder.setOnlyAlertOnce(true);
        mBuilder.setStyle(bigText);
        mBuilder.setAutoCancel(true);

        mNotificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            String channelId = "Your_channel_id";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);
        }

        mNotificationManager.notify(0, mBuilder.build());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}