package com.example.pkke_parking.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pkke_parking.R;
import com.google.android.material.appbar.CollapsingToolbarLayout;

public class DetailSiswaActivity extends AppCompatActivity {

    private TextView nama, nis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_siswa);

        nama = findViewById(R.id.get_nama);
        nis = findViewById(R.id.get_nis);

        String nama_val = getIntent().getStringExtra("nama");
        String nis_val = getIntent().getStringExtra("nis");

        nama.setText(nama_val);
        nis.setText(nis_val);

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
        if (id == R.id.tambah) {
            Toast.makeText(getApplicationContext(), "Tambah clicked", Toast.LENGTH_LONG).show();
            return true;
        }

        if (id == R.id.edit) {
            Toast.makeText(getApplicationContext(), "Edit clicked", Toast.LENGTH_LONG).show();
            return true;
        }

        if (id == R.id.hapus) {
            Toast.makeText(getApplicationContext(), "Hapus clicked", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
