package com.opatan.e_parking_petugas.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.opatan.e_parking_petugas.R;
import com.opatan.e_parking_petugas.datas.model.DataScanner;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DialogKonfirmasi extends DialogFragment {

    private String siswaId, pemeriksa;
    private TextView nama, kelas, plat1, plat2;
    private ImageView img_profile;
    private Button tolak, terima;
    private RelativeLayout content_progress;
    private LinearLayout content_dialog, content_unknown;
    private FirebaseUser currentUser;

    public DialogKonfirmasi(String siswaId)
    {
        this.siswaId = siswaId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.format_konfirmasi_dialog, container, false);
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawableResource(R.drawable.setengah_rounded_dialog);
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.format_konfirmasi_dialog, null);
        tolak = view.findViewById(R.id.tolak);
        nama = view.findViewById(R.id.nama_txt);
        kelas = view.findViewById(R.id.kelas);
        plat1 = view.findViewById(R.id.plat_1);
        plat2 = view.findViewById(R.id.plat_2);
        terima = view.findViewById(R.id.terima);
        content_dialog = view.findViewById(R.id.content_dialog);
        content_progress = view.findViewById(R.id.content_progress);
        img_profile = view.findViewById(R.id.img_profile);
        content_unknown = view.findViewById(R.id.content_unknown);

        tolak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext().getApplicationContext(), "Konfirmasi Gagal", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

        terima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendData();
                Toast.makeText(getContext().getApplicationContext(), "Konfirmasi Berhasil", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Siswa").child(siswaId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot ds) {
                    if (ds != null)
                    {
                        content_progress.setVisibility(View.GONE);
                        content_dialog.setVisibility(View.VISIBLE);
                        String nama_content = ds.child("nama").getValue(String.class);
                        String kelas_content = ds.child("level").getValue(String.class);
                        String plat1_content = ds.child("no_pol").getValue(String.class);
                        String plat2_content = ds.child("no_sim").getValue(String.class);
                        String imageUrl = ds.child("imageURL").getValue().toString();

                        nama.setText(nama_content);
                        kelas.setText(kelas_content);
                        plat1.setText(plat1_content);
                        plat2.setText(plat2_content);
                        Glide.with(getContext().getApplicationContext()).load(imageUrl).into(img_profile);
                    } else {
                        content_progress.setVisibility(View.GONE);
                        content_unknown.setVisibility(View.VISIBLE);
                    }
                }
            @Override
            public void onCancelled(DatabaseError de) {
                System.out.println("The read failed: " + de.getCode());
            }
        });

        builder.setView(view);
        return builder.create();
    }

    public void sendData()
    {
        String status = "";
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        pemeriksa = currentUser.getUid();
        SimpleDateFormat getDate = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat getTime = new SimpleDateFormat("HH:mm:ss");
        Date date_now = new Date();
        String date = getDate.format(date_now);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("ScanHarian").child(date);

        String waktu_masuk = getTime.format(date_now);
        int waktu = Integer.parseInt(getTime.format(date_now).split(":")[0]);

        if(waktu <= 07)
        {
            status = "hadir";
        } else
        {
            status = "terlambat";
        }

        DataScanner dataScanner = new DataScanner(siswaId, waktu_masuk, pemeriksa, status);
        ref.child(siswaId).setValue(dataScanner);
    }
}
