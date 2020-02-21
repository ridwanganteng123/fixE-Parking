package com.opatan.e_parking_admin.adapters;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.opatan.e_parking_admin.R;
import com.opatan.e_parking_admin.datas.model.DataDaftarPetugas;
import com.opatan.e_parking_admin.datas.model.DataDaftarSiswa;
import com.opatan.e_parking_admin.fragments.DaftarPetugasFragment;

import org.w3c.dom.Text;

import java.util.List;

public class AdapterDaftarPetugas extends RecyclerView.Adapter<AdapterDaftarPetugas.AdapterDaftarPetugasView> {

    private Context context;
    private List<DataDaftarPetugas> dataDaftarPetugasList;
    private LinearLayout linearPencet;
    private TextView nama_data_siswa;
    FirebaseDataListener listener;
    public AdapterDaftarPetugas(Context context, List<DataDaftarPetugas> dataDaftarPetugasList) {
        this.dataDaftarPetugasList = dataDaftarPetugasList;
        this.context = context;
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


    @NonNull
    @Override
    public AdapterDaftarPetugasView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.format_data_siswa_recycler, null);
        return new AdapterDaftarPetugasView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterDaftarPetugasView holder, final int position) {
        final String name = dataDaftarPetugasList.get(position).getNama();
        System.out.println("BARANG DATA one by one "+position+dataDaftarPetugasList.size());
        holder.linearLayoutPencet.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_view);
                dialog.setTitle("Pilih Aksi");
                dialog.show();

                Button btnDelete = (Button) dialog.findViewById(R.id.delete);

                //Apabila tombol delete dipencet
                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        listener.onDeleteData(dataDaftarPetugasList.get(position), position);
                    }
                });
                return true;
            }
        });
        holder.nama.setText(name);

    }

    @Override
    public int getItemCount() {
        return dataDaftarPetugasList.size();
    }
    public interface FirebaseDataListener{
        void onDeleteData(DataDaftarPetugas dataDaftarPetugas,int position);
    }
    private void DeletePetugas(String petugasId){
        DatabaseReference petugas = FirebaseDatabase.getInstance().getReference().child("Petugas").child(petugasId);
        petugas.removeValue();
        Toast.makeText(context,"Data berhasil dihapus",Toast.LENGTH_LONG).show();

    }


}
