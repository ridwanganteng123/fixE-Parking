package com.example.pkke_parking.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pkke_parking.activities.DetailSiswaActivity;
import com.example.pkke_parking.adapters.AdapterDaftarSiswa;
import com.example.pkke_parking.R;
import com.example.pkke_parking.datas.model.DataDaftarSiswa;
import com.example.pkke_parking.dialogs.DialogTambahData;

import java.util.ArrayList;
import java.util.List;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.internal.NavigationMenu;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import io.github.yavski.fabspeeddial.FabSpeedDial;

public class DaftarSiswaFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private AdapterDaftarSiswa adapterDaftarSiswa;
    private List<DataDaftarSiswa> dataDaftarSiswaList;
    private FrameLayout frameLayout_data_siswa;
    private FirebaseRecyclerOptions<DataDaftarSiswa> options;
    private FirebaseRecyclerAdapter<DataDaftarSiswa, AdapterDaftarSiswa.AdapterDaftarSiswaView> adapter;
    private SwipeRefreshLayout refreshLayout;
    private DatabaseReference databaseReference;
    private ShimmerFrameLayout shimmerFrameLayout;
    private FabSpeedDial fabSpeedDial;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daftar_siswa, null);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycler_data_siswa);
        shimmerFrameLayout = view.findViewById(R.id.container_shimmer);
        refreshLayout = view.findViewById(R.id.refresh_trigger);
        fabSpeedDial = view.findViewById(R.id.fab_speed_dial);
        frameLayout_data_siswa = view.findViewById(R.id.frameLayout_data_siswa);


        if (recyclerView != null) {
            recyclerView.setHasFixedSize(true);
        }


        refreshLayout.setColorSchemeResources(R.color.startColor,R.color.endColor,R.color.colorPrimaryDark,R.color.colorPrimary);
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        dataDaftarSiswaList = new ArrayList<DataDaftarSiswa>();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("siswa");
        options = new FirebaseRecyclerOptions.Builder<DataDaftarSiswa>().setQuery(databaseReference, DataDaftarSiswa.class).build();
        adapter = new FirebaseRecyclerAdapter<DataDaftarSiswa, AdapterDaftarSiswa.AdapterDaftarSiswaView>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AdapterDaftarSiswa.AdapterDaftarSiswaView adapterDaftarSiswaView, int i, @NonNull final DataDaftarSiswa dataDaftarSiswa) {
                adapterDaftarSiswaView.nama.setText(dataDaftarSiswa.getNama());
                adapterDaftarSiswaView.nis.setText(dataDaftarSiswa.getNis());
                String imageUri = dataDaftarSiswa.getImageURL();
                Glide.with(getContext()).load(imageUri).into(adapterDaftarSiswaView.profile);

                adapterDaftarSiswaView.linearLayoutPencet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), DetailSiswaActivity.class);
                        intent.putExtra("nama", dataDaftarSiswa.getNama());
                        intent.putExtra("nis", dataDaftarSiswa.getNis());
                        intent.putExtra("img", dataDaftarSiswa.getImageURL());
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public AdapterDaftarSiswa.AdapterDaftarSiswaView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new AdapterDaftarSiswa.AdapterDaftarSiswaView(LayoutInflater.from(getActivity()).inflate(R.layout.format_data_siswa_recycler, null));
            }

            @Override
            public void onDataChanged() {
                super.onDataChanged();
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
            }
        };

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        refreshLayout.setRefreshing(false);
                        adapter = new FirebaseRecyclerAdapter<DataDaftarSiswa, AdapterDaftarSiswa.AdapterDaftarSiswaView>(options) {
                            @Override
                            protected void onBindViewHolder(@NonNull AdapterDaftarSiswa.AdapterDaftarSiswaView adapterDaftarSiswaView, int i, @NonNull final DataDaftarSiswa dataDaftarSiswa) {
                                adapterDaftarSiswaView.nama.setText(dataDaftarSiswa.getNama());
                                adapterDaftarSiswaView.nis.setText(dataDaftarSiswa.getNis());
                                String imageUri = dataDaftarSiswa.getImageURL();

                                adapterDaftarSiswaView.linearLayoutPencet.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(getActivity(), DetailSiswaActivity.class);
                                        intent.putExtra("nama", dataDaftarSiswa.getNama());
                                        intent.putExtra("nis", dataDaftarSiswa.getNis());
                                        startActivity(intent);
                                    }
                                });
                            }

                            @NonNull
                            @Override
                            public AdapterDaftarSiswa.AdapterDaftarSiswaView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                                return new AdapterDaftarSiswa.AdapterDaftarSiswaView(LayoutInflater.from(getActivity()).inflate(R.layout.format_data_siswa_recycler, null));
                            }

                            @Override
                            public void onDataChanged() {
                                super.onDataChanged();
                                shimmerFrameLayout.stopShimmer();
                                shimmerFrameLayout.setVisibility(View.GONE);
                            }
                        };
                    }
                }, 3000);
            }
        });

        fabSpeedDial.setMenuListener(new FabSpeedDial.MenuListener() {
            @Override
            public boolean onPrepareMenu(NavigationMenu navigationMenu) {
                return true;
            }
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                if (menuItem.getItemId()==R.id.filter){
                    Toast.makeText(getContext().getApplicationContext(), "Filter Clicked", Toast.LENGTH_SHORT).show();
                } else if (menuItem.getItemId()==R.id.showDialog)
                {
                    openDialog();
                }
                return false;
            }
            @Override
            public void onMenuClosed() {

            }
        });

        recyclerView.setAdapter(adapter);
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
    }

    @Override
    public void onResume() {
        super.onResume();
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmer();
    }

    @Override
    public void onStart() {
        super.onStart();
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmer();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        shimmerFrameLayout.stopShimmer();
        adapter.stopListening();
        shimmerFrameLayout.setVisibility(View.GONE);
    }

    public void openDialog() {
        DialogTambahData exampleDialog = new DialogTambahData();
        if (exampleDialog.getDialog() != null && exampleDialog.getDialog().getWindow() != null) {
            exampleDialog.getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            exampleDialog.getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        exampleDialog.show(getFragmentManager(), "Dialog Tambah Data");
    }
}