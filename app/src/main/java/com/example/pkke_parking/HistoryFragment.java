package com.example.pkke_parking;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class HistoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private AdapterHistoryParkir adapterHistoryParkir;
    private List<DataHistoryParkir> dataHistoryParkirList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, null);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_history);
        dataHistoryParkirList = new ArrayList<>();
        adapterHistoryParkir = new AdapterHistoryParkir(getContext(),dataHistoryParkirList);

        dataHistoryParkirList.add(
                new DataHistoryParkir(
                        "Rabu, 27 Agustus 2019",
                        "06 : 50"
                ));

        dataHistoryParkirList.add(
                new DataHistoryParkir(
                        "Kamis, 28 Agustus 2019",
                        "06 : 38"
                ));

        dataHistoryParkirList.add(
                new DataHistoryParkir(
                        " Jum'at, 29 Agustus 2019",
                        "06 : 45"
                ));

        dataHistoryParkirList.add(
                new DataHistoryParkir(
                        " Senin, 2 September 2019",
                        "06 : 45"
                ));

        dataHistoryParkirList.add(
                new DataHistoryParkir(
                        "Selasa, 3 September 2019",
                        "06 : 55"
                ));

//        dataHistoryParkirList.add(
//                new DataHistoryParkir(
//                        "Rabu, 4 September 2019",
//                        "06 : 40"
//                ));
//
//        dataHistoryParkirList.add(
//                new DataHistoryParkir(
//                        "Kamis, 5 September 2019",
//                        "06 : 35"
//                ));
//
//        dataHistoryParkirList.add(
//                new DataHistoryParkir(
//                        "Jumat, 6 September 2019",
//                        "06 : 00"
//                ));

        if (recyclerView != null) {
            recyclerView.setHasFixedSize(true);
        }

        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapterHistoryParkir);
    }
}
