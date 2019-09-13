package com.example.pkke_parking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterDaftarSiswa extends RecyclerView.Adapter<AdapterDaftarSiswa.AdapterDaftarSiswaView> {

    private Context context;
    private List<DataDaftarSiswa> dataDaftarSiswaList;

    public AdapterDaftarSiswa(Context context, List<DataDaftarSiswa> dataDaftarSiswaList) {
        this.dataDaftarSiswaList = dataDaftarSiswaList;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterDaftarSiswaView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.format_data_siswa_recycler, null);
        return new AdapterDaftarSiswaView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterDaftarSiswaView holder, int position) {
        final DataDaftarSiswa dataDaftarSiswa = dataDaftarSiswaList.get(position);
        holder.nama.setText(dataDaftarSiswa.getNama());
        holder.nis.setText(dataDaftarSiswa.getNis());
        holder.profile.setImageDrawable(context.getResources().getDrawable(dataDaftarSiswa.getImage()));
    }

    @Override
    public int getItemCount() {
        return dataDaftarSiswaList.size();
    }

    public class AdapterDaftarSiswaView extends RecyclerView.ViewHolder{

        TextView nama, nis;
        ImageView profile;

        public AdapterDaftarSiswaView(@NonNull View itemView) {
            super(itemView);

            nama = (TextView) itemView.findViewById(R.id.nama_data_siswa);
            nis = (TextView) itemView.findViewById(R.id.nis_data_siswa);
            profile = (ImageView) itemView.findViewById(R.id.profile_data_siswa);

        }
    }
}
