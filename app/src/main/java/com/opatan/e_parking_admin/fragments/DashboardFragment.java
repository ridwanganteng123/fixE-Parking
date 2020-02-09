package com.opatan.e_parking_admin.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.opatan.e_parking_admin.R;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {

    private BarChart chart;
    float barWidth;
    float barSpace;
    int groupCount = 6;
    float groupSpace;

    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        barWidth = 0.3f;
        barSpace = 0f;
        groupSpace = 0.4f;

        chart = view.findViewById(R.id.barChart);
        chart.setDescription(null);
        chart.setPinchZoom(true);
        chart.setScaleEnabled(true);
        chart.setDrawBarShadow(true);
        chart.setDrawGridBackground(false);

        ArrayList xVals = new ArrayList();

        xVals.add("Senin");
        xVals.add("Selasa");
        xVals.add("Rabu");
        xVals.add("Kamis");
        xVals.add("Jumat");

        ArrayList hadir = new ArrayList();
        ArrayList tidak_hadir = new ArrayList();
        ArrayList terlambat = new ArrayList();

        hadir.add(new BarEntry(1, (float) 1));
        terlambat.add(new BarEntry(1, (float) 2));
        tidak_hadir.add(new BarEntry(1, (float) 3));
        hadir.add(new BarEntry(2, (float) 3));
        terlambat.add(new BarEntry(2, (float) 4));
        tidak_hadir.add(new BarEntry(2, (float) 4));
        hadir.add(new BarEntry(3, (float) 5));
        terlambat.add(new BarEntry(3, (float) 6));
        tidak_hadir.add(new BarEntry(4, (float) 6));
        hadir.add(new BarEntry(4, (float) 7));
        terlambat.add(new BarEntry(4, (float) 8));
        tidak_hadir.add(new BarEntry(1, (float) 3));
        hadir.add(new BarEntry(5, (float) 9));
        terlambat.add(new BarEntry(5, (float) 10));
        tidak_hadir.add(new BarEntry(1, (float) 3));

        BarDataSet set1, set2, set3;
        set1 = new BarDataSet(hadir, "Tepat Waktu");
        set1.setColor(Color.RED);
        set2 = new BarDataSet(terlambat, "Terlambat");
        set2.setColor(Color.BLUE);
        set3 = new BarDataSet(tidak_hadir, "Tidak Masuk");
        set3.setColor(Color.YELLOW);
        BarData data = new BarData(set1, set2, set3);
        data.setValueFormatter(new LargeValueFormatter());
        chart.setData(data);
        chart.getBarData().setBarWidth(barWidth);
        chart.getXAxis().setAxisMinimum(0);
        chart.getXAxis().setAxisMaximum(0 + chart.getBarData().getGroupWidth(groupSpace, barSpace) * groupCount);
        chart.groupBars(0, groupSpace, barSpace);
        chart.getData().setHighlightEnabled(false);
        chart.invalidate();

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setYOffset(20f);
        l.setXOffset(0f);
        l.setYEntrySpace(0f);
        l.setTextSize(8f);

        //X-axis
        XAxis xAxis = chart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setCenterAxisLabels(true);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisMaximum(6);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xVals));

        //Y-axis
        chart.getAxisRight().setEnabled(false);
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setDrawGridLines(false);
        leftAxis.setSpaceTop(35f);
        leftAxis.setAxisMinimum(0f);
    }
}
