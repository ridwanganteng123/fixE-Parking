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

public class DashboardFragment extends Fragment {

    private int[] yData;
    HashMap<String, String> hadir_list, terlambat_list;
    private String uid, siswaId, formattedDate, terlambatKey, hadirKey;
    private ImageButton prev_, next_;
    private FirebaseUser currentUser;
    public TextView hadir_status, terlambat_status, tgl_txt;
    private int jumlahHadir = 0, jumlahTerlambat = 0;
    private DatabaseReference databaseReference1, databaseReference2;
    private String[] xData = {"Tepat Waktu","Terlambat","Tidak Masuk"};
    PieChart pieChart;

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
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        uid = currentUser.getUid();

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
        databaseReference2 = FirebaseDatabase.getInstance().getReference().child("ScanHarian").child(tanggal);
        databaseReference2.orderByChild("status").equalTo("hadir").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (final DataSnapshot dataHadir : dataSnapshot.getChildren())
                {
                    hadir_list = new HashMap<String, String>();
                    hadirKey = dataHadir.child("siswaId").getValue().toString();
                    System.out.println("DATA HADIR : " + hadirKey);
                    hadir_list.put(hadirKey, "hadir");
                    System.out.println("LIST HADIR : " + hadir_list);
                    System.out.println("COUINT HADIR : " + hadir_list.size());

                    databaseReference2 = FirebaseDatabase.getInstance().getReference().child("ScanHarian").child(tanggal);
                    databaseReference2.orderByChild("status").equalTo("terlambat").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot dataTelat : dataSnapshot.getChildren())
                            {
                                terlambat_list = new HashMap<String, String>();
                                terlambatKey = dataTelat.child("siswaId").getValue().toString();
                                System.out.println("NOMOR : " + dataTelat.getChildrenCount());
                                System.out.println("DATA TERLAMBAT : " + terlambatKey);
                                terlambat_list.put(terlambatKey, "telat");
                                System.out.println("LIST TERLMABAT : " + terlambat_list);
                                jumlahHadir = hadir_list.size();
                                jumlahTerlambat = terlambat_list.size();
                                System.out.println("COUNT TERLAMBAT : " + terlambat_list.size());

                                terlambat_status.setText(String.valueOf(jumlahHadir));
                                hadir_status.setText(String.valueOf(jumlahTerlambat));

                                yData = new int[]{jumlahTerlambat, jumlahHadir, 0};
                                addDataSet();
                            }
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
