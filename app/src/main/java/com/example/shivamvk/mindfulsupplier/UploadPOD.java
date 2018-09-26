package com.example.shivamvk.mindfulsupplier;

import android.app.ProgressDialog;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.content.Intent;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UploadPOD extends AppCompatActivity {

    ImageView ivPODF,ivPODB;
    Button btPOD;
    private static final int PHOTO_PICKER_POD_FRONT = 123;
    private static final int PHOTO_PICKER_POD_BACK = 456;
    Uri stPODF, stPODB;

    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_pod);

        setTitle("Proof of deliviery");

        ivPODF = findViewById(R.id.iv_upload_pod_f);
        ivPODB = findViewById(R.id.iv_upload_pod_b);
        btPOD = findViewById(R.id.bt_upload_pod);

        storageReference = FirebaseStorage.getInstance().getReference()
                .child(getIntent().getStringExtra("orderid"));

        ivPODF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), PHOTO_PICKER_POD_FRONT);
            }
        });

        ivPODB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), PHOTO_PICKER_POD_BACK);
            }
        });

        btPOD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stPODF == null){
                    Toast.makeText(UploadPOD.this, "Please select an image before uploading!", Toast.LENGTH_SHORT).show();
                } else {
                    final ProgressDialog progressDialog = new ProgressDialog(UploadPOD.this);
                    progressDialog.setMessage("Please wait...");
                    progressDialog.setCancelable(false);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    StorageReference fReference = storageReference.child("PODF")
                            .child(stPODF.getLastPathSegment());

                    fReference.putFile(stPODF)
                            .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(UploadPOD.this, "Uploaded successfully", Toast.LENGTH_SHORT).show();
                                        onBackPressed();
                                    } else {
                                        Toast.makeText(UploadPOD.this, "error", Toast.LENGTH_SHORT).show();
                                    }
                                    progressDialog.dismiss();
                                }
                            });

                    if (stPODB != null){
                        StorageReference bReference = storageReference.child("PODB")
                                .child(stPODB.getLastPathSegment());
                        bReference.putFile(stPODB);
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private String generateHash(String s) {
        int hash = 21;
        for (int i = 0; i < s.length(); i++) {
            hash = hash*31 + s.charAt(i);
        }
        if (hash < 0){
            hash = hash * -1;
        }
        return hash + "";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PHOTO_PICKER_POD_FRONT){
            if (resultCode == RESULT_OK){
                Uri selectedImageURI = data.getData();
                ivPODF.setImageURI(selectedImageURI);
                stPODF = selectedImageURI;
            }
        }
        if (requestCode == PHOTO_PICKER_POD_BACK){
            if (resultCode == RESULT_OK){
                Uri selectedImageURI = data.getData();
                ivPODB.setImageURI(selectedImageURI);
                stPODB = selectedImageURI;
            }
        }
    }
}
