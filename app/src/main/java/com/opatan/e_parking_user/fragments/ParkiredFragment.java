package com.opatan.e_parking_user.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.opatan.e_parking_user.R;

public class ParkiredFragment extends Fragment {

    private FirebaseUser currentUser;
    private String uid, nama_txt;
    private TextView nama;
    private DatabaseReference databaseReference;

    public ParkiredFragment(String nama) {
        this.nama_txt = nama;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_parkired, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        nama = view.findViewById(R.id.txt_selamat_datang);
        nama.setText("Selamat Datang, " + nama_txt);
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
