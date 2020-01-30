package com.example.pkke_parking.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pkke_parking.R;
import com.example.pkke_parking.datas.model.DataDaftarSiswa;
import com.example.pkke_parking.fragments.DashboardFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private EditText emailTV, passwordTV, nisTv;
    private Button loginBtn;


    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();

        initializeUI();
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginUserAccount();
            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

    }

    private void loginUserAccount() {


        String email, password, nis;
        email = emailTV.getText().toString();
        password = passwordTV.getText().toString();


        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Please enter nis...", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Please enter password!", Toast.LENGTH_LONG).show();
            return;
        }
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Login successful!", Toast.LENGTH_LONG).show();


                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Login failed! Please try again later", Toast.LENGTH_LONG).show();

                        }
                    }
                });

        //DatabaseReference databaseReference =  FirebaseDatabase.getInstance().getReference("siswa").child(nis).child(firebaseAuth.getUid());
//        databaseReference.addValueEventListener(new ValueEventListener(){
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                DataDaftarSiswa dataDaftarSiswa = dataSnapshot.getValue(DataDaftarSiswa.class);
//                int userType = (userProfile.getUsertype());
//
//                switch (userType) {
//                    case 0:
//                        startActivity(new Intent(Login.this, DoctorActivity.class));
//                        break;
//                    case 1:
//                        startActivity(new Intent(Login.this, MainActivity.class));
//                        break;
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

    }
    private void updateUI() {

//        startActivity(DashboardFragment);
        finish();

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user != null) {
            //user is already connected  so we need to redirect him to home page
            updateUI();

        }
    }

    private void initializeUI() {
        emailTV = findViewById(R.id.email);
        passwordTV = findViewById(R.id.password);
        loginBtn = findViewById(R.id.login);
    }
}