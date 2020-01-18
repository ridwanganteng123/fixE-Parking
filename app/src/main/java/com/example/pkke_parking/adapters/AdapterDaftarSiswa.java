package com.example.pkke_parking.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pkke_parking.R;
import com.example.pkke_parking.activities.DetailSiswaActivity;
import com.example.pkke_parking.datas.model.DataDaftarSiswa;
import com.example.pkke_parking.fragments.DaftarSiswaFragment;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterDaftarSiswa extends RecyclerView.Adapter<AdapterDaftarSiswa.AdapterDaftarSiswaView> {

    private Context context;
    private List<DataDaftarSiswa> dataDaftarSiswaList;
    boolean showShimmer = true;
    int SHIMMER_ITEM = 5;

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
        if (showShimmer)
        {

        }
    }

    @Override
    public int getItemCount() {
        return showShimmer?SHIMMER_ITEM:dataDaftarSiswaList.size();
    }

    public static class AdapterDaftarSiswaView extends RecyclerView.ViewHolder{

        public ShimmerFrameLayout shimmerFrameLayout;
        public TextView nama, nis;
        public ImageView profile;
        public LinearLayout linearLayoutPencet;

        public AdapterDaftarSiswaView(@NonNull View itemView) {
            super(itemView);

            shimmerFrameLayout = itemView.findViewById(R.id.shimmer_layout);
            linearLayoutPencet = itemView.findViewById(R.id.linearPencet);
            nama = itemView.findViewById(R.id.nama_data_siswa);
            nis = itemView.findViewById(R.id.nis_data_siswa);
            profile = itemView.findViewById(R.id.profile_data_siswa);

        }
    }
}
