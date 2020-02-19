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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ActivityFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private AdapterHistoryParkir adapterHistoryParkir;
    private List<DataHistoryParkir> dataHistoryParkirList;
    private ShimmerFrameLayout shimmerFrameLayout;
    private FirebaseRecyclerOptions<DataHistoryParkir> options;
    private FirebaseRecyclerAdapter<DataHistoryParkir, AdapterHistoryParkir.AdapterHistoryParkirView> adapter;
    private FragmentTransaction ft;
    private String waktu, hari, siswa_txt, siswaId, petugasId;
    private DatabaseReference databaseReference1, databaseReference2, databaseReference3, databaseReference4;

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

        databaseReference1 = FirebaseDatabase.getInstance().getReference().child("ScanHarian").getRef();
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataHistoryParkirList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren())
                {
                    final String tanggal = ds.getRef().getKey();
                    databaseReference2 = FirebaseDatabase.getInstance().getReference().child("ScanHarian").child(tanggal).getRef();
                    databaseReference2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (final DataSnapshot dsTanggal : dataSnapshot.getChildren())
                            {
                                siswaId = dsTanggal.child("siswaId").getValue().toString();
                                petugasId = dsTanggal.child("pemeriksa").getValue().toString();
                                System.out.println("SISWAID : " + siswaId);
                                databaseReference3 = FirebaseDatabase.getInstance().getReference().child("Siswa").child(siswaId).getRef();
                                databaseReference3.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull final DataSnapshot dataSiswa) {
                                        databaseReference4 = FirebaseDatabase.getInstance().getReference().child("Petugas").child(petugasId).getRef();
                                        databaseReference4.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataPetugas) {
                                                siswa_txt = dataSiswa.child("nama").getValue().toString().split(" ")[0];
                                                System.out.println("NAMA SISWA : " + siswa_txt);
                                                final String petugas_txt = dataPetugas.child("nama").getValue().toString().split(" ")[0];
                                                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                                                Date myDate = null;
                                                try {
                                                    myDate = sdf.parse(tanggal);

                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }
                                                sdf.applyPattern("EEEE");
                                                hari = sdf.format(myDate);
                                                waktu = dsTanggal.child("waktu_masuk").getValue().toString();
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