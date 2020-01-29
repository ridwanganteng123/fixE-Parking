package com.example.pkke_parking.activities;

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

import com.example.pkke_parking.R;
import com.example.pkke_parking.fragments.DaftarSiswaFragment;
import com.example.pkke_parking.fragments.DashboardFragment;
import com.example.pkke_parking.fragments.HistoryFragment;
import com.example.pkke_parking.animates.Helper;
import com.example.pkke_parking.dialogs.DialogAboutApp;
import com.example.pkke_parking.dialogs.DialogSyaratDanKetentuan;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
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
    private ImageButton optionMenu, ketentuan;
    private GoogleApiClient mGoogleApiClient;
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
                case R.id.navigation_daftar_siswa:
                    fragment = new DaftarSiswaFragment();
                    appBarTitleTV.setText("Daftar Siswa");
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
        optionMenu = (ImageButton)findViewById(R.id.ketentuan);
        logout = (Button) findViewById(R.id.iconLogOut);
        settings = (Button) findViewById(R.id.iconSetting);

        optionMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogKetentuan();
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            }
        });
        logout.setTranslationY(-30);

        // Option menu

//        optionMenu.setOnClickListener(new View.OnClickListener() {
//            @SuppressLint("RtlHardcoded")
//            @TargetApi(Build.VERSION_CODES.M)
//            @RequiresApi(api = Build.VERSION_CODES.M)
//            @Override
//            public void onClick(View v) {
//                final PopupMenu popupMenu = new PopupMenu(MainActivity.this, optionMenu);
//                popupMenu.getMenuInflater().inflate(R.menu.sidebar_menu, popupMenu.getMenu());
//                popupMenu.setGravity(Gravity.getAbsoluteGravity(10,1));
//                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(android.view.MenuItem item) {
//                        Snackbar.make(findViewById(R.id.rootLayout), item.getTitle() + " Ditekan", Snackbar.LENGTH_SHORT).show();
//                        return true;
//                    }
//                });
//
//                popupMenu.show();
//
//            }
//        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        sNavigationDrawer = findViewById(R.id.navigationDrawer);
        List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuItem("Dashboard", R.drawable.news_bg));
        menuItems.add(new MenuItem("Profile", R.drawable.feed_bg));
        menuItems.add(new MenuItem("Settings", R.drawable.message_bg));
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
                    .replace(R.id.frameLayout, fragment)
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

                }

                sNavigationDrawer.setDrawerListener(new SNavigationDrawer.DrawerListener() {

                    @Override
                    public void onDrawerOpened() {

                    }

                    @Override
                    public void onDrawerOpening() {

                        Helper.slideDown(navView);
                        settings = (Button) findViewById(R.id.iconSetting);
                        logout = (Button) findViewById(R.id.iconLogOut);
                        settings.setVisibility(View.VISIBLE);
                        logout.setVisibility(View.VISIBLE);
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
                                    replace(R.id.frameLayout, fragment)
                                    .commit();
                        }
                    }

                    @Override
                    public void onDrawerClosed() {
                        settings = (Button) findViewById(R.id.iconSetting);
                        logout = (Button) findViewById(R.id.iconLogOut);
                        settings.setVisibility(View.GONE);
                        logout.setVisibility(View.GONE);
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
                    .replace(R.id.frameLayout, fragment)
                    .commit();
            return true;
        }
        return false;
    }

}
