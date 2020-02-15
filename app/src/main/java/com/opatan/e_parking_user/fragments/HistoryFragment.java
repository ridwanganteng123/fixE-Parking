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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class HistoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private AdapterHistoryParkir adapterHistoryParkir;
    DatabaseReference databaseReference1, databaseReference2, databaseReference3;
    private FirebaseUser currentUser;
    private String uid, pemeriksaId, waktu, siswaId, siswa_txt, petugas_txt;
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
                    siswaId = snapshot.child(uid).child("siswaId").getValue(String.class);
                    pemeriksaId =snapshot.child(uid).child("pemeriksa").getValue(String.class);
                    if(siswaId != null){
                        System.out.println("SISWAID : " + siswaId);
                        System.out.println("WAKTU : " + waktu);
                        System.out.println("PEMERIKSA : " + pemeriksaId);
                        databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Petugas").child(pemeriksaId).getRef();
                        databaseReference1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                System.out.println(dataSnapshot.getValue());
                                waktu = snapshot.child(uid).child("waktu_masuk").getValue(String.class);
                                String namaPemeriksa = dataSnapshot.child("nama").getValue(String.class).split(" ")[0];
                                SimpleDateFormat format1=new SimpleDateFormat("dd-MM-yyyy");
                                Date dt1= null;
                                try {
                                    dt1 = format1.parse(tanggal);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                DateFormat format2=new SimpleDateFormat("EEEE");
                                String finalDay=format2.format(dt1);
                                dataHistoryParkirList.add(new DataHistoryParkir(waktu, tanggal, finalDay, namaPemeriksa));
                                shimmerFrameLayout.hideShimmer();
                                shimmerFrameLayout.setVisibility(View.GONE);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                    System.out.println("UID" + snapshot.getKey());
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
