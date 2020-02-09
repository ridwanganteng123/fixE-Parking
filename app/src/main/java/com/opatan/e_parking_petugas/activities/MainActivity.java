package com.opatan.e_parking_petugas.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.opatan.e_parking_petugas.R;
import com.opatan.e_parking_petugas.fragments.DashboardFragment;
import com.opatan.e_parking_petugas.fragments.HistoryFragment;
import com.opatan.e_parking_petugas.dialogs.DialogSyaratDanKetentuan;
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
    public static Fragment fragment;
    private TextView appBarTitleTV;
    private ImageButton ketentuan;
    private Button settings, logout;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull android.view.MenuItem item) {
            appBarTitleTV = (TextView)findViewById(R.id.appBarTitleTV);
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_dashboard:
                    fragment = new DashboardFragment();
                    appBarTitleTV.setText("Dashboard");
                    break;
                case R.id.navigation_history:
                    fragment = new HistoryFragment();
                    appBarTitleTV.setText("History");
                    break;
            }
            return loadFragment(fragment);
        }
    };

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final BottomNavigationView navView = findViewById(R.id.navView);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        ketentuan = findViewById(R.id.ketentuan);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        sNavigationDrawer = findViewById(R.id.navigationDrawer);
        List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuItem("Dashboard", R.drawable.news_bg));
        menuItems.add(new MenuItem("Profile", R.drawable.feed_bg));
        menuItems.add(new MenuItem("Settings", R.drawable.message_bg));
        menuItems.add(new MenuItem("About App", R.drawable.message_bg));
        menuItems.add(new MenuItem("Logout", R.drawable.message_bg));
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
                        openDialogKetentuan();
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
