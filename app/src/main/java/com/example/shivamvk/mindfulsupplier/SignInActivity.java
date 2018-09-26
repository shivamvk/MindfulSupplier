package com.example.shivamvk.mindfulsupplier;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignInActivity extends AppCompatActivity {

    private EditText etLoginEmail, etLoginPassword;
    private Button btLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        etLoginEmail = findViewById(R.id.et_login_email);
        etLoginPassword = findViewById(R.id.et_login_password);

        btLogin = findViewById(R.id.bt_sign_in);

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verifyInputs()){
                    if (isNetworkAvailable()){
                        final ProgressDialog progressDialog = new ProgressDialog(SignInActivity.this);
                        progressDialog.setMessage("Logging you in...");
                        progressDialog.setCancelable(false);
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.show();
                        DatabaseReference reference = FirebaseDatabase.getInstance()
                                .getReference("suppliers")
                                .child(generateHash(etLoginEmail.getText().toString()));
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (!dataSnapshot.exists()){
                                    progressDialog.dismiss();
                                    Toast.makeText(SignInActivity.this, "Email doesn't exist!", Toast.LENGTH_SHORT).show();
                                } else {
                                    String pass = dataSnapshot.child("password").getValue().toString();
                                    if (pass.equals(etLoginPassword.getText().toString())){
                                        String name = dataSnapshot.child("name").getValue().toString();
                                        String email = dataSnapshot.child("email").getValue().toString();
                                        String number = dataSnapshot.child("number").getValue().toString();
                                        String emailverified = dataSnapshot.child("emailverified").getValue().toString();
                                        SharedPrefManager.getInstance(SignInActivity.this).LoginUser(
                                                name,
                                                email,
                                                number,
                                                emailverified
                                        );
                                        progressDialog.dismiss();
                                        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(SignInActivity.this, "Invalid email or password!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }
        });

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

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    private boolean verifyInputs() {
        String email = etLoginEmail.getText().toString();
        String password = etLoginPassword.getText().toString();

        if (email.isEmpty()){
            etLoginEmail.setError("Required");
            etLoginEmail.requestFocus();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etLoginEmail.setError("Enter a valid email");
            etLoginEmail.requestFocus();
            return false;
        }

        if (password.isEmpty()){
            etLoginPassword.setError("Required");
            etLoginPassword.requestFocus();
            return false;
        }

        return true;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
    }
}
