package com.opatan.e_parking_admin.dialogs;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.opatan.e_parking_admin.R;
import com.opatan.e_parking_admin.datas.model.DataDaftarPetugas;
import com.opatan.e_parking_admin.datas.model.DataDaftarSiswa;

import java.util.Calendar;

import static android.app.Activity.RESULT_OK;

public class DialogTambahDataPetugas extends DialogFragment {
    private Spinner kelas_txt, gender_txt;
    private EditText nis_txt, namalengkap_txt, tgl_lahir_txt, email_txt, pwd_txt;
    private ImageView tampil_img;
    private Button btnUpload,btnSubmit ,batal ;
    private String name, nis, tgl_lahir, email, pwd, level, kelas_val, gender_val;
    private Calendar c;
    static int PReqCode = 1 ;
    static int REQUESCODE = 1 ;
    private FirebaseAuth mAuth;
    public ProgressDialog progressDialog;
    String Storage_Path = "profile_petugas/";
    String Database_Path = "Petugas";
    Uri FilePathUri;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    int Image_Request_Code = 7;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.format_dialog_tambah_data_petugas, container, false);
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

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawableResource(R.drawable.rounded_dialog);
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        mAuth = FirebaseAuth.getInstance();

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.format_dialog_tambah_data_petugas, null);

        nis_txt = view.findViewById(R.id.nis_txt);
        tampil_img = view.findViewById(R.id.tampil_gambar);
        btnUpload = view.findViewById(R.id.btn_upload);
        email_txt = view.findViewById(R.id.email_txt);
        pwd_txt = view.findViewById(R.id.pwd_txt);
        namalengkap_txt = view.findViewById(R.id.namalengkap_txt);
        tgl_lahir_txt = view.findViewById(R.id.tgl_lahir_txt);
        btnSubmit = view.findViewById(R.id.btn_tambah);
        batal = view.findViewById(R.id.batal);
        gender_txt = view.findViewById(R.id.gender_txt);
        kelas_txt = view.findViewById(R.id.kelas_txt);

        progressDialog = new ProgressDialog(getActivity());

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
                progressDialog.setMessage("Loading ...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
                progressDialog.show();
                CreateUserAccount(mAuth.getCurrentUser());
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
    }

    private void CreateUserAccount(final FirebaseUser currentUser) {
        name = namalengkap_txt.getText().toString().trim();
        nis = nis_txt.getText().toString().trim();
        tgl_lahir = tgl_lahir_txt.getText().toString().trim();
        email = email_txt.getText().toString().trim();
        pwd = pwd_txt.getText().toString().trim();
        level = "Petugas";
        kelas_val = kelas_txt.getSelectedItem().toString();
        gender_val = gender_txt.getSelectedItem().toString();

        if (FilePathUri !=null){
            StorageReference mStorage = FirebaseStorage.getInstance().getReference().child(Storage_Path);
            final StorageReference imageFilePath = mStorage.child(FilePathUri.getLastPathSegment());
            mStorage.getPath().equals(imageFilePath.getPath());
            imageFilePath.putFile(FilePathUri).addOnSuccessListener(
                    new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(final Uri uri) {
                                  final String imageUrl = uri.toString();
                                  mAuth.createUserWithEmailAndPassword(email,pwd)
                                          .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                              @Override
                                              public void onComplete(@NonNull Task<AuthResult> task) {
                                                  if (task.isSuccessful()){
                                                      String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                                      DataDaftarPetugas dataDaftarPetugas = new DataDaftarPetugas(id, name, tgl_lahir,pwd,email, nis, level, imageUrl, kelas_val, gender_val);
                                                      FirebaseDatabase.getInstance().getReference(Database_Path).child(id).setValue(dataDaftarPetugas).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                          @Override
                                                          public void onComplete(@NonNull Task<Void> task) {
                                                              if (task.isSuccessful()) {
                                                                  progressDialog.dismiss();
                                                                  Toast.makeText(getContext().getApplicationContext(), "Data Berhasil Ditambah", Toast.LENGTH_LONG).show();
                                                                  dismiss();
                                                              } else if(TextUtils.isEmpty(name)) {
                                                                  progressDialog.dismiss();
                                                                  Toast.makeText(getContext().getApplicationContext(), "Isi Seluruh Field", Toast.LENGTH_LONG).show();
                                                              } else if(task.isCanceled()){
                                                                  progressDialog.dismiss();
                                                              }
                                                          }
                                                      });
                                                  }else{
                                                      progressDialog.dismiss();
                                                      Toast.makeText(getContext().getApplicationContext(),"Data gagal ditambahkan",Toast.LENGTH_LONG).show();
                                                  }
                                              }
                                          });
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext().getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                        }
                    });

        }

    }

    private void openGallery() {
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