package com.opatan.e_parking_admin.activities;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.opatan.e_parking_admin.R;

public class BiodataDetailFragment extends Fragment {

    private TextView nama_txt, nis_txt,email_txt,no_pol_txt,no_sim_txt, tgl_lahir_txt, level_txt;
    private String nama_val, nis_val,email_val,no_pol_val,no_sim_val, tgl_lahir_val, level_val;

    public BiodataDetailFragment(String nama_val, String nis_val, String email_val, String no_pol_val, String no_sim_val, String tgl_lahir_val, String level_val) {
        this.nama_val = nama_val;
        this.nis_val = nis_val;
        this.email_val = email_val;
        this.no_pol_val = no_pol_val;
        this.no_sim_val = no_sim_val;
        this.tgl_lahir_val = tgl_lahir_val;
        this.level_val = level_val;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_biodata_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nama_txt = view.findViewById(R.id.get_nama);
        nis_txt = view.findViewById(R.id.get_nis);
        email_txt = view.findViewById(R.id.email_txt);
        tgl_lahir_txt = view.findViewById(R.id.tgl_lahir_txt);
        no_pol_txt = view.findViewById(R.id.nopol_txt);
        no_sim_txt = view.findViewById(R.id.nosim_txt);
        level_txt = view.findViewById(R.id.level_txt);

        nama_txt.setText(nama_val);
        nis_txt.setText(nis_val);
        email_txt.setText(email_val);
        no_pol_txt.setText(no_pol_val);
        no_sim_txt.setText(no_sim_val);
        tgl_lahir_txt.setText(tgl_lahir_val);
        level_txt.setText(level_val);
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
