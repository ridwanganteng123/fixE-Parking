package com.example.pkke_parking.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pkke_parking.R;

public class ParkiredFragment extends Fragment {

    public ParkiredFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_parkired, container, false);
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
