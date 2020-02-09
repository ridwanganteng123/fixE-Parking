package com.opatan.e_parking_petugas.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.opatan.e_parking_petugas.R;
import com.opatan.e_parking_petugas.activities.CustomViewFinderScanner;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class DashboardFragment extends Fragment {
    public final static int QRcodeWidth = 500 ;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String uid,nis,EditTextCode;
    DatabaseReference databaseReference;
    Bitmap bitmap;
    private RelativeLayout frameLayout;
    ImageView img_brcd;
    private static final int ZXING_CAMERA_PERMISSION = 1;
    private Class<?> mClss;

    public DashboardFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btnScanner = view.findViewById(R.id.btn_scan);
        btnScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCustomViewFinderScannerActivity();
            }
        });
    }
    public void launchCustomViewFinderScannerActivity() {
        launchActivity(CustomViewFinderScanner.class);
    }
    public void launchActivity(Class<?> clss) {
        if (ContextCompat.checkSelfPermission(getContext().getApplicationContext(),Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            mClss = clss;
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CAMERA}, ZXING_CAMERA_PERMISSION);
        } else {
            Intent intent = new Intent(getContext().getApplicationContext(), clss);
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ZXING_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(mClss != null) {
                        Intent intent = new Intent(getContext().getApplicationContext(), mClss);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(getContext().getApplicationContext(), "Please grant camera permission to use the QR Scanner", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }
}
