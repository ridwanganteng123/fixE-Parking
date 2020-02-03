package com.example.pkke_parking.dialogs;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.content.ContentResolver;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.example.pkke_parking.R;
import com.example.pkke_parking.activities.LoginActivity;
import com.example.pkke_parking.activities.MainActivity;
import com.example.pkke_parking.activities.RegisterActivity;
import com.example.pkke_parking.datas.model.DataDaftarSiswa;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import dmax.dialog.SpotsDialog;

import static android.app.Activity.RESULT_OK;
import static androidx.constraintlayout.widget.Constraints.TAG;

public class DialogTambahData extends DialogFragment {
    private EditText nis_txt;
    private EditText namalengkap_txt;
    private EditText tgl_lahir_txt;
    private EditText nopol_txt;
    private EditText nosim_txt;
    private EditText pwd_txt;
    private EditText email_txt;
    private EditText level_txt;
    private ImageView tampil_img;
    private Button btnUpload;
    private Button btnSubmit;
    private Button batal;
    public String name, nis, tgl_lahir, email, no_pol, no_sim, pwd, level;
    private Calendar c;

    static int PReqCode = 1 ;
    static int REQUESCODE = 1 ;

    private FirebaseAuth mAuth;

    public ProgressDialog progressDialog;

    String Storage_Path = "profil_siswa/";

    String Database_Path = "siswa";

    Uri FilePathUri;

    StorageReference storageReference;
    DatabaseReference databaseReference;

    int Image_Request_Code = 7;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.format_dialog_tambah_data_siswa, container, false);
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path);
        storageReference = FirebaseStorage.getInstance().getReference();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawableResource(R.drawable.rounded_dialog);
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.format_dialog_tambah_data_siswa, null);

        tampil_img = view.findViewById(R.id.tampil_gambar);
        btnUpload = view.findViewById(R.id.btn_upload);
        email_txt = view.findViewById(R.id.email_txt);
        nopol_txt = view.findViewById(R.id.nopol_txt);
        nosim_txt = view.findViewById(R.id.nosim_txt);
        namalengkap_txt = view.findViewById(R.id.namalengkap_txt);
        tgl_lahir_txt = view.findViewById(R.id.tgl_lahir_txt);
        nis_txt = view.findViewById(R.id.nis_txt);
        pwd_txt = view.findViewById(R.id.pwd_txt);
        level_txt = view.findViewById(R.id.level_txt);
        btnSubmit = view.findViewById(R.id.btn_tambah);
        batal = view.findViewById(R.id.batal);

        mAuth = FirebaseAuth.getInstance();


        tgl_lahir_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == tgl_lahir_txt) {

                    c = Calendar.getInstance();
                    int mYear = c.get(Calendar.YEAR);
                    int mMonth = c.get(Calendar.MONTH);
                    int mDay = c.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                            new DatePickerDialog.OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {
                                    tgl_lahir_txt.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                }
                            }, mYear, mMonth, mDay);
                    datePickerDialog.show();
                }
            }
        });

        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            CreateUserAccount();
            }
        });




        btnUpload.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                openGallery();

            }


        });


        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {
        }
    }

    private void CreateUserAccount() {

        name = namalengkap_txt.getText().toString().trim();
        nis = nis_txt.getText().toString().trim();
        tgl_lahir = tgl_lahir_txt.getText().toString().trim();
        email = email_txt.getText().toString().trim();
        no_pol = namalengkap_txt.getText().toString().trim();
        no_sim = namalengkap_txt.getText().toString().trim();
        pwd = pwd_txt.getText().toString().trim();
        level = level_txt.getText().toString().trim();
            mAuth.createUserWithEmailAndPassword(email,pwd)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        String imageURL = FilePathUri.toString();
                        String id = databaseReference.child(Database_Path).push().getKey();
                        DataDaftarSiswa dataDaftarSiswa = new DataDaftarSiswa(id, name, tgl_lahir,no_pol,pwd,email, no_sim, nis, level, imageURL);
                        FirebaseDatabase.getInstance().getReference(Database_Path).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(dataDaftarSiswa).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getContext().getApplicationContext(), "Success tambah", Toast.LENGTH_LONG).show();
                                } else {

                                }
                            }
                        });

                        updateUserInfo( name , FilePathUri,mAuth.getCurrentUser());
                    }else{
                        showMessage("gagal" + task.getException().getMessage());
                    }
                }
            });



    }



    private void updateUserInfo(final String name, Uri FilePathUri, final FirebaseUser currentUser) {

        // first we need to upload user photo to firebase storage and get url

        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child(Storage_Path);
        final StorageReference imageFilePath = mStorage.child(FilePathUri.getLastPathSegment());
        imageFilePath.putFile(FilePathUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {



                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {


                        UserProfileChangeRequest profleUpdate = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .setPhotoUri(uri)
                                .build();


                        currentUser.updateProfile(profleUpdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {
                                            showMessage("Register Complete");
                                            updateUI();
                                        }

                                    }
                                });
                    }
                });
            }
        });
    }


    private void updateUI() {

        Intent homeActivity = new Intent(getActivity(),MainActivity.class);
        startActivity(homeActivity);

    }
    private void showMessage(String message) {
        Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();
    }

    private void openGallery() {
        //TODO: open gallery intent and wait for user to pick an image !

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,REQUESCODE);
    }
    private void checkAndRequestForPermission() {


        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) getActivity().getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Toast.makeText(getActivity().getApplicationContext(),"Please accept for required permission",Toast.LENGTH_SHORT).show();

            }

            else
            {
                ActivityCompat.requestPermissions((Activity) getActivity().getApplicationContext(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);
            }

        }
        else
            openGallery();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null ) {

           FilePathUri = data.getData() ;
            tampil_img.setImageURI(FilePathUri);


        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

}