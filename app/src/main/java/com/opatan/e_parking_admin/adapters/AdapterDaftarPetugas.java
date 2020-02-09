package com.opatan.e_parking_admin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.opatan.e_parking_admin.R;
import com.opatan.e_parking_admin.datas.model.DataDaftarPetugas;
import com.opatan.e_parking_admin.datas.model.DataDaftarSiswa;

import java.util.List;

public class AdapterDaftarPetugas extends RecyclerView.Adapter<AdapterDaftarPetugas.AdapterDaftarPetugasView> {

    private Context context;
    private List<DataDaftarPetugas> dataDaftarPetugasList;

    public AdapterDaftarPetugas(Context context, List<DataDaftarPetugas> dataDaftarPetugasList) {
        this.dataDaftarPetugasList = dataDaftarPetugasList;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterDaftarPetugasView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.format_data_siswa_recycler, null);
        return new AdapterDaftarPetugasView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterDaftarPetugasView holder, int position) {
    }

    @Override
    public int getItemCount() {
        return dataDaftarPetugasList.size();
    }

    public static class AdapterDaftarPetugasView extends RecyclerView.ViewHolder{

        public TextView nama, nis;
        public ImageView profile;
        public LinearLayout linearLayoutPencet;

        public AdapterDaftarPetugasView(@NonNull View itemView) {
            super(itemView);

            linearLayoutPencet = itemView.findViewById(R.id.linearPencet);
            nama = itemView.findViewById(R.id.nama_data_siswa);
            nis = itemView.findViewById(R.id.nis_data_siswa);
            profile = itemView.findViewById(R.id.profile_data_siswa);

        }
    }
}
