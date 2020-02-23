package com.opatan.e_parking_admin.fragments;

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

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.opatan.e_parking_admin.activities.DetailSiswaActivity;
import com.opatan.e_parking_admin.adapters.AdapterDaftarPetugas;
import com.opatan.e_parking_admin.adapters.AdapterDaftarSiswa;
import com.opatan.e_parking_admin.R;
import com.opatan.e_parking_admin.datas.model.DataDaftarPetugas;
import com.opatan.e_parking_admin.datas.model.DataDaftarSiswa;
import com.opatan.e_parking_admin.dialogs.DialogTambahDataSiswa;

import java.util.ArrayList;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.internal.NavigationMenu;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import io.github.yavski.fabspeeddial.FabSpeedDial;

public class DaftarSiswaFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private AdapterDaftarSiswa adapterDaftarSiswa;
    private ArrayList<DataDaftarSiswa> dataDaftarSiswaList;
    private FrameLayout frameLayout_data_siswa;
    private SlidingUpPanelLayout slidingUpPanelLayout;
    private Spinner spinnerGender, spinnerKelas;
    private FirebaseRecyclerOptions<DataDaftarSiswa> options;
    private FirebaseRecyclerAdapter<DataDaftarSiswa, AdapterDaftarSiswa.AdapterDaftarSiswaView> adapter;
    private DatabaseReference databaseReference;
    private ShimmerFrameLayout shimmerFrameLayout;

    private FabSpeedDial fabSpeedDial;
    private EditText cari_siswa;
    private FragmentTransaction ft;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daftar_users, null);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ft = getFragmentManager().beginTransaction();
        recyclerView = view.findViewById(R.id.recycler_data_siswa);
        shimmerFrameLayout = view.findViewById(R.id.container_shimmer);
        fabSpeedDial = view.findViewById(R.id.fab_speed_dial);
        frameLayout_data_siswa = view.findViewById(R.id.frameLayout_data_siswa);
        slidingUpPanelLayout = view.findViewById(R.id.sliding_up_panel_layout);
        spinnerGender = view.findViewById(R.id.spinner_gender);
        spinnerKelas = view.findViewById(R.id.spinner_kelas);
        cari_siswa = view.findViewById(R.id.cari_siswa);

        if (recyclerView != null) {
            recyclerView.setHasFixedSize(true);
        }

        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        dataDaftarSiswaList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Siswa");

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
                        intent.putExtra("id", dataDaftarSiswa.getSiswaId());
                        intent.putExtra("nama", dataDaftarSiswa.getNama());
                        intent.putExtra("img", dataDaftarSiswa.getImageURL());
                        intent.putExtra("tgl_lahir", dataDaftarSiswa.getTgl_lahir());
                        intent.putExtra("no_pol", dataDaftarSiswa.getNo_pol());
                        intent.putExtra("no_sim", dataDaftarSiswa.getNo_sim());
                        intent.putExtra("nis", dataDaftarSiswa.getNis());
                        intent.putExtra("email", dataDaftarSiswa.getEmail());
                        intent.putExtra("pwd", dataDaftarSiswa.getPwd());
                        intent.putExtra("level", dataDaftarSiswa.getLevel());
                        intent.putExtra("kelas", dataDaftarSiswa.getKelas());
                        intent.putExtra("gender", dataDaftarSiswa.getGender());
                        intent.putExtra("role", dataDaftarSiswa.getLevel());
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

        fabSpeedDial.setMenuListener(new FabSpeedDial.MenuListener() {
            @Override
            public boolean onPrepareMenu(NavigationMenu navigationMenu) {
                return true;
            }

            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                if (menuItem.getItemId()==R.id.filter)
                {
                    slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
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

        cari_siswa.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!editable.toString().isEmpty())
                {
                    search(editable.toString());

                }
            }
        });

        final int[] iGenderSelection = {spinnerGender.getSelectedItemPosition()};
        final int[] iKelasSelection = {spinnerKelas.getSelectedItemPosition()};

        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (iGenderSelection[0] != i){
                    String item = spinnerGender.getSelectedItem().toString();
                    searchSpinner("gender",item);
                }
                iGenderSelection[0] = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        spinnerKelas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (iKelasSelection[0] != i) {
                    String item = spinnerKelas.getSelectedItem().toString();
                    searchSpinner("kelas", item);
                }
                iKelasSelection[0] = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                
            }
        });
    }


    public void search(String editable)
    {
        Query firebaseSearchQuery = databaseReference.orderByChild("nama").startAt(editable).endAt(editable+"\uf8ff");
        options = new FirebaseRecyclerOptions.Builder<DataDaftarSiswa>().setQuery(firebaseSearchQuery, DataDaftarSiswa.class).build();
        adapter = new FirebaseRecyclerAdapter<DataDaftarSiswa, AdapterDaftarSiswa.AdapterDaftarSiswaView>(options) {
            @NonNull
            @Override
            public AdapterDaftarSiswa.AdapterDaftarSiswaView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new AdapterDaftarSiswa.AdapterDaftarSiswaView(LayoutInflater.from(getActivity()).inflate(R.layout.format_data_siswa_recycler, null));
            }

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
                        intent.putExtra("id", dataDaftarSiswa.getSiswaId());
                        intent.putExtra("nama", dataDaftarSiswa.getNama());
                        intent.putExtra("img", dataDaftarSiswa.getImageURL());
                        intent.putExtra("tgl_lahir", dataDaftarSiswa.getTgl_lahir());
                        intent.putExtra("no_pol", dataDaftarSiswa.getNo_pol());
                        intent.putExtra("no_sim", dataDaftarSiswa.getNo_sim());
                        intent.putExtra("nis", dataDaftarSiswa.getNis());
                        intent.putExtra("email", dataDaftarSiswa.getEmail());
                        intent.putExtra("pwd", dataDaftarSiswa.getPwd());
                        intent.putExtra("level", dataDaftarSiswa.getLevel());
                        intent.putExtra("kelas", dataDaftarSiswa.getKelas());
                        intent.putExtra("gender", dataDaftarSiswa.getGender());
                        intent.putExtra("role", dataDaftarSiswa.getLevel());
                        startActivity(intent);
                        startActivity(intent);
                        System.out.println(intent.putExtra("nama", dataDaftarSiswa.getNama()));
                    }
                });
            }
        };
        adapter.startListening();
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    public void searchSpinner(String key, String value)
    {
        Query firebaseSearchQuery = databaseReference.orderByChild(key).equalTo(value);
        options = new FirebaseRecyclerOptions.Builder<DataDaftarSiswa>().setQuery(firebaseSearchQuery, DataDaftarSiswa.class).build();
        adapter = new FirebaseRecyclerAdapter<DataDaftarSiswa, AdapterDaftarSiswa.AdapterDaftarSiswaView>(options) {
            @NonNull
            @Override
            public AdapterDaftarSiswa.AdapterDaftarSiswaView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new AdapterDaftarSiswa.AdapterDaftarSiswaView(LayoutInflater.from(getActivity()).inflate(R.layout.format_data_siswa_recycler, null));
            }

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
                        intent.putExtra("id", dataDaftarSiswa.getSiswaId());
                        intent.putExtra("nama", dataDaftarSiswa.getNama());
                        intent.putExtra("img", dataDaftarSiswa.getImageURL());
                        intent.putExtra("tgl_lahir", dataDaftarSiswa.getTgl_lahir());
                        intent.putExtra("no_pol", dataDaftarSiswa.getNo_pol());
                        intent.putExtra("no_sim", dataDaftarSiswa.getNo_sim());
                        intent.putExtra("nis", dataDaftarSiswa.getNis());
                        intent.putExtra("email", dataDaftarSiswa.getEmail());
                        intent.putExtra("pwd", dataDaftarSiswa.getPwd());
                        intent.putExtra("level", dataDaftarSiswa.getLevel());
                        intent.putExtra("kelas", dataDaftarSiswa.getKelas());
                        intent.putExtra("gender", dataDaftarSiswa.getGender());
                        startActivity(intent);
                        startActivity(intent);
                        System.out.println(intent.putExtra("nama", dataDaftarSiswa.getNama()));
                    }
                });
            }
        };
        adapter.startListening();
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
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
        DialogTambahDataSiswa exampleDialog = new DialogTambahDataSiswa();
        if (exampleDialog.getDialog() != null && exampleDialog.getDialog().getWindow() != null) {
            exampleDialog.getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            exampleDialog.getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        Bundle bundle = new Bundle();
        bundle.putString("action","insert");
        exampleDialog.setArguments(bundle);
        exampleDialog.show(getFragmentManager(), "Dialog Tambah Data");
    }
}