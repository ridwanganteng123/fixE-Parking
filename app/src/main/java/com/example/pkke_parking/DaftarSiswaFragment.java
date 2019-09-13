package com.example.pkke_parking;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;


public class DaftarSiswaFragment extends Fragment{

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private AdapterDaftarSiswa adapterDaftarSiswa;
    private List<DataDaftarSiswa> dataDaftarSiswaList;
    private FrameLayout frameLayout_data_siswa;
    private TextView textViewUsername;
    private TextView textViewPassword;
    private Button button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_daftar_siswa, null);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        frameLayout_data_siswa = (FrameLayout) view.findViewById(R.id.frameLayout_data_siswa);
        ImageButton floatingActionButton = (ImageButton) view.findViewById(R.id.fab);
        final FragmentTransaction ft = getFragmentManager().beginTransaction();

        floatingActionButton.bringToFront();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_data_siswa);
        dataDaftarSiswaList = new ArrayList<>();
        adapterDaftarSiswa = new AdapterDaftarSiswa(getContext(), dataDaftarSiswaList);


        if (recyclerView != null) {
            recyclerView.setHasFixedSize(true);
        }

        dataDaftarSiswaList.add(
                new DataDaftarSiswa(
                        R.drawable.ic_launcher_foreground,
                        "Angga Gemilang",
                        "1718117111"
                )
        );

        dataDaftarSiswaList.add(
                new DataDaftarSiswa(
                        R.drawable.ic_launcher_foreground,
                        "Emir Othman",
                        "1718117232"
                )
        );

        dataDaftarSiswaList.add(
                new DataDaftarSiswa(
                        R.drawable.ic_launcher_foreground,
                        "Vioriza Qiyaski",
                        "1718117123"
                )
        );

        dataDaftarSiswaList.add(
                new DataDaftarSiswa(
                        R.drawable.ic_launcher_foreground,
                        "Ridwan Mutamasiqin",
                        "1718117643"
                )
        );

        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapterDaftarSiswa);

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
