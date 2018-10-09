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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
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

/*public class AccountFragment extends Fragment {

    private TextView tvAccountUserName, tvAccountEmailVerified, tvAccountEmailNotVerified,
           tvAccountAlternateNumberVerified,tvAccountAlternateNumberNotVerified;
    private ImageView ivAccountEditNumber, ivAccountEmailNotVerified,
            ivAccountEmailVerified,ivAccountAlternateNumberVerified,ivAccountAlternateNumberNotVerified
            ,ivUploadPanCard;
    private Button btLogout,btSaveChanges;
    private CardView cvLogout;
    private EditText etAccountUserName,etAccountUserEmail,etAccountUserNumber,etAccountUserAlternateNumber;
    private RelativeLayout rlUploadPANCard,rlUploadAddressProof,rlUploadVisitingCard;

    private static final int PHOTO_PICKER_ADHAAR = 123;
    private static final int PHOTO_PICKER_PAN_CARD = 456;
    private static final int PHOTO_PICKER_VISITING_CARD = 789;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account, null, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvAccountUserName = view.findViewById(R.id.tv_account_user_name);

        etAccountUserName = view.findViewById(R.id.et_account_user_name);

        etAccountUserEmail = view.findViewById(R.id.et_account_user_email);
        tvAccountEmailVerified = view.findViewById(R.id.tv_account_email_verified);
        tvAccountEmailNotVerified = view.findViewById(R.id.tv_account_email_not_verified);
        ivAccountEmailNotVerified = view.findViewById(R.id.iv_account_email_not_verified);
        ivAccountEmailVerified = view.findViewById(R.id.iv_account_email_verified);


        etAccountUserNumber = view.findViewById(R.id.et_account_user_number);


        etAccountUserAlternateNumber = view.findViewById(R.id.et_account_alternate_user_number);
        *//*tvAccountAlternateNumberVerified = view.findViewById(R.id.tv_account_alternate_number_verified);
        tvAccountAlternateNumberNotVerified = view.findViewById(R.id.tv_account_alternate_number_not_verified);
        ivAccountAlternateNumberVerified = view.findViewById(R.id.iv_account_alternate_number_verified);
        ivAccountAlternateNumberNotVerified = view.findViewById(R.id.iv_account_alternate_number_not_verified);
*//*
        rlUploadPANCard = view.findViewById(R.id.rl_upload_pan_card_activity_account);
        rlUploadAddressProof = view.findViewById(R.id.rl_upload_address_proof_activity_account);
        rlUploadVisitingCard = view.findViewById(R.id.rl_upload_visiting_card_activity_account);

        rlUploadPANCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),UploadActivity.class);
                intent.putExtra("documentname","PAN CARD");
                startActivity(intent);
            }
        });

        rlUploadAddressProof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),UploadActivity.class);
                intent.putExtra("documentname","AADHAR CARD");
                startActivity(intent);
            }
        });

        rlUploadVisitingCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),UploadActivity.class);
                intent.putExtra("documentname","VISITING CARD");
                startActivity(intent);
            }
        });


       *//* tvAccountUploadAadhar = view.findViewById(R.id.tv_account_upload_aadhar);

        cvUploadAadhar = view.findViewById(R.id.cv_account_upload_aadhar);
        if(!SharedPrefManager.getInstance(getContext()).getPanCardFrontUrl().equals("No")){
            tvAccountUploadAadhar.setText("Aadhar uploaded");
        }*//*

      //  cvLogout = view.findViewById(R.id.cv_logout);

        btLogout = view.findViewById(R.id.bt_logout);
        btSaveChanges = view.findViewById(R.id.bt_save_changes);



        //ivAccountEditNumber = view.findViewById(R.id.iv_account_edit_user_number);

        tvAccountUserName.setText(SharedPrefManager.getInstance(getContext()).getName());
        etAccountUserName.setText(SharedPrefManager.getInstance(getContext()).getName());
        etAccountUserEmail.setText(SharedPrefManager.getInstance(getContext()).getEmail());
        etAccountUserNumber.setText(SharedPrefManager.getInstance(getContext()).getNumber());
        etAccountUserAlternateNumber.setText(SharedPrefManager.getInstance(getContext()).getAlternateNumber());


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
                                                                SharedPrefManager.getInstance(getContext()).getAlternateNumber(),
                                                                "Yes"
                                                        );
                                                        Fragment fragment=new OldAccountFragment();
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
                        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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

       *//* ivAccountEditNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_edit_details, null, false);
                builder.setView(view1);
                final AlertDialog dialog = builder.show();
                final TextView tvDialogHeadings = view1.findViewById(R.id.tv_dialog_headings);
                final EditText etDialogEditName = view1.findViewById(R.id.et_dialog_edit_name);
                final EditText etDialogEditEmail = view1.findViewById(R.id.et_dialog_edit_email);

                final EditText etDialogCompanyName = view1.findViewById(R.id.et_dialog_edit_company_name);
                final EditText etDialogAddress = view1.findViewById(R.id.et_dialog_edit_address);

                final Button btDialogEditSave = view1.findViewById(R.id.bt_dialog_edit_save);
                etDialogEditName.setText(SharedPrefManager.getInstance(getActivity()).getName());
                etDialogEditEmail.setText(SharedPrefManager.getInstance(getContext()).getEmail());

                if(!SharedPrefManager.getInstance(getContext()).getCompanyName().equals("Not provided")){
                    etDialogCompanyName.setText(SharedPrefManager.getInstance(getContext()).getCompanyName());
                }

                if(!SharedPrefManager.getInstance(getContext()).getAddress().equals("Not provided")){
                    etDialogAddress.setText(SharedPrefManager.getInstance(getContext()).getAddress());
                }


                btDialogEditSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (verifyInputs(etDialogEditEmail.getText().toString(),"Not Provided",etDialogAddress.getText().toString())){
                            if (isNetworkAvailable()){
                                final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                                progressDialog.setMessage("Saving changes...");
                                progressDialog.setCanceledOnTouchOutside(false);
                                progressDialog.setCancelable(false);
                                progressDialog.show();
                                DatabaseReference reference = FirebaseDatabase.getInstance()
                                        .getReference("users")
                                        .child(SharedPrefManager.getInstance(getContext()).getNumber());
                                reference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        dataSnapshot.child("email").getRef().setValue(etDialogEditEmail.getText().toString());
                                        dataSnapshot.child("name").getRef().setValue(etDialogEditName.getText().toString());
                                        dataSnapshot.child("companyname").getRef().setValue("Not provided");
                                        dataSnapshot.child("address").getRef().setValue(etDialogAddress.getText().toString());

                                        if (!SharedPrefManager.getInstance(getContext()).getEmail().equals(etDialogEditEmail.getText().toString())){
                                            dataSnapshot.child("emailverified").getRef().setValue("No");
                                            SharedPrefManager.getInstance(getContext())
                                                    .LoginUser(etDialogEditName.getText().toString(),
                                                            etDialogEditEmail.getText().toString(),
                                                            SharedPrefManager.getInstance(getContext()).getNumber(),
                                                            "No",
                                                            "Yes",
                                                            "Not provided",
                                                            etDialogAddress.getText().toString()
                                                    );
                                        } else if(SharedPrefManager.getInstance(getContext()).isEmailVerified().equals("Yes")){
                                            SharedPrefManager.getInstance(getContext())
                                                    .LoginUser(etDialogEditName.getText().toString(),
                                                            etDialogEditEmail.getText().toString(),
                                                            SharedPrefManager.getInstance(getContext()).getNumber(),
                                                            "Yes",
                                                            "Yes",
                                                            "Not provided",
                                                            etDialogAddress.getText().toString()
                                                    );
                                        }

                                        dialog.dismiss();
                                        progressDialog.dismiss();
                                        Fragment fragment=new AccountFragment();
                                        FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                                        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                                        fragmentTransaction.replace(R.id.fl_home_activity,fragment);
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




                    private boolean verifyInputs(String s1,String s2,String s3) {
                        if (s1.isEmpty()){
                            etDialogEditEmail.setError("Required");
                            etDialogEditEmail.requestFocus();
                            return false;
                        }

                        if (!Patterns.EMAIL_ADDRESS.matcher(s1).matches()){
                            etDialogEditEmail.setError("Enter a valid email");
                            etDialogEditEmail.requestFocus();
                            return false;
                        }

                        if(s2.isEmpty()){
                            etDialogCompanyName.setError("Required");
                            etDialogCompanyName.requestFocus();
                            return false;
                        }
                        if(s3.isEmpty()){
                            etDialogAddress.setError("Required");
                            etDialogAddress.requestFocus();
                            return false;
                        }


                        return true;
                    }
                });

            }
        });
        *//*

        *//*cvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPrefManager.getInstance(getContext()).Logout();
                Intent intent = new Intent(getContext(), OnBoardingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
*//*

        btSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(verifyInputs()){
                    if(isNetworkAvailable()){
                        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                        progressDialog.setMessage("Saving changes...");
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        DatabaseReference reference = FirebaseDatabase.getInstance()
                                .getReference("suppliers")
                                .child(generateHash(SharedPrefManager.getInstance(getContext()).getEmail()));
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                dataSnapshot.child("email").getRef().setValue(etAccountUserEmail.getText().toString());
                                dataSnapshot.child("name").getRef().setValue(etAccountUserName.getText().toString());
                                dataSnapshot.child("number").getRef().setValue(etAccountUserNumber.getText().toString());
                                dataSnapshot.child("alternatenumber").getRef().setValue(etAccountUserAlternateNumber.getText().toString());

                                SharedPrefManager.getInstance(getContext())
                                        .LoginUser(etAccountUserName.getText().toString(),
                                                etAccountUserEmail.getText().toString(),
                                                etAccountUserNumber.getText().toString(),
                                                etAccountUserAlternateNumber.getText().toString(),
                                                "No"
                                        );


                               *//* if (!SharedPrefManager.getInstance(getContext()).getEmail().equals(etAccountUserEmail.getText().toString())){
                                    dataSnapshot.child("emailverified").getRef().setValue("No");
                                    SharedPrefManager.getInstance(getContext())
                                            .LoginUser(etAccountUserName.getText().toString(),
                                                    etAccountUserEmail.getText().toString(),
                                                    etAccountUserNumber.getText().toString(),
                                                    "No"
                                            );
                                    ivAccountEmailNotVerified.setVisibility(View.VISIBLE);
                                    tvAccountEmailNotVerified.setVisibility(View.VISIBLE);
                                    ivAccountEmailVerified.setVisibility(View.GONE);
                                    tvAccountEmailVerified.setVisibility(View.GONE);
                                } else if(SharedPrefManager.getInstance(getContext()).isEmailVerified().equals("Yes")){
                                    SharedPrefManager.getInstance(getContext())
                                            .LoginUser(etAccountUserName.getText().toString(),
                                                    etAccountUserEmail.getText().toString(),
                                                    etAccountUserNumber.getText().toString(),
                                                    "Yes"
                                            );
                                } else if(!SharedPrefManager.getInstance(getContext()).isEmailVerified().equals("Yes")){
                                    SharedPrefManager.getInstance(getContext())
                                            .LoginUser(etAccountUserName.getText().toString(),
                                                    etAccountUserEmail.getText().toString(),
                                                    etAccountUserNumber.getText().toString(),
                                                    "No"
                                            );
                                }*//*

                                tvAccountUserName.setText(etAccountUserName.getText().toString().trim());
                                progressDialog.dismiss();

                               *//* Fragment fragment=new AccountFragment();
                                FragmentManager fragmentManager= getActivity().getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.fl_home_activity,fragment);
                                fragmentTransaction.commit();*//*
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }
        });


    }*/


public class AccountFragment extends Fragment {

    private TextView tvAccountUserName, tvAccountEmailVerified, tvAccountEmailNotVerified,
            tvAccountAlternateNumberVerified,tvAccountAlternateNumberNotVerified;
    private ImageView ivAccountEditNumber, ivAccountEmailNotVerified,
            ivAccountEmailVerified,ivAccountAlternateNumberVerified,ivAccountAlternateNumberNotVerified
            ,ivUploadPanCard;
    private Button btLogout,btSaveChanges;
    private EditText etAccountUserName,etAccountUserEmail,etAccountUserNumber,etAccountUserAlternateNumber;
    private RelativeLayout rlUploadPANCard,rlUploadAddressProof,rlUploadVisitingCard;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvAccountUserName = view.findViewById(R.id.tv_account_user_name);

        etAccountUserName = view.findViewById(R.id.et_account_user_name);

        etAccountUserEmail = view.findViewById(R.id.et_account_user_email);
        tvAccountEmailVerified = view.findViewById(R.id.tv_account_email_verified);
        tvAccountEmailNotVerified = view.findViewById(R.id.tv_account_email_not_verified);
        ivAccountEmailNotVerified = view.findViewById(R.id.iv_account_email_not_verified);
        ivAccountEmailVerified = view.findViewById(R.id.iv_account_email_verified);


        etAccountUserNumber = view.findViewById(R.id.et_account_user_number);


        etAccountUserAlternateNumber = view.findViewById(R.id.et_account_alternate_user_number);
        /*tvAccountAlternateNumberVerified = view.findViewById(R.id.tv_account_alternate_number_verified);
        tvAccountAlternateNumberNotVerified = view.findViewById(R.id.tv_account_alternate_number_not_verified);
        ivAccountAlternateNumberVerified = view.findViewById(R.id.iv_account_alternate_number_verified);
        ivAccountAlternateNumberNotVerified = view.findViewById(R.id.iv_account_alternate_number_not_verified);
*/
        rlUploadPANCard = view.findViewById(R.id.rl_upload_pan_card_activity_account);
        rlUploadAddressProof = view.findViewById(R.id.rl_upload_address_proof_activity_account);
        rlUploadVisitingCard = view.findViewById(R.id.rl_upload_visiting_card_activity_account);

        rlUploadPANCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),UploadActivity.class);
                intent.putExtra("documentname","PAN CARD");
                startActivity(intent);
            }
        });

        rlUploadAddressProof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),UploadActivity.class);
                intent.putExtra("documentname","AADHAR CARD");
                startActivity(intent);
            }
        });

        rlUploadVisitingCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),UploadActivity.class);
                intent.putExtra("documentname","VISITING CARD");
                startActivity(intent);
            }
        });



        btLogout = view.findViewById(R.id.bt_logout);
        btSaveChanges = view.findViewById(R.id.bt_save_changes);



        ivAccountEditNumber = view.findViewById(R.id.iv_account_edit_user_number);

        tvAccountUserName.setText(SharedPrefManager.getInstance(getContext()).getName());
        etAccountUserName.setText(SharedPrefManager.getInstance(getContext()).getName());
        etAccountUserEmail.setText(SharedPrefManager.getInstance(getContext()).getEmail());
        etAccountUserNumber.setText(SharedPrefManager.getInstance(getContext()).getNumber());
        etAccountUserAlternateNumber.setText(SharedPrefManager.getInstance(getContext()).getAlternateNumber());


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
                                                                .child(SharedPrefManager.getInstance(getContext()).getNumber())
                                                                .child("emailverified");
                                                        reference.setValue("Yes");
                                                        SharedPrefManager.getInstance(getContext()).LoginUser(
                                                                SharedPrefManager.getInstance(getContext()).getName(),
                                                                SharedPrefManager.getInstance(getContext()).getEmail(),
                                                                SharedPrefManager.getInstance(getContext()).getNumber(),
                                                                SharedPrefManager.getInstance(getContext()).getAlternateNumber(),
                                                                "Yes",
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
                        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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

        btSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(verifyInputs()){
                    if(isNetworkAvailable()){
                        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                        progressDialog.setMessage("Saving changes...");
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        DatabaseReference reference = FirebaseDatabase.getInstance()
                                .getReference("suppliers")
                                .child(SharedPrefManager.getInstance(getContext()).getNumber());
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                dataSnapshot.child("email").getRef().setValue(etAccountUserEmail.getText().toString());
                                dataSnapshot.child("name").getRef().setValue(etAccountUserName.getText().toString());
                                dataSnapshot.child("number").getRef().setValue(etAccountUserNumber.getText().toString());
                                dataSnapshot.child("alternatenumber").getRef().setValue(etAccountUserAlternateNumber.getText().toString());

                                if (!SharedPrefManager.getInstance(getContext()).getEmail().equals(etAccountUserEmail.getText().toString())){
                                    dataSnapshot.child("emailverified").getRef().setValue("No");
                                    SharedPrefManager.getInstance(getContext())
                                            .LoginUser(etAccountUserName.getText().toString(),
                                                    etAccountUserEmail.getText().toString(),
                                                    etAccountUserNumber.getText().toString(),
                                                    etAccountUserAlternateNumber.getText().toString(),
                                                    "No",
                                                    "Yes"
                                            );
                                    ivAccountEmailNotVerified.setVisibility(View.VISIBLE);
                                    tvAccountEmailNotVerified.setVisibility(View.VISIBLE);
                                    ivAccountEmailVerified.setVisibility(View.GONE);
                                    tvAccountEmailVerified.setVisibility(View.GONE);
                                } else if(SharedPrefManager.getInstance(getContext()).isEmailVerified().equals("Yes")){
                                    SharedPrefManager.getInstance(getContext())
                                            .LoginUser(etAccountUserName.getText().toString(),
                                                    etAccountUserEmail.getText().toString(),
                                                    etAccountUserNumber.getText().toString(),
                                                    etAccountUserAlternateNumber.getText().toString(),
                                                    "Yes",
                                                    "Yes"
                                            );
                                } else if(!SharedPrefManager.getInstance(getContext()).isEmailVerified().equals("Yes")){
                                    SharedPrefManager.getInstance(getContext())
                                            .LoginUser(etAccountUserName.getText().toString(),
                                                    etAccountUserEmail.getText().toString(),
                                                    etAccountUserNumber.getText().toString(),
                                                    etAccountUserAlternateNumber.getText().toString(),
                                                    "No",
                                                    "Yes"
                                            );
                                }
                                tvAccountUserName.setText(etAccountUserName.getText().toString().trim());
                                progressDialog.dismiss();

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }
        });

        btLogout.setOnClickListener(new View.OnClickListener() {
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

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    private boolean verifyInputs(){
        String userName = etAccountUserName.getText().toString().trim();
        String userEmail = etAccountUserEmail.getText().toString().trim();
        //  String userNumber = etAccountUserNumber.getText().toString().trim();
        String userAlternateNumber = etAccountUserAlternateNumber.getText().toString().trim();

        if(userName.isEmpty()){
            etAccountUserName.requestFocus();
            etAccountUserName.setError("Required");
            return false;
        }
        if(userEmail.isEmpty()){
            etAccountUserEmail.requestFocus();
            etAccountUserEmail.setError("Required");
            return false;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
            etAccountUserEmail.requestFocus();
            etAccountUserEmail.setError("Not a valid email");
            return false;
        }

        if(userAlternateNumber.length() >1 && userAlternateNumber.length() <10){
            etAccountUserAlternateNumber.requestFocus();
            etAccountUserAlternateNumber.setError("Not a valid number");
            return false;
        }
        return true;
    }

}
