package com.example.pkke_parking.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pkke_parking.R;
import com.example.pkke_parking.dialogs.DialogTambahData;
import com.example.pkke_parking.fragments.DaftarSiswaFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DetailSiswaActivity extends AppCompatActivity {

    private TextView nama, nis;
    private ImageView gambar;
    private RecyclerView.LayoutManager mlayoutManager;
    private DatabaseReference databaseReference;
    String Database_Path = "siswa";
    Bundle bundle;
    public static Fragment fragment = null;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_siswa);

        bundle = getIntent().getExtras();
        nama = findViewById(R.id.get_nama);
        nis = findViewById(R.id.get_nis);
        gambar = findViewById(R.id.gambar);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("siswa");
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        user = firebaseUser.getUid();

        String image_url = getIntent().getStringExtra("img");
        String nama_val = getIntent().getStringExtra("nama");
        String nis_val = getIntent().getStringExtra("nis");

        nama.setText(nama_val);
        nis.setText(nis_val);
        Glide.with(getApplicationContext()).load(image_url).into(gambar);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(nama_val);

        collapsingToolbarLayout.setCollapsedTitleTextColor(ContextCompat.getColor(this, R.color.white));
        collapsingToolbarLayout.setExpandedTitleColor(ContextCompat.getColor(this, R.color.white));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        bundle = getIntent().getExtras();
        if (id == R.id.edit) {
            openDialog();
            Toast.makeText(getApplicationContext(), "Edit clicked", Toast.LENGTH_LONG).show();
            return true;
        } else if (id == R.id.hapus) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(DetailSiswaActivity.this);
            dialog.setTitle("Hapus Data Siswa");
            dialog.setMessage("Apakah Anda yakin ingin menghapus data siswa");
            dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    deleteSiswa(bundle.getString("id"));
                    firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(DetailSiswaActivity.this,"Delete Akun Berhasil",Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(DetailSiswaActivity.this,DaftarSiswaFragment.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(DetailSiswaActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            });
            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                    dialogInterface.dismiss();
                }
            });
            AlertDialog alertDialog = dialog.create();
            alertDialog.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void deleteSiswa(String siswaId) {
        DatabaseReference siswa = FirebaseDatabase.getInstance().getReference().child("siswa").child(siswaId);
        firebaseUser.delete();
        siswa.removeValue();
        Toast.makeText(this,"Data Berhasil dihapus",Toast.LENGTH_LONG).show();
    }
    public void openDialog(){
        DialogTambahData dialogTambahData = new DialogTambahData();
        if (dialogTambahData.getDialog() != null && dialogTambahData.getDialog().getWindow() !=null){
            dialogTambahData.getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialogTambahData.getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        dialogTambahData.show(getSupportFragmentManager(),"Dialog Update Data");
    }
}