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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SignUpActivity extends AppCompatActivity {

    private EditText etSignupName, etSignupEmail, etSignupNumber, etSignupPassword;
    private Button btSignup;
    TextView tvEmailExists;

    List<String> listOfUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etSignupName = findViewById(R.id.et_signup_name);
        etSignupEmail = findViewById(R.id.et_signup_email);
        etSignupNumber = findViewById(R.id.et_signup_number);
        etSignupPassword = findViewById(R.id.et_signup_password);
        tvEmailExists = findViewById(R.id.tv_signup_email_exists);

        btSignup = findViewById(R.id.bt_sign_up);

        listOfUsers = new ArrayList<>();

        btSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup();
            }
        });
    }

    private void signup() {
        if (verifyInputs()){
            if (isNetworkAvailable()){
                final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this);
                progressDialog.setMessage("Signing you up...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
                progressDialog.show();
                final DatabaseReference reference = FirebaseDatabase.getInstance()
                        .getReference("suppliers")
                        .child(generateHash(etSignupEmail.getText().toString()));
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.child("email").exists()){
                            User user = new User(
                                    etSignupName.getText().toString(),
                                    etSignupEmail.getText().toString(),
                                    etSignupNumber.getText().toString(),
                                    etSignupPassword.getText().toString(),
                                    "No",
                                    "No",
                                    "Supplier"
                            );
                            reference.setValue(user);
                            progressDialog.dismiss();
                            SharedPrefManager.getInstance(SignUpActivity.this)
                                    .LoginUser(etSignupName.getText().toString(),
                                            etSignupEmail.getText().toString(),
                                            etSignupNumber.getText().toString(),
                                            "No");
                            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        } else {
                            progressDialog.dismiss();
                            tvEmailExists.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            } else {

            }
        }
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
        String name = etSignupName.getText().toString();
        String email = etSignupEmail.getText().toString();
        String number = etSignupNumber.getText().toString();
        String password = etSignupPassword.getText().toString();

        if (name.isEmpty()){
            etSignupName.setError("Reuiqred");
            etSignupName.requestFocus();
            return false;
        }

        if (email.isEmpty()){
            etSignupEmail.setError("Required");
            etSignupEmail.requestFocus();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etSignupEmail.setError("Enter a valid email");
            etSignupEmail.requestFocus();
            return false;
        }

        if (number.isEmpty()){
            etSignupNumber.setError("Required");
            etSignupNumber.requestFocus();
            return false;
        }

        if (number.length() != 10){
            etSignupNumber.setError("Enter a valid number");
            etSignupNumber.requestFocus();
            return false;
        }

        if (password.isEmpty()){
            etSignupPassword.setError("Required");
            etSignupPassword.requestFocus();
            return false;
        }

        if (password.length() < 7){
            etSignupPassword.setError("Minimum 7 characters required");
            etSignupPassword.requestFocus();
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
