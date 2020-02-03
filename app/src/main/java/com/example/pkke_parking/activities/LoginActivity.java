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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText emailTV, passwordTV, nisTv;
    private Button loginBtn;
    private Intent DashboardFragment;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();
        DashboardFragment = new Intent(this,com.example.pkke_parking.activities.MainActivity.class);

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
                            loginBtn.setVisibility(View.VISIBLE);
                            updateUI();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                               startActivity(intent);
                        }
                        else {
                            showMessage(task.getException().getMessage());
                            loginBtn.setVisibility(View.VISIBLE);
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
    public void  updateUI(){


        startActivity(DashboardFragment);
            finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user != null) {
            updateUI();

        }
    }
    private void showMessage(String text) {

        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_LONG).show();
    }

    private void initializeUI() {
        emailTV = findViewById(R.id.email);
        passwordTV = findViewById(R.id.password);
        loginBtn = findViewById(R.id.login);

    }
}