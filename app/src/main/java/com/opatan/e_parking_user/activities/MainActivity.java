package com.opatan.e_parking_user.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.opatan.e_parking_user.R;
import com.opatan.e_parking_user.fragments.DashboardFragment;
import com.opatan.e_parking_user.fragments.HistoryFragment;
import com.opatan.e_parking_user.animates.Helper;
import com.opatan.e_parking_user.dialogs.DialogAboutApp;
import com.opatan.e_parking_user.dialogs.DialogSyaratDanKetentuan;
import com.opatan.e_parking_user.fragments.ParkiredFragment;
import com.opatan.e_parking_user.fragments.StatistikFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.shrikanthravi.e_parking2.data.MenuItem;
import com.shrikanthravi.e_parking2.widget.SNavigationDrawer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    SNavigationDrawer sNavigationDrawer;
    int color1 = 0;
    Class fragmentClass;
    public static Fragment fragment;
    private TextView appBarTitleTV;
    private FirebaseUser currentUser;
    private String uid;
    private ImageButton optionMenu, ketentuan;
    private Button settings, logout;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull android.view.MenuItem item) {
            appBarTitleTV = findViewById(R.id.appBarTitleTV);
            fragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_dashboard:
                    changeFragment();
                    break;
                case R.id.navigation_history:
                    fragment = new HistoryFragment();
                    appBarTitleTV.setText("History");
                    break;
                case R.id.navigation_analitik:
                    fragment = new StatistikFragment();
                    appBarTitleTV.setText("Statistik");
                    break;
            }
            return loadFragment(fragment);
        }
    };

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appBarTitleTV = findViewById(R.id.appBarTitleTV);
        final BottomNavigationView navView = findViewById(R.id.navView);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        optionMenu = findViewById(R.id.ketentuan);

        optionMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogKetentuan();
            }
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        sNavigationDrawer = findViewById(R.id.navigationDrawer);
        List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuItem("Dashboard", R.drawable.news_bg));
        menuItems.add(new MenuItem("Profile", R.drawable.feed_bg));
        menuItems.add(new MenuItem("Settings", R.drawable.message_bg));
        menuItems.add(new MenuItem("About App", R.drawable.message_bg));
        menuItems.add(new MenuItem("Log Out", R.drawable.message_bg));
        sNavigationDrawer.setMenuItemList(menuItems);

        changeFragment();

        sNavigationDrawer.setOnMenuItemClickListener(new SNavigationDrawer.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClicked(int position) {
                System.out.println("Position " + position);
                switch (position) {
                    case 0: {
                        color1 = R.color.Black;
                        changeFragment();
                        break;
                    }
                    case 1:{
                        color1 = R.color.Black;
                        startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                        break;
                    }
                    case 2: {
                        color1 = R.color.Black;
                        startActivity(new Intent(MainActivity.this, SettingActivity.class));
                        break;
                    }
                    case 3: {
                        color1 = R.color.Black;
                        openDialog();
                        break;
                    }
                    case 4: {
                        color1 = R.color.Black;
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case DialogInterface.BUTTON_POSITIVE:
                                        FirebaseAuth.getInstance().signOut();
                                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        Toast.makeText(MainActivity.this, "Logout Berhasil", Toast.LENGTH_SHORT).show();
                                        break;
                                    case DialogInterface.BUTTON_NEGATIVE:
                                        Toast.makeText(MainActivity.this, "Logout Gagal", Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            }
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("Yakin Ingin Keluar?")
                                .setPositiveButton("Ya", dialogClickListener)
                                .setNegativeButton("Tidak", dialogClickListener)
                                .show();
                        break;
                    }
                }
                sNavigationDrawer.setDrawerListener(new SNavigationDrawer.DrawerListener() {
                    @Override
                    public void onDrawerOpened() {

                    }

                    @Override
                    public void onDrawerOpening() {
                        Helper.slideDown(navView);
                    }

                    @Override
                    public void onDrawerClosing() {
                        System.out.println("Drawer closed");
                        Helper.slideUp(navView);
                        try {
                            fragment = (Fragment) fragmentClass.newInstance();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (fragment != null) {
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            fragmentManager
                                    .beginTransaction()
                                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out).
                                    replace(R.id.frameLayoutMain, fragment)
                                    .commit();
                        }
                    }

                    @Override
                    public void onDrawerClosed() {
                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {
                        System.out.println("State " + newState);
                    }
                });
            }
        });
    }

    public void openDialog() {
        DialogAboutApp dialogAboutApp = new DialogAboutApp();
        if (dialogAboutApp.getDialog() != null && dialogAboutApp.getDialog().getWindow() != null) {
            dialogAboutApp.getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialogAboutApp.getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        dialogAboutApp.show(getSupportFragmentManager(), "Dialog Tambah Data");
    }

    public void openDialogKetentuan() {
        DialogSyaratDanKetentuan exampleDialog = new DialogSyaratDanKetentuan();
        if (exampleDialog.getDialog() != null && exampleDialog.getDialog().getWindow() != null) {
            exampleDialog.getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            exampleDialog.getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        exampleDialog.show(getSupportFragmentManager(), "Dialog Ketentuan");
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                    .replace(R.id.frameLayoutMain, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    private void changeFragment() {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        uid = currentUser.getUid();
        SimpleDateFormat getDate = new SimpleDateFormat("dd-MM-yyyy");
        Date date_now = new Date();
        final String date = getDate.format(date_now);
        DatabaseReference dbParkired = FirebaseDatabase.getInstance().getReference("ScanHarian").child(date).child(uid);
        dbParkired.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshotParkir) {
                DatabaseReference dbParkired = FirebaseDatabase.getInstance().getReference("Siswa").child(uid);
                dbParkired.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshotNama) {
                        if (dataSnapshotParkir.exists())
                        {
                            String nama_txt = dataSnapshotNama.child("nama").getValue(String.class);
                            String nama = nama_txt.split(" ")[0];
                            loadFragment(new ParkiredFragment(nama));
                            appBarTitleTV.setText("Dashboard");
                        } else {
                            loadFragment(new DashboardFragment());
                            appBarTitleTV.setText("Dashboard");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
