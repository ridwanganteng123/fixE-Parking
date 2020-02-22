package com.opatan.e_parking_admin.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.internal.NavigationMenu;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.opatan.e_parking_admin.R;
import com.opatan.e_parking_admin.activities.DetailSiswaActivity;
import com.opatan.e_parking_admin.adapters.AdapterDaftarPetugas;
import com.opatan.e_parking_admin.adapters.AdapterDaftarSiswa;
import com.opatan.e_parking_admin.datas.model.DataDaftarPetugas;
import com.opatan.e_parking_admin.datas.model.DataDaftarSiswa;
import com.opatan.e_parking_admin.dialogs.DialogTambahDataPetugas;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;

import javax.xml.datatype.DatatypeFactory;

import io.github.yavski.fabspeeddial.FabSpeedDial;

public class DaftarPetugasFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private AdapterDaftarSiswa adapterDaftarSiswa;
    private ArrayList<DataDaftarSiswa> dataDaftarSiswaList;
    private FrameLayout frameLayout_data_siswa;
    private SlidingUpPanelLayout slidingUpPanelLayout;
    private FirebaseRecyclerOptions<DataDaftarPetugas> options;
    private FirebaseRecyclerAdapter<DataDaftarPetugas, AdapterDaftarPetugas.AdapterDaftarPetugasView> adapter;
    private DatabaseReference databaseReference;
    private ShimmerFrameLayout shimmerFrameLayout;
    private FabSpeedDial fabSpeedDial;
    private EditText cari_siswa;
    private Spinner spinnerGender, spinnerKelas;
    private FragmentTransaction ft;
    private Context context;
    Bundle bundle;

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
        cari_siswa = view.findViewById(R.id.cari_siswa);
        spinnerGender = view.findViewById(R.id.spinner_gender);
        spinnerKelas = view.findViewById(R.id.spinner_kelas);

        if (recyclerView != null) {
            recyclerView.setHasFixedSize(true);
        }

        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        dataDaftarSiswaList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Petugas");

        options = new FirebaseRecyclerOptions.Builder<DataDaftarPetugas>().setQuery(databaseReference, DataDaftarPetugas.class).build();
        adapter = new FirebaseRecyclerAdapter<DataDaftarPetugas, AdapterDaftarPetugas.AdapterDaftarPetugasView>(options) {

            @Override
            protected void onBindViewHolder(@NonNull AdapterDaftarPetugas.AdapterDaftarPetugasView adapterDaftarPetugasView, int i, @NonNull final DataDaftarPetugas dataDaftarPetugas) {
                adapterDaftarPetugasView.nama.setText(dataDaftarPetugas.getNama());
                adapterDaftarPetugasView.nis.setText(dataDaftarPetugas.getNis());
                String imageUri = dataDaftarPetugas.getImageURL();
                Glide.with(getContext()).load(imageUri).into(adapterDaftarPetugasView.profile);

                adapterDaftarPetugasView.linearLayoutPencet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), DetailSiswaActivity.class);
                        intent.putExtra("id", dataDaftarPetugas.getPetugasId());
                        intent.putExtra("nama", dataDaftarPetugas.getNama());
                        intent.putExtra("nis", dataDaftarPetugas.getNis());
                        intent.putExtra("img", dataDaftarPetugas.getImageURL());
                        intent.putExtra("tgl_lahir", dataDaftarPetugas.getTgl_lahir());
                        intent.putExtra("email", dataDaftarPetugas.getEmail());
                        intent.putExtra("pwd", dataDaftarPetugas.getPwd());
                        intent.putExtra("level", dataDaftarPetugas.getLevel());
                        intent.putExtra("kelas", dataDaftarPetugas.getKelas());
                        intent.putExtra("gender", dataDaftarPetugas.getGender());
                        startActivity(intent);
                    }
                });
                adapterDaftarPetugasView.linearLayoutPencet.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        bundle = getArguments();
                        final Dialog dialog = new Dialog(context);
                        dialog.setContentView(R.layout.dialog_view);
                        dialog.setTitle("Pilih Aksi");
                        dialog.show();

                        Button btnDelete = dialog.findViewById(R.id.delete);

                        //Apabila tombol delete dipencet
                        btnDelete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();

                            }
                        });
                        return true;
                    }
                });
            }

            @NonNull
            @Override
            public AdapterDaftarPetugas.AdapterDaftarPetugasView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new AdapterDaftarPetugas.AdapterDaftarPetugasView(LayoutInflater.from(getActivity()).inflate(R.layout.format_data_siswa_recycler, null));
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



    }
    private Boolean DeletePetugas(String petugasId){
        DatabaseReference petugas = FirebaseDatabase.getInstance().getReference().child("Petugas").child(petugasId);
        petugas.removeValue();
        Toast.makeText(context,"Data berhasil dihapus",Toast.LENGTH_LONG).show();
        return true;
    }

    public void search(String editable)
    {
        Query firebaseSearchQuery = databaseReference.orderByChild("nama").startAt(editable).endAt(editable+"\uf8ff");
        options = new FirebaseRecyclerOptions.Builder<DataDaftarPetugas>().setQuery(firebaseSearchQuery, DataDaftarPetugas.class).build();
        adapter = new FirebaseRecyclerAdapter<DataDaftarPetugas, AdapterDaftarPetugas.AdapterDaftarPetugasView>(options) {
            @NonNull
            @Override
            public AdapterDaftarPetugas.AdapterDaftarPetugasView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new AdapterDaftarPetugas.AdapterDaftarPetugasView(LayoutInflater.from(getActivity()).inflate(R.layout.format_data_siswa_recycler, null));
            }

            @Override
            protected void onBindViewHolder(@NonNull AdapterDaftarPetugas.AdapterDaftarPetugasView adapterDaftarPetugasView, int i, @NonNull final DataDaftarPetugas dataDaftarPetugas) {
                adapterDaftarPetugasView.nama.setText(dataDaftarPetugas.getNama());
                adapterDaftarPetugasView.nis.setText(dataDaftarPetugas.getNis());
                String imageUri = dataDaftarPetugas.getImageURL();
                Glide.with(getContext()).load(imageUri).into(adapterDaftarPetugasView.profile);

                adapterDaftarPetugasView.linearLayoutPencet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), DetailSiswaActivity.class);
                        intent.putExtra("id", dataDaftarPetugas.getPetugasId());
                        intent.putExtra("nama", dataDaftarPetugas.getNama());
                        intent.putExtra("nis", dataDaftarPetugas.getNis());
                        intent.putExtra("img", dataDaftarPetugas.getImageURL());
                        intent.putExtra("tgl_lahir", dataDaftarPetugas.getTgl_lahir());
                        intent.putExtra("email", dataDaftarPetugas.getEmail());
                        intent.putExtra("pwd", dataDaftarPetugas.getPwd());
                        intent.putExtra("level", dataDaftarPetugas.getLevel());
                        intent.putExtra("kelas", dataDaftarPetugas.getKelas());
                        intent.putExtra("gender", dataDaftarPetugas.getGender());
                        startActivity(intent);
                    }
                });
            }
        };
        adapter.startListening();
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    public void dropdownSearch(String key, String value)
    {
        Query firebaseSearchQuery = databaseReference.orderByChild(key).equalTo(value);
        options = new FirebaseRecyclerOptions.Builder<DataDaftarPetugas>().setQuery(firebaseSearchQuery, DataDaftarPetugas.class).build();
        adapter = new FirebaseRecyclerAdapter<DataDaftarPetugas, AdapterDaftarPetugas.AdapterDaftarPetugasView>(options) {
            @NonNull
            @Override
            public AdapterDaftarPetugas.AdapterDaftarPetugasView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new AdapterDaftarPetugas.AdapterDaftarPetugasView(LayoutInflater.from(getActivity()).inflate(R.layout.format_data_siswa_recycler, null));
            }

            @Override
            protected void onBindViewHolder(@NonNull AdapterDaftarPetugas.AdapterDaftarPetugasView adapterDaftarPetugasView, int i, @NonNull final DataDaftarPetugas dataDaftarPetugas) {
                adapterDaftarPetugasView.nama.setText(dataDaftarPetugas.getNama());
                adapterDaftarPetugasView.nis.setText(dataDaftarPetugas.getNis());
                String imageUri = dataDaftarPetugas.getImageURL();
                Glide.with(getContext()).load(imageUri).into(adapterDaftarPetugasView.profile);

                adapterDaftarPetugasView.linearLayoutPencet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), DetailSiswaActivity.class);
                        intent.putExtra("id", dataDaftarPetugas.getPetugasId());
                        intent.putExtra("nama", dataDaftarPetugas.getNama());
                        intent.putExtra("nis", dataDaftarPetugas.getNis());
                        intent.putExtra("img", dataDaftarPetugas.getImageURL());
                        intent.putExtra("tgl_lahir", dataDaftarPetugas.getTgl_lahir());
                        intent.putExtra("email", dataDaftarPetugas.getEmail());
                        intent.putExtra("pwd", dataDaftarPetugas.getPwd());
                        intent.putExtra("level", dataDaftarPetugas.getLevel());
                        intent.putExtra("kelas", dataDaftarPetugas.getKelas());
                        intent.putExtra("gender", dataDaftarPetugas.getGender());
                        startActivity(intent);
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
        DialogTambahDataPetugas exampleDialog = new DialogTambahDataPetugas();
        if (exampleDialog.getDialog() != null && exampleDialog.getDialog().getWindow() != null) {
            exampleDialog.getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            exampleDialog.getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        exampleDialog.show(getFragmentManager(), "Dialog Tambah Data");
    }
}