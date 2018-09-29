package com.example.shivamvk.mindfulsupplier;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.content.Intent;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UploadPOD extends AppCompatActivity {

    ImageView ivPODFront,ivPODBack,ivPODOtherDocs;
    RelativeLayout rlFront,rlback,rlOtherDocs;
    TextView tvFront,tvBack,tvOtherDocs;
    Button btPOD;
    private static final int PHOTO_PICKER_POD_FRONT = 1;
    private static final int PHOTO_PICKER_POD_BACK = 2;
    private static final int PHOTO_PICKER_POD_OTHERDOCS = 3;

    Uri stPODF, stPODB,stPODOD;

    StorageReference storageReference;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_pod);

       // setTitle("Proof of deliviery");

        rlFront = findViewById(R.id.rl_upload_pod_front);
        rlback =findViewById(R.id.rl_upload_pod_back);
        rlOtherDocs = findViewById(R.id.rl_upload_pod_other_docs);

        ivPODFront = findViewById(R.id.iv_upload_pod_front);
        ivPODBack = findViewById(R.id.iv_upload_pod_back);
        ivPODOtherDocs = findViewById(R.id.iv_upload_pod_other_docs);

        tvFront = findViewById(R.id.tv_upload_pod_front);
        tvBack = findViewById(R.id.tv_upload_pod_back);
        tvOtherDocs = findViewById(R.id.tv_upload_pod_other_docs);

        btPOD = findViewById(R.id.bt_submit_pod);


       /* ivPODOtherDocs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(UploadPOD.this, "helloo", Toast.LENGTH_SHORT).show();
            }
        });*/

       databaseReference = FirebaseDatabase.getInstance().getReference()
               .child("suppliers")
               .child(SharedPrefManager.getInstance(this).getNumber())
               .child("podimages")
               .child("order id: "+getIntent().getStringExtra("orderid"));

        storageReference = FirebaseStorage.getInstance().getReference()
                .child("suppliers")
                .child(SharedPrefManager.getInstance(this).getNumber())
                .child("POD IMAGES")
                .child("order id: "+getIntent().getStringExtra("orderid"));

        Log.i("TAG", "onCreate: " +getIntent().getStringExtra("orderid"));

        rlFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), PHOTO_PICKER_POD_FRONT);
            }
        });

        rlback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), PHOTO_PICKER_POD_BACK);
            }
        });

        rlOtherDocs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), PHOTO_PICKER_POD_OTHERDOCS);
            }
        });

        btPOD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog progressDialog = new ProgressDialog(UploadPOD.this);
                if (stPODF == null && stPODOD ==null && stPODB == null){
                    Toast.makeText(UploadPOD.this, "Please select an image before uploading!", Toast.LENGTH_SHORT).show();
                } else {

                    progressDialog.setMessage("Please wait...");
                    progressDialog.setCancelable(false);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    if (stPODF != null){


                        final StorageReference fReference = storageReference.child(stPODF.getLastPathSegment());


                        /*fReference.putFile(stPODF)
                                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                                        if (task.isSuccessful()){
                                            progressDialog.dismiss();
                                            Toast.makeText(UploadPOD.this, "Uploaded successfully", Toast.LENGTH_SHORT).show();
                                          //  onBackPressed();
                                        } else {
                                            Toast.makeText(UploadPOD.this, "error", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });*/

                        fReference.putFile(stPODF).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if(!task.isSuccessful()){
                                    throw task.getException();
                                }
                                return fReference.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if(task.isSuccessful()){
                                    progressDialog.dismiss();
                                    Uri downloadUri = task.getResult();
                                    String photoUrl = downloadUri.toString();
                                    databaseReference.child("podfront").setValue(photoUrl);
                                    Toast.makeText(UploadPOD.this, "Uploaded successfully", Toast.LENGTH_SHORT).show();
                                    Log.i("TAG", "onComplete: photo  "+ photoUrl);
                                } else {
                                    Toast.makeText(UploadPOD.this, "error", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                    if (stPODB != null){
                        final StorageReference bReference = storageReference.child(stPODB.getLastPathSegment());
                        bReference.putFile(stPODB).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if(!task.isSuccessful()){
                                    throw task.getException();
                                }
                                return bReference.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if(task.isSuccessful()){
                                    progressDialog.dismiss();
                                    Uri downloadUri = task.getResult();
                                    String photoUrl = downloadUri.toString();
                                    databaseReference.child("podback").setValue(photoUrl);
                                    Toast.makeText(UploadPOD.this, "Uploaded successfully", Toast.LENGTH_SHORT).show();
                                    Log.i("TAG", "onComplete: photo  "+ photoUrl);
                                } else {
                                    Toast.makeText(UploadPOD.this, "error", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                    if(stPODOD !=null){
                        final StorageReference odReference = storageReference.child(stPODOD.getLastPathSegment());
                        odReference.putFile(stPODOD).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if(!task.isSuccessful()){
                                    throw task.getException();
                                }
                                return odReference.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if(task.isSuccessful()){
                                    progressDialog.dismiss();
                                    Uri downloadUri = task.getResult();
                                    String photoUrl = downloadUri.toString();
                                    databaseReference.child("podotherdocs").setValue(photoUrl);
                                    Toast.makeText(UploadPOD.this, "Uploaded successfully", Toast.LENGTH_SHORT).show();
                                    Log.i("TAG", "onComplete: photo  "+ photoUrl);
                                } else {
                                    Toast.makeText(UploadPOD.this, "error", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PHOTO_PICKER_POD_FRONT){
            if (resultCode == RESULT_OK){
                Uri selectedImageURI = data.getData();
                Log.i("TAG", "onActivityResult: " +data.getData());
                ivPODFront.setVisibility(View.VISIBLE);
                tvFront.setVisibility(View.GONE);
                ivPODFront.setImageURI(selectedImageURI);
                stPODF = selectedImageURI;
            }
        }
        if (requestCode == PHOTO_PICKER_POD_BACK){
            if (resultCode == RESULT_OK){
                Uri selectedImageURI = data.getData();
                ivPODBack.setVisibility(View.VISIBLE);
                tvBack.setVisibility(View.GONE);
                ivPODBack.setImageURI(selectedImageURI);
                stPODB = selectedImageURI;
            }
        }
        if (requestCode == PHOTO_PICKER_POD_OTHERDOCS){
            if (resultCode == RESULT_OK){
                Uri selectedImageURI = data.getData();
                ivPODOtherDocs.setVisibility(View.VISIBLE);
                tvOtherDocs.setVisibility(View.GONE);
                ivPODOtherDocs.setImageURI(selectedImageURI);
                stPODOD = selectedImageURI;
            }
        }
    }
}
