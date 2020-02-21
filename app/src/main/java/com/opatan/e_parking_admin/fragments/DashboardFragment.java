package com.opatan.e_parking_admin.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.opatan.e_parking_admin.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class DashboardFragment extends Fragment {

    private int[] yData;
    private String formattedDate, siswaId, status;
    private ImageButton prev_, next_;
    private FirebaseUser currentUser;
    public TextView hadir_status, terlambat_status, tgl_txt, tidak_hadir_status;
    private DatabaseReference databaseReference1, databaseReference2;
    private String[] xData = {"Tepat Waktu","Terlambat","Tidak Masuk"};
    PieChart pieChart;
    int jumlahTidakHadir;
    int jumlahHadir;
    int jumlahTerlambat;

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
        hadir_status = view.findViewById(R.id.hadir_txt);
        terlambat_status = view.findViewById(R.id.terlambat_txt);
        tidak_hadir_status = view.findViewById(R.id.tidak_hadir_txt);
        prev_ = view.findViewById(R.id.prev_date);
        next_ = view.findViewById(R.id.next_date);
        tgl_txt = view.findViewById(R.id.tgl_txt);
        pieChart = view.findViewById(R.id.pieChart);
        pieChart.setRotationEnabled(true);
        pieChart.setHoleRadius(50f);
        pieChart.setCenterTextSize(16);
        pieChart.setUsePercentValues(true);
        pieChart.setCenterText("Statistik");
        pieChart.setDescription(null);
        pieChart.setDrawEntryLabels(true);

        final Calendar c = Calendar.getInstance();

        System.out.println("Current time => " + c.getTime());

        final SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        formattedDate = df.format(c.getTime());

        tgl_txt.setText(formattedDate);
        getData(formattedDate);

        prev_.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                c.add(Calendar.DATE, -1);
                formattedDate = df.format(c.getTime());
                Log.v("PREVIOUS DATE : ", formattedDate);
                tgl_txt.setText(formattedDate);
                getData(formattedDate);
            }
        });

        next_.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                c.add(Calendar.DATE, 1);
                formattedDate = df.format(c.getTime());
                Log.v("NEXT DATE : ", formattedDate);
                tgl_txt.setText(formattedDate);
                getData(formattedDate);
            }
        });
    }

    private void getData(final String tanggal)
    {
        databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Siswa");
        databaseReference1.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dSiswa) {
                jumlahTidakHadir=0;
                jumlahHadir=0;
                jumlahTerlambat=0;
                for (final DataSnapshot siswaSnapshot : dSiswa.getChildren())
                {
                   final String siswaId = siswaSnapshot.child("siswaId").getValue().toString();
                   final String nama = siswaSnapshot.child("nama").getValue().toString();
                   DatabaseReference dbRef2 = FirebaseDatabase.getInstance().getReference("ScanHarian").child(tanggal);
                   dbRef2.addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(DataSnapshot dPresensi) {
                           System.out.println("DATA PRESENSI : " + dPresensi);
                           String status = dPresensi.child(siswaId).child("status").getValue(String.class);
                           System.out.println("ID : " + siswaId);
                           System.out.println("NAMA : " + nama);
                           System.out.println("WAKTU MASUKNYA : " + status);
                           if(status == null)
                           {
                              jumlahTidakHadir++;
                           } else if (status.equals("hadir")){
                              jumlahHadir++;
                           } else if (status.equals("terlambat")){
                              jumlahTerlambat++;
                           }
                           terlambat_status.setText(String.valueOf(jumlahHadir));
                           hadir_status.setText(String.valueOf(jumlahTerlambat));
                           tidak_hadir_status.setText(String.valueOf(jumlahTidakHadir));
                           yData = new int[]{jumlahTerlambat, jumlahHadir, jumlahTidakHadir};
                           addDataSet();
                       }
                       @Override
                       public void onCancelled(DatabaseError de) { System.out.println("The read failed: " + de.getCode());
                       }
                   });

                }
            }
            @Override
            public void onCancelled(DatabaseError de) {
                System.out.println("The read failed: " + de.getCode());
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
        PieDataSet pieDataSet = new PieDataSet(yEntrys, "");
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
}
