package com.example.pkke_parking.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pkke_parking.R;
import com.example.pkke_parking.datas.model.DataHistoryParkir;

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
        holder.waktu.setText(dataHistoryParkir.getWaktu());
        holder.tanggal.setText(dataHistoryParkir.getTanggal());
    }

    @Override
    public int getItemCount() {
        return dataHistoryParkirList.size();
    }

    public class AdapterHistoryParkirView extends RecyclerView.ViewHolder{

        TextView waktu;
        TextView tanggal;

        public AdapterHistoryParkirView(@NonNull View itemView) {
            super(itemView);

            waktu = (TextView) itemView.findViewById(R.id.waktu);
            tanggal = (TextView) itemView.findViewById(R.id.tanggal);
        }
    }
}
