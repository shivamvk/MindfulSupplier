package com.example.shivamvk.mindfulsupplier;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountActivity extends AppCompatActivity {

    private TextView tvAccountUserName, tvAccountUserEmail, tvAccountUserNumber, tvAccountEmailNotVerified, tvAccountNumberNotVerified, tvAccountEmailVerified;
    private ImageView ivAccountEditNumber, ivAccountEmailNotVerified, ivAccountNumberNotVerfied, ivAccountEmailVerified, ivAccountNumberVerified;
    private CardView cvLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);


        tvAccountUserName = findViewById(R.id.tv_account_user_name);
        tvAccountUserEmail = findViewById(R.id.tv_account_user_email);
        tvAccountUserNumber = findViewById(R.id.tv_account_user_number);
        tvAccountEmailVerified = findViewById(R.id.tv_account_email_verified);
        tvAccountEmailNotVerified = findViewById(R.id.tv_account_email_not_verified);
        tvAccountNumberNotVerified = findViewById(R.id.tv_account_number_not_verified);

        cvLogout = findViewById(R.id.cv_logout);

        ivAccountEmailNotVerified = findViewById(R.id.iv_account_email_not_verified);
        ivAccountEmailVerified = findViewById(R.id.iv_account_email_verified);

        ivAccountEditNumber = findViewById(R.id.iv_account_edit_user_number);

        tvAccountUserName.setText(SharedPrefManager.getInstance(this).getName());
        tvAccountUserEmail.setText(SharedPrefManager.getInstance(this).getEmail());
        tvAccountUserNumber.setText("+91" + SharedPrefManager.getInstance(this).getNumber());

        if (SharedPrefManager.getInstance(this).isEmailVerified().equals("Yes")){
            tvAccountEmailNotVerified.setVisibility(View.GONE);
            ivAccountEmailNotVerified.setVisibility(View.GONE);
            tvAccountEmailVerified.setVisibility(View.VISIBLE);
            ivAccountEmailVerified.setVisibility(View.VISIBLE);
        }

        tvAccountEmailNotVerified.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(AccountActivity.this);
                builder.setMessage("We will send a verification otp to " +
                        SharedPrefManager.getInstance(AccountActivity.this).getEmail() +
                        ". Please make sure you have this email account handy right now."
                );
                builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final ProgressDialog progressDialog = new ProgressDialog(AccountActivity.this);
                        progressDialog.setMessage("Please wait...");
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        String url = "http://13.250.198.160:8080/FacebookAndroid/sendotp.jsp?email=" + SharedPrefManager.getInstance(AccountActivity.this).getEmail();
                        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                                url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        response = response.trim();
                                        AlertDialog.Builder builder1 = new AlertDialog.Builder(AccountActivity.this);
                                        View view1 = LayoutInflater.from(AccountActivity.this).inflate(R.layout.dialog_email_verification, null, false);
                                        builder1.setView(view1);
                                        final TextView tvDialogEmailInvalidOTP = view1.findViewById(R.id.tv_dialog_email_verification_invaild_otp);
                                        TextView tvDialogEmailVerification = view1.findViewById(R.id.tv_dialog_email_verification);
                                        tvDialogEmailVerification.setText("A verification otp is sent to " + SharedPrefManager.getInstance(AccountActivity.this).getEmail() + ".");
                                        progressDialog.dismiss();
                                        final AlertDialog dialog1 = builder1.show();
                                        EditText etDialogEmailVerification = view1.findViewById(R.id.et_dialog_email_otp);
                                        final String finalResponse = response;
                                        etDialogEmailVerification.addTextChangedListener(new TextWatcher() {
                                            @Override
                                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                            }

                                            @Override
                                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                            }

                                            @Override
                                            public void afterTextChanged(Editable editable) {
                                                if (editable.length() == 4){
                                                    if (editable.toString().equals(finalResponse)){
                                                        dialog1.dismiss();
                                                        DatabaseReference reference = FirebaseDatabase.getInstance()
                                                                .getReference("suppliers")
                                                                .child(generateHash(SharedPrefManager.getInstance(AccountActivity.this).getEmail()))
                                                                .child("emailverified");
                                                        reference.setValue("Yes");
                                                        SharedPrefManager.getInstance(AccountActivity.this).LoginUser(
                                                                SharedPrefManager.getInstance(AccountActivity.this).getName(),
                                                                SharedPrefManager.getInstance(AccountActivity.this).getEmail(),
                                                                SharedPrefManager.getInstance(AccountActivity.this).getNumber(),
                                                                "Yes"
                                                        );
                                                        recreate();
                                                    } else {
                                                        tvDialogEmailInvalidOTP.setVisibility(View.VISIBLE);
                                                    }
                                                }
                                            }
                                        });
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        progressDialog.dismiss();
                                        Toast.makeText(AccountActivity.this, "Please check your internet connection and try again!", Toast.LENGTH_SHORT).show();

                                    }
                                });
                        RequestQueue requestQueue = Volley.newRequestQueue(AccountActivity.this);
                        requestQueue.add(stringRequest);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //do nothing
                    }
                });
                builder.show();
            }
        });

        ivAccountEditNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(AccountActivity.this);
                View view1 = LayoutInflater.from(AccountActivity.this).inflate(R.layout.dialog_edit_details, null, false);
                builder.setView(view1);
                final AlertDialog dialog = builder.show();
                final TextView tvDialogHeadings = view1.findViewById(R.id.tv_dialog_headings);
                final EditText etDialogEditName = view1.findViewById(R.id.et_dialog_edit_name);
                final EditText etDialogEditNumber = view1.findViewById(R.id.et_dialog_edit_number);
                final Button btDialogEditSave = view1.findViewById(R.id.bt_dialog_edit_save);
                etDialogEditName.setText(SharedPrefManager.getInstance(AccountActivity.this).getName());
                etDialogEditNumber.setText(SharedPrefManager.getInstance(AccountActivity.this).getNumber());
                btDialogEditSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (verifyInputs(etDialogEditNumber.getText().toString())){
                            if (isNetworkAvailable()){
                                final ProgressDialog progressDialog = new ProgressDialog(AccountActivity.this);
                                progressDialog.setMessage("Saving changes...");
                                progressDialog.setCanceledOnTouchOutside(false);
                                progressDialog.setCancelable(false);
                                progressDialog.show();
                                DatabaseReference reference = FirebaseDatabase.getInstance()
                                        .getReference("suppliers")
                                        .child(generateHash(SharedPrefManager.getInstance(AccountActivity.this).getEmail()));
                                reference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        dataSnapshot.child("number").getRef().setValue(etDialogEditNumber.getText().toString());
                                        dataSnapshot.child("name").getRef().setValue(etDialogEditName.getText().toString());
                                        if (SharedPrefManager.getInstance(AccountActivity.this).getNumber().equals(etDialogEditNumber.getText().toString()) && SharedPrefManager.getInstance(AccountActivity.this).isNumberVerified().equals("Yes")){
                                            SharedPrefManager.getInstance(AccountActivity.this).LoginUser(
                                                    etDialogEditName.getText().toString(),
                                                    SharedPrefManager.getInstance(AccountActivity.this).getEmail(),
                                                    etDialogEditNumber.getText().toString(),
                                                    SharedPrefManager.getInstance(AccountActivity.this).isEmailVerified()
                                            );
                                        } else {
                                            dataSnapshot.child("numberverified").getRef().setValue("No");
                                            SharedPrefManager.getInstance(AccountActivity.this).LoginUser(
                                                    etDialogEditName.getText().toString(),
                                                    SharedPrefManager.getInstance(AccountActivity.this).getEmail(),
                                                    etDialogEditNumber.getText().toString(),
                                                    SharedPrefManager.getInstance(AccountActivity.this).isEmailVerified()
                                            );
                                        }
                                        dialog.dismiss();
                                        progressDialog.dismiss();
                                        Toast.makeText(AccountActivity.this, "Changes saved successfully!", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                        }
                    }

                    private boolean isNetworkAvailable() {
                        ConnectivityManager connectivityManager
                                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                        return activeNetworkInfo != null;
                    }


                    private boolean verifyInputs(String s1) {
                        if (s1.isEmpty()){
                            etDialogEditNumber.setError("Required");
                            etDialogEditNumber.requestFocus();
                            return false;
                        }

                        if (s1.length() != 10){
                            etDialogEditNumber.setError("Enter a valid number");
                            etDialogEditNumber.requestFocus();
                            return false;
                        }

                        return true;
                    }
                });
            }
        });

        cvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPrefManager.getInstance(AccountActivity.this).Logout();
                Intent intent = new Intent(AccountActivity.this, OnBoardingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
