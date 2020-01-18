package com.example.pkke_parking.dialogs;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
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
import androidx.fragment.app.DialogFragment;

import com.example.pkke_parking.R;
import com.example.pkke_parking.activities.MainActivity;
import com.example.pkke_parking.datas.model.DataDaftarSiswa;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import dmax.dialog.SpotsDialog;

import static android.app.Activity.RESULT_OK;

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
                addSiswa();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Masukkan Gambar"), Image_Request_Code);
            }
        });

        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {
            FilePathUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), FilePathUri);
                tampil_img.setImageBitmap(bitmap);
                btnUpload.setText("Gambar Dipilih");
                Log.d("LINKPREVIEW", String.valueOf(data.getData()));
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String GetFileExtension(Uri uri) {
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void addSiswa(){
        name = namalengkap_txt.getText().toString().trim();
        nis = nis_txt.getText().toString().trim();
        tgl_lahir = tgl_lahir_txt.getText().toString().trim();
        email = email_txt.getText().toString().trim();
        no_pol = namalengkap_txt.getText().toString().trim();
        no_sim = namalengkap_txt.getText().toString().trim();
        pwd = namalengkap_txt.getText().toString().trim();
        level = level_txt.getText().toString().trim();

        if (!TextUtils.isEmpty(name)){
            if (FilePathUri != null) {
                progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage("Loading ...");
                progressDialog.setIndeterminate(false);
                progressDialog.show();
                final StorageReference storageReference2nd = storageReference.child(Storage_Path + System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));
                storageReference2nd.putFile(FilePathUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                String imageURL = storageReference2nd.getDownloadUrl().toString();
                                Log.d("LINKIMAGE", imageURL);
                                String id = databaseReference.child(Database_Path).push().getKey();
                                DataDaftarSiswa daftarSiswa = new DataDaftarSiswa(id, name, tgl_lahir, no_pol, pwd, email, no_sim, nis, level, imageURL );
                                databaseReference.child(id).setValue(daftarSiswa);
                                progressDialog.dismiss();
                                Toast.makeText(getContext().getApplicationContext(), "Tambah Data Berhasil", Toast.LENGTH_LONG).show();
                                dismiss();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Toast.makeText(getContext().getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
            }
            else {
                Toast.makeText(getContext().getApplicationContext(), "Pilih Gambar", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getActivity(), "Masukkan Data Dengan Benar", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
}