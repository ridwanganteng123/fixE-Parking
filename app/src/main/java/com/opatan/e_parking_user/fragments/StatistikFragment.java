package com.opatan.e_parking_user.fragments;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.TextView;
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
    public TextView hadir_status, terlambat_status;
    private DatabaseReference databaseReference1, databaseReference2;
    private String[] xData = {"Tepat Waktu","Terlambat","Tidak Masuk"};
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
        hadir_status = view.findViewById(R.id.hadir_txt);
        terlambat_status = view.findViewById(R.id.terlambat_txt);

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

        databaseReference1 = FirebaseDatabase.getInstance().getReference().child("ScanHarian").getRef();
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(final DataSnapshot snapshot : dataSnapshot.getChildren()){
                    final String tanggal = snapshot.getRef().getKey();
                    siswaId = snapshot.child(uid).child("siswaId").getValue(String.class);
                    System.out.println("SISWA ID : " + siswaId);
                    System.out.println("TANGGAL : " + tanggal);

                    databaseReference2 = FirebaseDatabase.getInstance().getReference().child("ScanHarian").child(tanggal);
                    databaseReference2.orderByChild("status").equalTo("hadir").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            final String hadirKey = dataSnapshot.child(siswaId).getRef().getKey();
                            ArrayList<String> hadir_list = new ArrayList<>();
                            hadir_list.add(hadirKey);
                            final int jumlahHadir = hadir_list.size();

                            databaseReference2 = FirebaseDatabase.getInstance().getReference().child("ScanHarian").child(tanggal);
                            databaseReference2.orderByChild("status").equalTo("terlambat").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    final String terlambatKey = dataSnapshot.child(siswaId).getRef().getKey();
                                    ArrayList<String> terlambat_list = new ArrayList<>();
                                    terlambat_list.add(terlambatKey);
                                    int jumlahTerlambat = terlambat_list.size();
                                    terlambat_status.setText(String.valueOf(jumlahTerlambat));
                                    hadir_status.setText(String.valueOf(jumlahHadir));
                                    yData = new int[]{jumlahHadir, jumlahTerlambat, 0};
                                    addDataSet();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
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