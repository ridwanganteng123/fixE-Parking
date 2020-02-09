package com.opatan.e_parking_petugas.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.opatan.e_parking_petugas.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {
    FirebaseUser currentUser;
    String uid,nama,nis, no_pol,no_sim,imageUrl;
    TextView name,nopol_txt,nosim_txt, nis_txt;
    ImageView img_profile;
    RelativeLayout content_progress, content_profile;

    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ImageButton button  = findViewById(R.id.back_profile);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        uid = currentUser.getUid();

        name = findViewById(R.id.nama);
        nopol_txt = findViewById(R.id.nopol_txt);
        nosim_txt = findViewById(R.id.nosim_txt);
        img_profile = findViewById(R.id.img_profile);
        nis_txt = findViewById(R.id.nis_txt);
        content_profile = findViewById(R.id.content_profil);
        content_progress = findViewById(R.id.content_progressbar);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                content_profile.setVisibility(View.VISIBLE);
                content_progress.setVisibility(View.GONE);
                for (DataSnapshot keyId : dataSnapshot.getChildren()){
                    nis = keyId.child(uid).child("nis").getValue(String.class);
                    nama = keyId.child(uid).child("nama").getValue(String.class);
                    no_pol = keyId.child(uid).child("no_pol").getValue(String.class);
                    no_sim = keyId.child(uid).child("no_sim").getValue(String.class);
                    imageUrl = keyId.child(uid).child("imageUrl").getValue(String.class);
                }
                name.setText(nama);
                nis_txt.setText(nis);

                nopol_txt.setText(no_pol);
                nosim_txt.setText(no_sim);
                Glide.with(ProfileActivity.this).load(imageUrl).into(img_profile);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}