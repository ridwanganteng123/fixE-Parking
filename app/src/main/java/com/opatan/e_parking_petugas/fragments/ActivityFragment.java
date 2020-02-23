package com.opatan.e_parking_petugas.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.opatan.e_parking_petugas.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ActivityFragment extends Fragment {

    private String formattedDate, uid;
    private ImageButton prev_, next_;
    private Handler handler = new Handler();
    private TextView hadir_status, tgl_txt, jumlah_siswa_txt;
    private DatabaseReference databaseReference1;
    private Calendar calendar;
    private TextView tv;
    private ProgressBar mProgress;
    private int jumlahnyeken, pStatus, persenan, jumlahSiswa;

    public ActivityFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_activity_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tv = view.findViewById(R.id.tv);
        hadir_status = view.findViewById(R.id.hadir_txt);
        prev_ = view.findViewById(R.id.prev_date);
        next_ = view.findViewById(R.id.next_date);
        tgl_txt = view.findViewById(R.id.tgl_txt);
        jumlah_siswa_txt = view.findViewById(R.id.jumlah_siswa_txt);
        Resources res = getResources();
        Drawable drawable = res.getDrawable(R.drawable.circular);
        mProgress = view.findViewById(R.id.circularProgressbar);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        uid = firebaseUser.getUid();

        final Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        final SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        formattedDate = df.format(c.getTime());

        tgl_txt.setText(formattedDate);
        getData(formattedDate);
        jumlahnyeken = 0;
        pStatus = 0;
        jumlahSiswa = 0;
        persenan = 0;

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
                                    getData(tanggal);
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

        mProgress.setProgress(0);
        mProgress.setSecondaryProgress(100);
        mProgress.setMax(100);
        mProgress.setProgressDrawable(drawable);
    }

    private void getData(final String tanggal)
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
                            System.out.println("UID : " + uid);
                            System.out.println("JUMLAH SISWA : " + jumlahSiswa);

                            if (pemeriksa.equals(uid)) {
                                jumlahnyeken++;
                            } else {
                                jumlahnyeken = 0;
                            }

                            persenan = hitungPersen(jumlahSiswa, jumlahnyeken);

                            System.out.println("JUMLAH NYEKEN : " + jumlahnyeken);
                            jumlah_siswa_txt.setText(String.valueOf(jumlahSiswa));
                            hadir_status.setText(String.valueOf(jumlahnyeken));
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
