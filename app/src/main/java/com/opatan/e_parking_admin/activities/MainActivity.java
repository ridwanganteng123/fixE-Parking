package com.opatan.e_parking_admin.activities;

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
import com.opatan.e_parking_admin.R;
import com.opatan.e_parking_admin.datas.model.PrefManager;
import com.opatan.e_parking_admin.fragments.ActivityFragment;
import com.opatan.e_parking_admin.fragments.DaftarPetugasFragment;
import com.opatan.e_parking_admin.fragments.DaftarSiswaFragment;
import com.opatan.e_parking_admin.fragments.DashboardFragment;
import com.opatan.e_parking_admin.animates.Helper;
import com.opatan.e_parking_admin.dialogs.DialogAboutApp;
import com.opatan.e_parking_admin.dialogs.DialogSyaratDanKetentuan;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.shrikanthravi.e_parking2.data.MenuItem;
import com.shrikanthravi.e_parking2.widget.SNavigationDrawer;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    SNavigationDrawer sNavigationDrawer;
    int color1 = 0;
    Class fragmentClass;
    private PrefManager prefs;
    public static Fragment fragment;
    private TextView appBarTitleTV;
    private ImageButton optionMenu, ketentuan;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull android.view.MenuItem item) {
            appBarTitleTV = findViewById(R.id.appBarTitleTV);
            Fragment fragment = null;



            switch (item.getItemId()) {
                case R.id.navigation_dashboard:
                    fragment = new DashboardFragment();
                    appBarTitleTV.setText("Dashboard");
                    break;
                case R.id.navigation_daftar_siswa:
                    fragment = new DaftarSiswaFragment();
                    appBarTitleTV.setText("Daftar Siswa");
                    break;
                case R.id.navigation_aktivitas:
                    fragment = new ActivityFragment();
                    appBarTitleTV.setText("Aktivitas");
                    break;
                case R.id.navigation_daftar_petugas:
                    fragment = new DaftarPetugasFragment();
                    appBarTitleTV.setText("Daftar Petugas");
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
        fragmentClass = DashboardFragment.class;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                    .replace(R.id.frameLayoutMain, fragment)
                    .commit();
        }

        sNavigationDrawer.setOnMenuItemClickListener(new SNavigationDrawer.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClicked(int position) {
                System.out.println("Position " + position);

                switch (position) {
                    case 0: {
                        color1 = R.color.Black;
                        fragmentClass = DashboardFragment.class;
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
                    }

                    @Override
                    public void onDrawerClosing() {
                        System.out.println("Drawer closed");
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
}