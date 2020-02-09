package com.opatan.e_parking_petugas.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.opatan.e_parking_petugas.R;
import com.opatan.e_parking_petugas.datas.model.DataHistoryParkir;

import java.util.List;

public class AdapterHistoryParkir extends RecyclerView.Adapter<AdapterHistoryParkir.AdapterHistoryParkirView> {

    private Context context;
    private List<DataHistoryParkir> dataHistoryParkirList;

    public AdapterHistoryParkir(Context context, List<DataHistoryParkir> dataHistoryParkirList) {
        this.dataHistoryParkirList = dataHistoryParkirList;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterHistoryParkirView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.format_history_recycler, null);
        return new AdapterHistoryParkirView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterHistoryParkirView holder, int position) {
        final DataHistoryParkir dataHistoryParkir = dataHistoryParkirList.get(position);
        holder.waktu_masuk.setText(dataHistoryParkir.getWaktu_masuk());
        holder.tanggal.setText(dataHistoryParkir.getHari() + ", " + dataHistoryParkir.getTanggal());
        holder.siswa.setText("Anda Telah Memeriksa " + dataHistoryParkir.getSiswa());
    }

    @Override
    public int getItemCount() {
        return dataHistoryParkirList.size();
    }

    public class AdapterHistoryParkirView extends RecyclerView.ViewHolder{

        public TextView waktu_masuk, siswa, tanggal;

        public AdapterHistoryParkirView(@NonNull View itemView) {
            super(itemView);

            waktu_masuk = itemView.findViewById(R.id.waktu);
            siswa = itemView.findViewById(R.id.siswa);
            tanggal = itemView.findViewById(R.id.tanggal);
        }
    }
}
