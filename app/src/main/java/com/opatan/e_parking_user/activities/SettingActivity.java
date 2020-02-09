package com.opatan.e_parking_user.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.opatan.e_parking_user.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingActivity extends AppCompatActivity {
    DatabaseReference databaseReference;
    FirebaseUser currentUser;
    String uid, username_txt, profil_txt, nis_txt;
    TextView username, nis;
    ImageView profil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        LinearLayout onboarding = findViewById(R.id.onoboarding);
        LinearLayout login = findViewById(R.id.login);
        LinearLayout detailSiswa = findViewById(R.id.detail_siswa);
        LinearLayout logout = findViewById(R.id.signout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        profil = findViewById(R.id.foto);
        username = findViewById(R.id.username);
        nis = findViewById(R.id.nis);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

        onboarding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), OnBoardingActivity.class));
            }
        });

        detailSiswa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), DetailSiswaActivity.class));
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
                                Snackbar snackbar = Snackbar.make(findViewById(R.id.frameLayout), "Yes diklik", Snackbar.LENGTH_SHORT );
                                snackbar.show();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                Toast.makeText(SettingActivity.this, "No ditekan", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                builder.setMessage("Yakin Ingin Keluar?")
                        .setPositiveButton("Ya", dialogClickListener)
                        .setNegativeButton("Tidak", dialogClickListener)
                        .show();
            }
        });

        toolbar.setTitle("Settings");
        toolbar.setTitleTextColor(Color.parseColor("#000000"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        uid = currentUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot keyId : dataSnapshot.getChildren()){
                    username_txt = keyId.child(uid).child("nama").getValue(String.class);
                    profil_txt = keyId.child(uid).child("imageUrl").getValue(String.class);
                    nis_txt = keyId.child(uid).child("nis").getValue(String.class);
                }
                username.setText(username_txt);
                nis.setText(nis_txt);
                Glide.with(SettingActivity.this).load(profil_txt).into(profil);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}

