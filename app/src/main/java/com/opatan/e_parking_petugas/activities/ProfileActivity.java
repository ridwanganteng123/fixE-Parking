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
    String uid,nama,nis, email,imageUrl;
    TextView name,email_txt, nis_txt;
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

        name = findViewById(R.id.nama_txt);
        img_profile = findViewById(R.id.img_profile);
        nis_txt = findViewById(R.id.nis_txt);
        email_txt = findViewById(R.id.email_txt);
        content_profile = findViewById(R.id.content_profil);
        content_progress = findViewById(R.id.content_progressbar);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference("Petugas");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println("UID : " + uid);
                content_profile.setVisibility(View.VISIBLE);
                content_progress.setVisibility(View.GONE);
                    nis = dataSnapshot.child(uid).child("nis").getValue(String.class);
                    System.out.println("NIS : " + nis);
                    email = dataSnapshot.child(uid).child("email").getValue(String.class);
                    System.out.println("NIS : " + email);
                    nama = dataSnapshot.child(uid).child("nama").getValue(String.class);
                    System.out.println("NIS : " + nama);
                    imageUrl = dataSnapshot.child(uid).child("imageURL").getValue(String.class);
                    name.setText(nama);
                    nis_txt.setText(nis);
                    email_txt.setText(email);
                    Glide.with(ProfileActivity.this).load(imageUrl).into(img_profile);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}