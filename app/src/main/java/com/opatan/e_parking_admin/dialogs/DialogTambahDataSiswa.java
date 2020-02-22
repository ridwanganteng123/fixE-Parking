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

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.opatan.e_parking_admin.R;
import com.opatan.e_parking_admin.activities.LoginActivity;
import com.opatan.e_parking_admin.datas.model.DataDaftarSiswa;
import com.google.android.gms.tasks.OnCompleteListener;
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

import java.util.Calendar;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class DialogTambahDataSiswa extends DialogFragment {
    private EditText nis_txt, namalengkap_txt, tgl_lahir_txt, nopol_txt, nosim_txt, pwd_txt, email_txt;
    private ImageView tampil_img;
    private Button btnUpload, btnSubmit, batal;
    public String siswaId, name, nis, tgl_lahir, email, no_pol, no_sim, pwd, level, Imageurl;
    private Calendar c;

    static int PReqCode = 1;
    static int REQUESCODE = 1;

    private FirebaseAuth mAuth;

    public ProgressDialog progressDialog;

    String Storage_Path = "profile_siswa/";

    String Database_Path = "Siswa";
    FirebaseUser currentUser;

    Uri FilePathUri;
    Bundle bundle;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    int Image_Request_Code = 7;

    public DialogTambahDataSiswa() {
    }

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
        bundle = getArguments();
        final String action = bundle.getString("action");
        databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path);
        storageReference = FirebaseStorage.getInstance().getReference();

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

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
        if (action.equals("update")) {
            nis_txt.setText(bundle.getString("nis"));
            namalengkap_txt.setText(bundle.getString("nama"));
            tgl_lahir_txt.setText(bundle.getString("tgl_lahir"));
            nopol_txt.setText(bundle.getString("no_pol"));
            nosim_txt.setText(bundle.getString("no_sim"));
            pwd_txt.setText(bundle.getString("pwd"));
            email_txt.setText(bundle.getString("email"));
            level.equals("Siswa");
            String img_url = bundle.getString("imageURL");
            Glide.with(getContext()).load(img_url).into(tampil_img);
            btnUpload.setText(bundle.getString("imageURL"));
            btnSubmit.setText("update");
            getDialog().setTitle("Update Siswa");
        }
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (action.equals("insert")) {
                    CreateUserAccount(mAuth.getCurrentUser());
                    openDialog();
                } else if (action.equals("update")) {
                    String siswaId = bundle.getString("id");
                    String name = namalengkap_txt.getText().toString().trim();
                    String nis = nis_txt.getText().toString().trim();
                    String tgl_lahir = tgl_lahir_txt.getText().toString().trim();
                    String email = email_txt.getText().toString().trim();
                    String no_pol = nopol_txt.getText().toString().trim();
                    String no_sim = nosim_txt.getText().toString().trim();
                    String pwd = pwd_txt.getText().toString().trim();
                    String level = "Siswa";
                    updateSiswa(siswaId, name, nis, tgl_lahir, email, no_pol, no_sim, pwd, level);
                }

            }
        });
        btnUpload.setOnClickListener(new View.OnClickListener() {
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

    private void openDialog()
    {
        ProgressDialog progressDialog = new ProgressDialog(getContext().getApplicationContext());
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
    }

    private void CreateUserAccount(final FirebaseUser currentUser) {
        name = namalengkap_txt.getText().toString().trim();
        nis = nis_txt.getText().toString().trim();
        tgl_lahir = tgl_lahir_txt.getText().toString().trim();
        email = email_txt.getText().toString().trim();
        no_pol = nopol_txt.getText().toString().trim();
        no_sim = nosim_txt.getText().toString().trim();
        pwd = pwd_txt.getText().toString().trim();
        level = "Siswa";
        if (FilePathUri != null) {
            final StorageReference mStorage = FirebaseStorage.getInstance().getReference().child(Storage_Path);
            final StorageReference imageFilePath = mStorage.child(FilePathUri.getLastPathSegment());
            mStorage.getPath().equals(imageFilePath.getPath());

            imageFilePath.putFile(FilePathUri).addOnSuccessListener(
                    new OnSuccessListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(final Uri uri) {
                                    final String imageURL = uri.toString();
                                    mAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {

                                                String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                                DataDaftarSiswa dataDaftarSiswa = new DataDaftarSiswa(id, name, tgl_lahir, no_pol, pwd, email, no_sim, nis, level, imageURL);
                                                FirebaseDatabase.getInstance().getReference(Database_Path).child(id).setValue(dataDaftarSiswa).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(getContext().getApplicationContext(), "Data Berhasil Ditambah", Toast.LENGTH_LONG).show();
                                                            dismiss();
                                                        } else if (TextUtils.isEmpty(name)) {
                                                            Toast.makeText(getContext().getApplicationContext(), "Isi Seluruh Field", Toast.LENGTH_LONG).show();
                                                        } else if (task.isCanceled()) {

                                                        }
                                                    }
                                                });

                                            } else {
                                                Toast.makeText(getContext().getApplicationContext(), "Data Gagal Ditambahkan", Toast.LENGTH_LONG).show();
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
                            Toast.makeText(getContext().getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                        }
                    });
        }

    }


    private boolean updateSiswa(String siswaId, final String name, String nis, String
            tgl_lahir, String email, String no_pol, String no_sim, String pwd, String level) {

        String imageURL = FilePathUri.toString();
        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child(Storage_Path);
        final StorageReference imageFilePath = mStorage.child(FilePathUri.getLastPathSegment());
        imageFilePath.putFile(FilePathUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

//                        UserProfileChangeRequest profleUpdate = new UserProfileChangeRequest.Builder()
//                                .setDisplayName(name)
//                                .setPhotoUri(uri)
//                                .build();
//
//                        currentUser.updateProfile(profleUpdate)
//                                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        if (task.isSuccessful()) {
//                                            dismiss();
//                                        }
//                                    }
//                                });
                    }
                });
            }
        });

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Siswa").child(siswaId);
        DataDaftarSiswa dataDaftarSiswa = new DataDaftarSiswa(siswaId, name, nis, tgl_lahir, email, no_pol, no_sim, pwd, level, imageURL);

        databaseReference.setValue(dataDaftarSiswa);

        Toast.makeText(getContext(), "Data Berhasil diedit!", Toast.LENGTH_SHORT).show();

        return true;
    }


    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUESCODE);
    }

    private void checkAndRequestForPermission() {

        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) getActivity().getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Toast.makeText(getActivity().getApplicationContext(), "Please accept for required permission", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions((Activity) getActivity().getApplicationContext(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);
            }
        } else
            openGallery();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null) {
            FilePathUri = data.getData();
            tampil_img.setImageURI(FilePathUri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

}