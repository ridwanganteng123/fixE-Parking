package com.opatan.e_parking_admin.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.opatan.e_parking_admin.R;

public class ForgotActivoty extends AppCompatActivity {
//    Toolbar toolbar;
//    ProgressBar progressBar;
    EditText Email;
    Button btnReset;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);

//        toolbar = findViewById(R.id.toolbar);
//        progressBar = findViewById(R.id.progressBar);
        Email = findViewById(R.id.Email);
        btnReset = findViewById(R.id.submit);


//        toolbar.setTitle("Forgot Password");
        firebaseAuth = FirebaseAuth.getInstance();

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                progressBar.setVisibility(View.VISIBLE);
                firebaseAuth.sendPasswordResetEmail(Email.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(ForgotActivoty.this,"Password send to your email",Toast.LENGTH_LONG).show();
                                }
                                else{
                                    Toast.makeText(ForgotActivoty.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });
    }
}
