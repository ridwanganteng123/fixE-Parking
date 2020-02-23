package com.opatan.e_parking_admin.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.opatan.e_parking_admin.R;
import com.opatan.e_parking_admin.adapters.AdapterSiswaStatistik;
import com.opatan.e_parking_admin.datas.model.DataDetailStatistik;
import com.opatan.e_parking_admin.datas.model.DataHistoryParkir;

import java.util.ArrayList;

public class DetailStatistikActivity extends AppCompatActivity {

    private int pStatus = 0, persenan = 0;
    private Handler handler = new Handler();
    private String judul, tanggal, code, nama = "", siswaId = "", pemeriksaId="", waktu_masuk="", namaPetugas="", imageUrl="";
    private TextView tv;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private RelativeLayout relativeLayout;
    private ProgressBar mProgress;
    private ArrayList<DataDetailStatistik> dataDetailStatistikArrayList;
    private DatabaseReference databaseReference1;
    private ShimmerFrameLayout shimmerFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_statistik);

        Resources res = getResources();
        Drawable drawable = res.getDrawable(R.drawable.circular);
        recyclerView = findViewById(R.id.recycler_statistik);
        mProgress = findViewById(R.id.circularProgressbar);
        shimmerFrameLayout = findViewById(R.id.container_shimmer);
        relativeLayout = findViewById(R.id.content_no_data);

        mProgress.setProgress(0);
        mProgress.setSecondaryProgress(100);
        mProgress.setMax(100);
        mProgress.setProgressDrawable(drawable);
        persenan = getIntent().getExtras().getInt("persentase");
        judul = getIntent().getStringExtra("judul");
        tanggal = getIntent().getStringExtra("tanggal");
        code = getIntent().getStringExtra("code");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(judul);

        recyclerView.setHasFixedSize(true);

        dataDetailStatistikArrayList = new ArrayList<>();

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        getData(tanggal, code);

        adapter = new AdapterSiswaStatistik(this, dataDetailStatistikArrayList);
        recyclerView.setAdapter(adapter);

        tv = findViewById(R.id.tv);
    }

    private void getData(String tanggal, String code) {
        DatabaseReference dbRef2 = FirebaseDatabase.getInstance().getReference("ScanHarian").child(tanggal).getRef();
        dbRef2.orderByChild("status").equalTo(code).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dPresensi) {
                for (final DataSnapshot presensiSnapshot : dPresensi.getChildren())
                {
                    pemeriksaId = presensiSnapshot.child("pemeriksa").getValue().toString();
                    siswaId = presensiSnapshot.child("siswaId").getValue().toString();
                    waktu_masuk = presensiSnapshot.child("waktu_masuk").getValue(String.class);
                    databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Siswa").child(siswaId);
                    databaseReference1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(final DataSnapshot dSiswa) {
                            databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Petugas").child(pemeriksaId);
                            databaseReference1.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dPetugas) {
                                    nama = dSiswa.child("nama").getValue().toString();
                                    imageUrl = dSiswa.child("imageURL").getValue().toString();
                                    namaPetugas = dPetugas.child("nama").getValue().toString();
                                    if (presensiSnapshot.getValue() != null)
                                    {
                                        dataDetailStatistikArrayList.add(new DataDetailStatistik(waktu_masuk, nama, namaPetugas, imageUrl));
                                        shimmerFrameLayout.hideShimmer();
                                        shimmerFrameLayout.setVisibility(View.GONE);
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
                                    } else {
                                        shimmerFrameLayout.hideShimmer();
                                        shimmerFrameLayout.setVisibility(View.GONE);
                                        relativeLayout.setVisibility(View.VISIBLE);
                                        recyclerView.setVisibility(View.GONE);
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
    }
}
