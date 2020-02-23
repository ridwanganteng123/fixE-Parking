package com.opatan.e_parking_admin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.opatan.e_parking_admin.R;
import com.opatan.e_parking_admin.datas.model.DataDetailStatistik;
import com.opatan.e_parking_admin.datas.model.DataHistoryParkir;

import java.util.List;

public class AdapterSiswaStatistik extends RecyclerView.Adapter<AdapterSiswaStatistik.AdapterSiswaStatistikView> {

    private Context context;
    private List<DataDetailStatistik> dataDetailStatistikList;

    public AdapterSiswaStatistik(Context context, List<DataDetailStatistik> dataDetailStatistikList) {
        this.dataDetailStatistikList = dataDetailStatistikList;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterSiswaStatistikView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.format_siswa_statistik_recycler, null);
        return new AdapterSiswaStatistikView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterSiswaStatistikView holder, int position) {
        final DataDetailStatistik dataDetailStatistik = dataDetailStatistikList.get(position);
        holder.waktu_masuk.setText(dataDetailStatistik.getWaktu_masuk());
        holder.siswa.setText(dataDetailStatistik.getSiswa());
        holder.petugas.setText("Pemeriksa : " + dataDetailStatistik.getPetugas());
        Glide.with(context).load(dataDetailStatistik.getImageUrl()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return dataDetailStatistikList.size();
    }

    public class AdapterSiswaStatistikView extends RecyclerView.ViewHolder{

        ImageView image;
        TextView waktu_masuk, siswa, petugas, nis;

        AdapterSiswaStatistikView(@NonNull View itemView) {
            super(itemView);

            petugas = itemView.findViewById(R.id.pemeriksa_txt);
            waktu_masuk = itemView.findViewById(R.id.jam_masuk_txt);
            siswa = itemView.findViewById(R.id.nama_txt);
            image = itemView.findViewById(R.id.siswa_profile);
        }
    }
}
