package com.opatan.e_parking_admin.activities;

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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.opatan.e_parking_admin.R;
import com.opatan.e_parking_admin.dialogs.DialogTambahDataSiswa;
import com.opatan.e_parking_admin.dialogs.DialogUpdateDataSiswa;
import com.opatan.e_parking_admin.fragments.DaftarSiswaFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DetailSiswaActivity extends AppCompatActivity {

    private TextView nama_txt, nis_txt;
    private ImageView gambar;
    private RecyclerView.LayoutManager mlayoutManager;
    private DatabaseReference databaseReference;
    String Database_Path = "siswa";
    Bundle bundle;
    public static Fragment fragment = null;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    private String user, name, nis, tgl_lahir, email, no_pol, no_sim, pwd, Imageurl, siswaId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_siswa);

        bundle = getIntent().getExtras();
        nama_txt = findViewById(R.id.get_nama);
        nis_txt = findViewById(R.id.get_nis);
        gambar = findViewById(R.id.gambar);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Siswa");
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        user = firebaseUser.getUid();

        siswaId = getIntent().getStringExtra("id");
        String image_url = getIntent().getStringExtra("img");
        String nama_val = getIntent().getStringExtra("nama");
        String nis_val = getIntent().getStringExtra("nis");

        Toast.makeText(DetailSiswaActivity.this,"Detail Siswa", Toast.LENGTH_LONG).show();

        nama_txt.setText(nama_val);
        nis_txt.setText(nis_val);
        Glide.with(getApplicationContext()).load(image_url).into(gambar);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
                        String nis_val = getIntent().getStringExtra("nis");
                        String nama_val = getIntent().getStringExtra("nama");
                        String tgl_lahir = getIntent().getStringExtra("tgl_lahir");
                        String no_pol = getIntent().getStringExtra("no_pol");
                        String no_sim = getIntent().getStringExtra("no_sim");
                        String email = getIntent().getStringExtra("email");
                        String pwd = getIntent().getStringExtra("pwd");
                        String img = getIntent().getStringExtra("img");
                        openDialog(siswaId,nis_val,nama_val,tgl_lahir,no_pol,no_sim,email,pwd,img);
                        Toast.makeText(DetailSiswaActivity.this,"Edit Data", Toast.LENGTH_LONG).show();
           return true;
        } else if (id == R.id.hapus) {
            deleteSiswa(bundle.getString("id"));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void deleteSiswa(String siswaId) {
        DatabaseReference siswa = FirebaseDatabase.getInstance().getReference().child("Siswa").child(siswaId);
        siswa.removeValue();
//        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
        Toast.makeText(this,"Data Berhasil dihapus",Toast.LENGTH_LONG).show();
    }
    public void openDialog(String siswaId,String nama, String tgl_lahir,String no_pol,String pwd,
                           String email,String no_sim,String nis,String ImageUrl){
        DialogUpdateDataSiswa dialogTambahDataSiswa = new DialogUpdateDataSiswa(siswaId,nama,tgl_lahir,no_pol,pwd,email,no_sim,nis,ImageUrl,"update");
        if (dialogTambahDataSiswa.getDialog() != null && dialogTambahDataSiswa.getDialog().getWindow() !=null){
            dialogTambahDataSiswa.getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialogTambahDataSiswa.getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        Bundle bundle = new Bundle();
        bundle.putString("action","update");
        dialogTambahDataSiswa.setArguments(bundle);
        dialogTambahDataSiswa.show(getSupportFragmentManager(),"Dialog Update Data");
    }
}