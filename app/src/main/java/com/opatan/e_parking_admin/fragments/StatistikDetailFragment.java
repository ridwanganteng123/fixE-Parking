package com.opatan.e_parking_admin.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import com.opatan.e_parking_admin.datas.model.DataHistoryParkir;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class StatistikDetailFragment extends Fragment {

    private int[] yData;
    private String siswaId, role, formattedDate;
    private TextView hadir_status, terlambat_status, tidak_masuk_status, tgl_txt, tv, jumlah_siswa, nyeken;
    private DatabaseReference databaseReference1, databaseReference2;
    private Handler handler = new Handler();
    private String[] xData = {"Tepat Waktu","Terlambat","Tidak Masuk"};
    private ImageButton prev_, next_;
    private Calendar calendar;
    private PieChart pieChart;
    private int jumlahTerlambat, jumlahTidakHadir, jumlahHadir, jumlahnyeken, pStatus, persenan, jumlahSiswa;
    private ProgressBar mProgress;
    private LinearLayout contentSiswa, contentPetugas;

    public StatistikDetailFragment(String siswaId, String role) {
        this.siswaId = siswaId;
        this.role = role;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_statistik_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        hadir_status = view.findViewById(R.id.hadir_txt);
        tv = view.findViewById(R.id.tv);
        terlambat_status = view.findViewById(R.id.terlambat_txt);
        tidak_masuk_status = view.findViewById(R.id.tidak_masuk_txt);
        contentPetugas = view.findViewById(R.id.content_petugas);
        contentSiswa = view.findViewById(R.id.content_siswa);
        prev_ = view.findViewById(R.id.prev_date);
        tgl_txt = view.findViewById(R.id.tgl_txt);
        next_ = view.findViewById(R.id.next_date);
        mProgress = view.findViewById(R.id.circularProgressbar);
        jumlah_siswa = view.findViewById(R.id.jumlah_siswa_txt);
        nyeken = view.findViewById(R.id.nyeken_txt);

        final Calendar c = Calendar.getInstance();
        final SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        formattedDate = df.format(c.getTime());
        tgl_txt.setText(formattedDate);
        getPetugasData(formattedDate);

        pieChart = view.findViewById(R.id.pieChart);
        pieChart.setRotationEnabled(true);
        pieChart.setHoleRadius(50f);
        pieChart.setCenterTextSize(16);
        pieChart.setUsePercentValues(true);
        pieChart.setCenterText("Statistik");
        pieChart.setDescription(null);
        pieChart.setDrawEntryLabels(true);

        if (role.equals("Siswa"))
        {
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
                        String status = dataHadir.child(siswaId).child("status").getValue(String.class);
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
        } else if (role.equals("Petugas")){
            contentSiswa.setVisibility(View.GONE);
            contentPetugas.setVisibility(View.VISIBLE);

            tgl_txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (view == tgl_txt) {
                        calendar = Calendar.getInstance();
                        int mYear = calendar.get(Calendar.YEAR);
                        int mMonth = calendar.get(Calendar.MONTH);
                        int mDay = calendar.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year,
                                                          int monthOfYear, int dayOfMonth) {
                                        String tanggal = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                        tgl_txt.setText(tanggal);
                                        getPetugasData(tanggal);
                                    }
                                }, mYear, mMonth, mDay);
                        datePickerDialog.show();
                    }
                }
            });

            prev_.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    c.add(Calendar.DATE, -1);
                    formattedDate = df.format(c.getTime());
                    Log.v("PREVIOUS DATE : ", formattedDate);
                    tgl_txt.setText(formattedDate);
                    getPetugasData(formattedDate);
                }
            });

            next_.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    c.add(Calendar.DATE, 1);
                    formattedDate = df.format(c.getTime());
                    Log.v("NEXT DATE : ", formattedDate);
                    tgl_txt.setText(formattedDate);
                    getPetugasData(formattedDate);
                }
            });
        }

        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

            }

            @Override
            public void onNothingSelected() {

            }
        });
    }

    private void getPetugasData(final String tanggal)
    {
        databaseReference1 = FirebaseDatabase.getInstance().getReference("ScanHarian").child(tanggal);
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dPresensi) {
                jumlahnyeken = 0;
                pStatus = 0;
                jumlahSiswa = 0;
                persenan = 0;
                for (final DataSnapshot dataPresensi : dPresensi.getChildren()) {
                    databaseReference1 = FirebaseDatabase.getInstance().getReference("Siswa").getRef();
                    databaseReference1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dSiswa) {
                            jumlahSiswa = (int) dSiswa.getChildrenCount();
                            String pemeriksa = dataPresensi.child("pemeriksa").getValue().toString();

                            System.out.println("DATA PRESENSI : " + dataPresensi);
                            System.out.println("PEMERIKSA : " + pemeriksa);
                            System.out.println("UID : " + siswaId);
                            System.out.println("JUMLAH SISWA : " + jumlahSiswa);

                            if (pemeriksa.equals(siswaId)) {
                                jumlahnyeken++;
                            }

                            persenan = hitungPersen(jumlahSiswa, jumlahnyeken);

                            System.out.println("JUMLAH NYEKEN : " + jumlahnyeken);
                            nyeken.setText(String.valueOf(jumlahnyeken));
                            jumlah_siswa.setText(String.valueOf(jumlahSiswa));

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    while (pStatus < persenan) {
                                        pStatus += 1;

                                        handler.post(new Runnable() {

                                            @Override
                                            public void run() {
                                                mProgress.setProgress(pStatus);
                                                tv.setText(pStatus + "%");

                                            }
                                        });
                                        try {
                                            Thread.sleep(16);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }).start();
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
    }

    private int hitungPersen(int jumlahSiswa, int nilaiTertentu)
    {
        double hasil = (double) nilaiTertentu / (double) jumlahSiswa * 100;
        int hasil_main = (int) hasil;
        return hasil_main;
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
