package com.example.pkke_parking.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pkke_parking.R;
import com.google.android.material.appbar.CollapsingToolbarLayout;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_siswa);

        bundle = getIntent().getExtras();
        nama = findViewById(R.id.get_nama);
        nis = findViewById(R.id.get_nis);
        gambar = findViewById(R.id.gambar);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("siswa");

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
            Toast.makeText(getApplicationContext(), "Edit clicked", Toast.LENGTH_LONG).show();
            return true;
        } else if (id == R.id.hapus) {
            Intent intent = new Intent(this, MainActivity.class);
            deleteSiswa(bundle.getString("id"));
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteSiswa(String siswaId) {
        DatabaseReference siswa = FirebaseDatabase.getInstance().getReference().child("siswa").child(siswaId);
        siswa.removeValue();
        Toast.makeText(this,"Data Berhasil dihapus",Toast.LENGTH_LONG).show();
   }
}