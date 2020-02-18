package com.opatan.e_parking_admin.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.opatan.e_parking_admin.R;
import com.opatan.e_parking_admin.adapters.AdapterHistoryParkir;
import com.opatan.e_parking_admin.datas.model.DataHistoryParkir;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ActivityFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private AdapterHistoryParkir adapterHistoryParkir;
    private List<DataHistoryParkir> dataHistoryParkirList;
    private ShimmerFrameLayout shimmerFrameLayout;
    private FirebaseRecyclerOptions<DataHistoryParkir> options;
    private FirebaseRecyclerAdapter<DataHistoryParkir, AdapterHistoryParkir.AdapterHistoryParkirView> adapter;
    private FragmentTransaction ft;
    private String siswaId, pemeriksaId, waktu, tanggal, hari, siswa_txt, petugas_txt;
    private DatabaseReference databaseReference1, databaseReference2, databaseReference3;

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
        return inflater.inflate(R.layout.fragment_activity, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        shimmerFrameLayout = view.findViewById(R.id.container_shimmer);
        recyclerView = view.findViewById(R.id.recycler_history);
        dataHistoryParkirList = new ArrayList<>();
        adapterHistoryParkir = new AdapterHistoryParkir(getContext(),dataHistoryParkirList);

        databaseReference1 = FirebaseDatabase.getInstance().getReference().child("ScanHarian").child("12-02-2020");
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren())
                {
                    waktu = ds.child("waktu_masuk").getValue(String.class);
                    siswaId = ds.child("siswaId").getValue(String.class);
                    pemeriksaId = ds.child("pemeriksa").getValue(String.class);
                    tanggal = databaseReference1.getKey();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", java.util.Locale.getDefault());
                    Date myDate = null;
                    try {
                        myDate = sdf.parse(tanggal);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    sdf.applyPattern("EEEE");
                    hari = sdf.format(myDate);

                    databaseReference2 = FirebaseDatabase.getInstance().getReference().child("Siswa").child(siswaId);
                    databaseReference2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            siswa_txt = dataSnapshot.child("nama").getValue(String.class).split(" ")[0];

                            databaseReference3 = FirebaseDatabase.getInstance().getReference().child("Petugas").child(pemeriksaId);
                            databaseReference3.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    petugas_txt = dataSnapshot.child("nama").getValue(String.class).split(" ")[0];
                                    dataHistoryParkirList.add(new DataHistoryParkir(waktu, tanggal, siswa_txt, hari, petugas_txt));
                                    shimmerFrameLayout.hideShimmer();
                                    shimmerFrameLayout.setVisibility(View.GONE);
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

        if (recyclerView != null) {
            recyclerView.setHasFixedSize(true);
        }

        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapterHistoryParkir);
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
