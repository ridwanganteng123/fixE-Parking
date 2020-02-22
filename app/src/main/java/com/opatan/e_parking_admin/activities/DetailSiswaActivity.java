package com.opatan.e_parking_admin.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.opatan.e_parking_admin.R;
import com.opatan.e_parking_admin.dialogs.DialogUpdateDataSiswa;
import com.opatan.e_parking_admin.fragments.ActivityFragment;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.opatan.e_parking_admin.fragments.DashboardFragment;

public class DetailSiswaActivity extends AppCompatActivity {

    private ImageView gambar;
    Bundle bundle;
    public static Fragment fragment = null;
    TabLayout tabLayout;
    private String siswaId;
    private String nama_val, nis_val,email_val,no_pol_val,no_sim_val, tgl_lahir_val, image_url, level_val;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_siswa);

        bundle = getIntent().getExtras();
        gambar = findViewById(R.id.gambar);
        Toolbar toolbar = findViewById(R.id.toolbar);
        tabLayout = findViewById(R.id.tab_layout);

        siswaId = getIntent().getStringExtra("id");
        image_url = getIntent().getStringExtra("img");
        nama_val = getIntent().getStringExtra("nama");
        nis_val = getIntent().getStringExtra("nis");
        email_val = getIntent().getStringExtra("email");
        no_pol_val = getIntent().getStringExtra("no_pol");
        no_sim_val = getIntent().getStringExtra("no_sim");
        tgl_lahir_val = getIntent().getStringExtra("tgl_lahir");
        level_val = getIntent().getStringExtra("level");

        if (no_pol_val == null && no_sim_val == null)
        {
            no_sim_val = "-";
            no_pol_val = "-";
        }

        Glide.with(getApplicationContext()).load(image_url).into(gambar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(nama_val);
        collapsingToolbarLayout.setCollapsedTitleTextColor(ContextCompat.getColor(this, R.color.white));
        collapsingToolbarLayout.setExpandedTitleColor(ContextCompat.getColor(this, R.color.white));

        loadFragment(new BiodataDetailFragment(nama_val, nis_val, email_val, no_pol_val, no_sim_val, tgl_lahir_val, level_val));

        tabLayout.addTab(tabLayout.newTab().setText("Biodata"));
        tabLayout.addTab(tabLayout.newTab().setText("Statistik"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tabLayout.getSelectedTabPosition() == 0){
                    loadFragment(new BiodataDetailFragment(nama_val, nis_val, email_val, no_pol_val, no_sim_val, tgl_lahir_val, level_val));
                }else if(tabLayout.getSelectedTabPosition() == 1){
                    loadFragment(new StatistikDetailFragment());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                    .replace(R.id.frameLayout, fragment)
                    .commit();
            return true;
        }
        return false;
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
    public void openDialog(String siswaId, String nis, String nama, String tgl_lahir,String no_pol,String pwd,
                           String email,String no_sim,String ImageUrl){
//        DialogUpdateDataSiswa dialogUpdateDataSiswa = new DialogUpdateDataSiswa(siswaId,nis,nama,tgl_lahir,no_pol,pwd,email,no_sim,ImageUrl);
        DialogUpdateDataSiswa dialogUpdateDataSiswa = new DialogUpdateDataSiswa(siswaId,nama,tgl_lahir,no_pol,pwd,email,no_sim,nis,ImageUrl);

        if (dialogUpdateDataSiswa.getDialog() != null && dialogUpdateDataSiswa.getDialog().getWindow() !=null){
            dialogUpdateDataSiswa.getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialogUpdateDataSiswa.getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        Bundle bundle = new Bundle();
        bundle.putString("action","update");
        dialogUpdateDataSiswa.setArguments(bundle);
        dialogUpdateDataSiswa.show(getSupportFragmentManager(),"Dialog Update Data");
    }

    class MyAdapter extends FragmentPagerAdapter {
        Context context;
        int totalTabs;

        public MyAdapter(Context c, FragmentManager fm, int totalTabs) {
            super(fm);
            context = c;
            this.totalTabs = totalTabs;
        }
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    DashboardFragment dashboardFragment = new DashboardFragment();
                    return dashboardFragment;
                case 1:
                    ActivityFragment activityFragment = new ActivityFragment();
                    return activityFragment;
                default:
                    return null;
            }
        }
        @Override
        public int getCount() {
            return totalTabs;
        }
    }

}