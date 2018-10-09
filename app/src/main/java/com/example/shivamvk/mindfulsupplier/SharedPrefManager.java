package com.example.shivamvk.mindfulsupplier;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {

    private static SharedPrefManager sharedPrefManager;
    private static Context context;

    private static final String SHARED_PREF_NAME = "MySharedPref";
    private static final String FILTER_SHARED_PREF = "FilterSharedPref";

    private SharedPrefManager(Context context) {
        SharedPrefManager.context = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (sharedPrefManager == null) {
            sharedPrefManager = new SharedPrefManager(context);
        }
        return sharedPrefManager;
    }

    /*public void LoginUser(String name,String email,String number,String alternatenumber, String emailverified){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("name", name);
        editor.putString("email", email);
        editor.putString("number", number);
        editor.putString("emailverified", emailverified);
        editor.putString("alternatenumber",alternatenumber);

        editor.apply();
    }*/

    public void LoginUser(String name, String email, String number,String alternatenumber, String emailverified, String numberverified){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", name);
        editor.putString("email", email);
        editor.putString("number", number);
        editor.putString("emailverified", emailverified);
        editor.putString("numberverified", numberverified);
        editor.putString("alternatenumber",alternatenumber);
        //editor.putString("companyname", companyname);
        // editor.putString("address", address);

        editor.apply();
    }

/*
    public void LoginUser(String name, String email, String number,String emailverified, String numberverified){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", name);
        editor.putString("email", email);
        editor.putString("number", number);
        editor.putString("emailverified", emailverified);
        editor.putString("numberverified", numberverified);
        //editor.putString("companyname", companyname);
        // editor.putString("address", address);

        editor.apply();
    }*/

    public String getName(){
        SharedPreferences sharedPreferences=context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString("name",null);
    }

    public String getEmail(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString("email", null);
    }

    public String getNumber(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString("number", null);
    }

    public String getAlternateNumber(){
        SharedPreferences sharedPreferences=context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString("alternatenumber","");
    }

    public String isEmailVerified() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString("emailverified", "No");
    }


    public String isNumberVerified() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString("numberVerified", "No");
    }

    public void Logout() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public void putFilterOrigin(String origin){
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILTER_SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("filterorigin", origin);
        editor.apply();
    }

    public void putFilterDestination(String destination){
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILTER_SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("filterdestination", destination);
        editor.apply();
    }

    public void putFilterTruck(String truck){
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILTER_SHARED_PREF,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("filtertruck",truck);
        editor.apply();
    }

    public void putFilterMaterial(String material){
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILTER_SHARED_PREF,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("filtermaterial",material);
        editor.apply();
    }

    public void putFilterDate(String date){
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILTER_SHARED_PREF,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("filterdate",date);
        editor.apply();
    }

    public String getFilterOrigin(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILTER_SHARED_PREF, Context.MODE_PRIVATE);
        return sharedPreferences.getString("filterorigin", null);
    }

    public String getFilterDestination(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILTER_SHARED_PREF, Context.MODE_PRIVATE);
        return sharedPreferences.getString("filterdestination", null);
    }

    public String getFilterTruck(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILTER_SHARED_PREF,Context.MODE_PRIVATE);
        return sharedPreferences.getString("filtertruck",null);
    }

    public String getFilterMaterial(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILTER_SHARED_PREF,Context.MODE_PRIVATE);
        return sharedPreferences.getString("filtermaterial",null);
    }

    public String getFilterDate(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILTER_SHARED_PREF,Context.MODE_PRIVATE);
        return sharedPreferences.getString("filterdate",null);
    }

    public void clearFilterSharedPref(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILTER_SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public void aadharcardfront(String aadharcardfront){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("aadharcardfront",aadharcardfront);
        editor.apply();
    }

    public void aadharcardback(String aadharcardback){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("aadharcardback",aadharcardback);
        editor.apply();
    }
    public void pancardfront(String pancardfront){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("pancardfront",pancardfront);
        editor.apply();
    }

    public void visitingcardback(String visitingcardback){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("visitingcardback",visitingcardback);
        editor.apply();
    }
    public void visitingcardfront(String visitingcardfront){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("visitingcardfront",visitingcardfront);
        editor.apply();
    }

    public void pancardback(String pancardback){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("pancardback",pancardback);
        editor.apply();
    }


    public String getPanCardFrontUrl(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString("pancardfront","Not provided");
    }

    public String getPanCardBackUrl(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString("pancardback","Not provided");
    }

    public String getAadharCardFrontUrl(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString("aadharcardfront","Not provided");
    }

    public String getAadharCardBackUrl(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString("aadharcardback","Not provided");
    }

    public String getVisitingCardFrontUrl(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString("visitingcardfront","Not provided");
    }

    public String getVisitingCardBackUrl(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString("visitingcardback","Not provided");
    }


}
