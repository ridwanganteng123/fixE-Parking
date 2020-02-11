package com.opatan.e_parking_user.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.opatan.e_parking_user.adapters.AdapterHistoryParkir;
import com.opatan.e_parking_user.R;
import com.opatan.e_parking_user.datas.model.DataHistoryParkir;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class HistoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private AdapterHistoryParkir adapterHistoryParkir;
    DatabaseReference databaseReference1, databaseReference2, databaseReference3;
    private FirebaseUser currentUser;
    private String uid, pemeriksaId, waktu, tanggal, hari, siswaId, siswa_txt, petugas_txt;
    private List<DataHistoryParkir> dataHistoryParkirList;
    ShimmerFrameLayout shimmerFrameLayout;
    private FragmentTransaction ft;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, null);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        shimmerFrameLayout = view.findViewById(R.id.container_shimmer);
        recyclerView = view.findViewById(R.id.recycler_history);
        dataHistoryParkirList = new ArrayList<>();
        adapterHistoryParkir = new AdapterHistoryParkir(getContext(),dataHistoryParkirList);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        uid = currentUser.getUid();

        databaseReference1 = FirebaseDatabase.getInstance().getReference().child("ScanHarian").getRef();
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(final DataSnapshot snapshot : dataSnapshot.getChildren()){
                    final String tanggal = snapshot.getRef().getKey();
                    System.out.println("SNAPSHOT : " + snapshot);
                    System.out.println("TANGGAL : " + tanggal);
                    databaseReference2 = FirebaseDatabase.getInstance().getReference().child("Petugas").getRef();
                    databaseReference2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot snapshot1 : dataSnapshot.getChildren()){
                                System.out.println("SNAPSHOT1 : " + snapshot1.getKey());
                                System.out.println("SISWAID : " + snapshot.child(snapshot.getRef().getKey()).child(snapshot1.getKey()));
                                System.out.println("UID : " + uid);
                                String siswaId = snapshot.child(snapshot1.getKey()).toString();
                                if(uid.equals(siswaId)){
                                    final DataSnapshot history = snapshot.child(snapshot1.getKey());
                                    waktu = history.child("waktu_masuk").getValue(String.class);
                                    String nama = snapshot1.child("nama").getValue(String.class).split(" ")[0];
                                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                                    Date myDate = null;
                                    try {
                                        myDate = sdf.parse(tanggal);

                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    System.out.println(nama);
                                    sdf.applyPattern("EEEE");
                                    hari = sdf.format(myDate);
                                    dataHistoryParkirList.add(new DataHistoryParkir(waktu, tanggal, hari, nama));
                                    shimmerFrameLayout.hideShimmer();
                                    shimmerFrameLayout.setVisibility(View.GONE);

                                }
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
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterHistoryParkir);
    }
}
