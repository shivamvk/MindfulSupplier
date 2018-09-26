package com.example.shivamvk.mindfulsupplier;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.shivamvk.mindfulsupplier.OnBoardingActivity;
import com.example.shivamvk.mindfulsupplier.SharedPrefManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountFragment extends Fragment {

    private TextView tvAccountUserName, tvAccountUserEmail, tvAccountUserNumber, tvAccountEmailNotVerified, tvAccountNumberNotVerified, tvAccountEmailVerified;
    private ImageView ivAccountEditNumber, ivAccountEmailNotVerified, ivAccountNumberNotVerfied, ivAccountEmailVerified, ivAccountNumberVerified;
    private CardView cvLogout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account, null, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvAccountUserName = view.findViewById(R.id.tv_account_user_name);
        tvAccountUserEmail = view.findViewById(R.id.tv_account_user_email);
        tvAccountUserNumber = view.findViewById(R.id.tv_account_user_number);
        tvAccountEmailVerified = view.findViewById(R.id.tv_account_email_verified);
        tvAccountEmailNotVerified = view.findViewById(R.id.tv_account_email_not_verified);
        tvAccountNumberNotVerified = view.findViewById(R.id.tv_account_number_not_verified);

        cvLogout = view.findViewById(R.id.cv_logout);

        ivAccountEmailNotVerified = view.findViewById(R.id.iv_account_email_not_verified);
        ivAccountEmailVerified = view.findViewById(R.id.iv_account_email_verified);

        ivAccountEditNumber = view.findViewById(R.id.iv_account_edit_user_number);

        tvAccountUserName.setText(SharedPrefManager.getInstance(getContext()).getName());
        tvAccountUserEmail.setText(SharedPrefManager.getInstance(getContext()).getEmail());
        tvAccountUserNumber.setText("+91" + SharedPrefManager.getInstance(getContext()).getNumber());

        if (SharedPrefManager.getInstance(getContext()).isEmailVerified().equals("Yes")){
            tvAccountEmailNotVerified.setVisibility(View.GONE);
            ivAccountEmailNotVerified.setVisibility(View.GONE);
            tvAccountEmailVerified.setVisibility(View.VISIBLE);
            ivAccountEmailVerified.setVisibility(View.VISIBLE);
        }

        tvAccountEmailNotVerified.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("We will send a verification otp to " +
                        SharedPrefManager.getInstance(getContext()).getEmail() +
                        ". Please make sure you have this email account handy right now."
                );
                builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final ProgressDialog progressDialog = new ProgressDialog(getContext());
                        progressDialog.setMessage("Please wait...");
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        String url = "http://13.250.198.160:8080/FacebookAndroid/sendotp.jsp?email=" + SharedPrefManager.getInstance(getContext()).getEmail();
                        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                                url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        response = response.trim();
                                        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                                        View view1 = LayoutInflater.from(getContext()).inflate(R.layout.dialog_email_verification, null, false);
                                        builder1.setView(view1);
                                        final TextView tvDialogEmailInvalidOTP = view1.findViewById(R.id.tv_dialog_email_verification_invaild_otp);
                                        TextView tvDialogEmailVerification = view1.findViewById(R.id.tv_dialog_email_verification);
                                        tvDialogEmailVerification.setText("A verification otp is sent to " + SharedPrefManager.getInstance(getContext()).getEmail() + ".");
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
                                                                .child(generateHash(SharedPrefManager.getInstance(getContext()).getEmail()))
                                                                .child("emailverified");
                                                        reference.setValue("Yes");
                                                        SharedPrefManager.getInstance(getContext()).LoginUser(
                                                                SharedPrefManager.getInstance(getContext()).getName(),
                                                                SharedPrefManager.getInstance(getContext()).getEmail(),
                                                                SharedPrefManager.getInstance(getContext()).getNumber(),
                                                                "Yes"
                                                        );
                                                        Fragment fragment=new AccountFragment();
                                                        FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                                                        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                                                        fragmentTransaction.replace(R.id.fl_main_activity,fragment);
                                                        fragmentTransaction.addToBackStack(null);
                                                        fragmentTransaction.commit();
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
                                        Toast.makeText(getContext(), "Please check your internet connection and try again!", Toast.LENGTH_SHORT).show();

                                    }
                                });
                        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
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
                        = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                return activeNetworkInfo != null;
            }
        });

        ivAccountEditNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_edit_details, null, false);
                builder.setView(view1);
                final AlertDialog dialog = builder.show();
                final TextView tvDialogHeadings = view1.findViewById(R.id.tv_dialog_headings);
                final EditText etDialogEditName = view1.findViewById(R.id.et_dialog_edit_name);
                final EditText etDialogEditNumber = view1.findViewById(R.id.et_dialog_edit_number);
                final Button btDialogEditSave = view1.findViewById(R.id.bt_dialog_edit_save);
                etDialogEditName.setText(SharedPrefManager.getInstance(getActivity()).getName());
                etDialogEditNumber.setText(SharedPrefManager.getInstance(getActivity()).getNumber());
                btDialogEditSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (verifyInputs(etDialogEditNumber.getText().toString())){
                            if (isNetworkAvailable()){
                                final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                                progressDialog.setMessage("Saving changes...");
                                progressDialog.setCanceledOnTouchOutside(false);
                                progressDialog.setCancelable(false);
                                progressDialog.show();
                                DatabaseReference reference = FirebaseDatabase.getInstance()
                                        .getReference("suppliers")
                                        .child(generateHash(SharedPrefManager.getInstance(getActivity()).getEmail()));
                                reference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        dataSnapshot.child("number").getRef().setValue(etDialogEditNumber.getText().toString());
                                        dataSnapshot.child("name").getRef().setValue(etDialogEditName.getText().toString());
                                        if (SharedPrefManager.getInstance(getContext()).getNumber().equals(etDialogEditNumber.getText().toString()) && SharedPrefManager.getInstance(getContext()).isNumberVerified().equals("Yes")){
                                            SharedPrefManager.getInstance(getContext()).LoginUser(
                                                    etDialogEditName.getText().toString(),
                                                    SharedPrefManager.getInstance(getContext()).getEmail(),
                                                    etDialogEditNumber.getText().toString(),
                                                    SharedPrefManager.getInstance(getContext()).isEmailVerified()
                                            );
                                        } else {
                                            dataSnapshot.child("numberverified").getRef().setValue("No");
                                            SharedPrefManager.getInstance(getContext()).LoginUser(
                                                    etDialogEditName.getText().toString(),
                                                    SharedPrefManager.getInstance(getContext()).getEmail(),
                                                    etDialogEditNumber.getText().toString(),
                                                    SharedPrefManager.getInstance(getContext()).isEmailVerified()
                                            );
                                        }
                                        dialog.dismiss();
                                        progressDialog.dismiss();
                                        Fragment fragment=new AccountFragment();
                                        FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                                        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                                        fragmentTransaction.replace(R.id.fl_main_activity,fragment);
                                        fragmentTransaction.addToBackStack(null);
                                        fragmentTransaction.commit();
                                        //Toast.makeText(getActivity(), "Changes saved successfully!", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
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
                                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
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
                SharedPrefManager.getInstance(getContext()).Logout();
                Intent intent = new Intent(getContext(), OnBoardingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

    }
}
