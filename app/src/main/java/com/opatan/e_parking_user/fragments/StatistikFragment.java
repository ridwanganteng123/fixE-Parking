package com.opatan.e_parking_user.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.opatan.e_parking_user.R;
import com.opatan.e_parking_user.activities.MainActivity;
import com.opatan.e_parking_user.datas.model.DataHistoryParkir;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class StatistikFragment extends Fragment {

    private int[] yData;
    private String uid, siswaId;
    private FirebaseUser currentUser;
    private TextView hadir_status, terlambat_status, tidak_masuk_status;
    private DatabaseReference databaseReference1, databaseReference2;
    private String[] xData = {"Tepat Waktu","Terlambat","Tidak Masuk"};
    private PieChart pieChart;
    private int jumlahHadir;
    private int jumlahTerlambat;
    private int jumlahTidakHadir;
    private LinearLayout terlambat_section, hadir_section;
    private LineChart lineChart;

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
        hadir_status = view.findViewById(R.id.hadir_txt);
        terlambat_status = view.findViewById(R.id.terlambat_txt);
        hadir_section = view.findViewById(R.id.hadir_section);
        terlambat_section = view.findViewById(R.id.terlambat_section);
        tidak_masuk_status = view.findViewById(R.id.tidak_masuk_txt);


        lineChart = view.findViewById(R.id.lineChart);
        LineDataSet lineDataSet = new LineDataSet(getData(), "Inducesmile");
        lineDataSet.setColor(ContextCompat.getColor(getContext().getApplicationContext(), R.color.colorPrimary));
        lineDataSet.setValueTextColor(ContextCompat.getColor(getContext().getApplicationContext(), R.color.colorPrimaryDark));
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        final String[] months = new String[]{"Senin", "Selasa", "Rabu", "Kamis", "Jumat"};
        ValueFormatter formatter = new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                return months[(int) value];
            }
        };
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(formatter);

        YAxis yAxisRight = lineChart.getAxisRight();
        yAxisRight.setEnabled(false);

        YAxis yAxisLeft = lineChart.getAxisLeft();
        yAxisLeft.setGranularity(1f);

        LineData data = new LineData(lineDataSet);
        lineChart.setData(data);
        lineChart.animateX(2500);
        lineChart.invalidate();


        pieChart = view.findViewById(R.id.pieChart);
        pieChart.setRotationEnabled(true);
        pieChart.setHoleRadius(50f);
        pieChart.setCenterTextSize(16);
        pieChart.setUsePercentValues(true);
        pieChart.setCenterText("Statistik");
        pieChart.setDescription(null);
        pieChart.setDrawEntryLabels(true);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        uid = currentUser.getUid();

        hadir_section.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext().getApplicationContext(), "DiPencet", Toast.LENGTH_SHORT).show();
            }
        });

        terlambat_section.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext().getApplicationContext(), "DiPencet", Toast.LENGTH_SHORT).show();
            }
        });

        databaseReference1 = FirebaseDatabase.getInstance().getReference().child("ScanHarian").getRef();
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                jumlahHadir = 0;
                jumlahTerlambat = 0;
                jumlahTidakHadir = 0;
                for (DataSnapshot dataHadir : dataSnapshot.getChildren())
                {
                    System.out.println("DATA HADIR : " + dataHadir);
                    String status = dataHadir.child(uid).child("status").getValue(String.class);
                    System.out.println("STATUS : " + status);
                    if (status == null){
                        jumlahTidakHadir++;
                    }else if(status.equals("hadir")){
                        jumlahHadir++;
                    }else if (status.equals("terlambat")){
                        jumlahTerlambat++;
                    }
                }
                terlambat_status.setText(String.valueOf(jumlahHadir));
                hadir_status.setText(String.valueOf(jumlahTerlambat));
                tidak_masuk_status.setText(String.valueOf(jumlahTidakHadir));
                yData = new int[]{jumlahTerlambat, jumlahHadir, jumlahTidakHadir};
                addDataSet();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

            }

            @Override
            public void onNothingSelected() {

            }
        });
    }

    private ArrayList getData(){
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(0f, 4f));
        entries.add(new Entry(1f, 1f));
        entries.add(new Entry(2f, 2f));
        entries.add(new Entry(3f, 4f));
        entries.add(new Entry(4f, 5f));
        return entries;
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}