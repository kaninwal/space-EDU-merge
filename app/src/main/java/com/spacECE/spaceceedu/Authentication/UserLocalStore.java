package com.spacECE.spaceceedu.Authentication;

import android.content.Context;
import android.content.SharedPreferences;

public class UserLocalStore {

    public static final String DETAILS = "UserDetails";
    private final SharedPreferences userLocalDatabase;

    public UserLocalStore(Context context) {
        userLocalDatabase = context.getSharedPreferences(DETAILS, Context.MODE_PRIVATE);
    }

    public Account getLoggedInAccount() {
        String name = userLocalDatabase.getString("username", null);
        String account_id = userLocalDatabase.getString("account_id", null);
        String contact_number = userLocalDatabase.getString("contact_number", null);
        String UID = userLocalDatabase.getString("UID", null);
        String profile_pic = userLocalDatabase.getString("profile_pic", null);
        boolean isConsultant = userLocalDatabase.getBoolean("isConsultant", false);

        if (account_id == null) {
            return null;
        }

        Account account = new Account(account_id, name, contact_number, isConsultant, profile_pic);
        account.setuId(UID);
        
        if (isConsultant) {
            // Note: Account class doesn't have setters for these, but they are stored in SharedPreferences.
            // If needed, the Account class could be updated to hold these values when retrieved.
        }
        
        return account;
    }

    public void setUserLoggedIn(boolean loggedIn, Account account) {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putBoolean("loggedIn", loggedIn);
        if (account != null) {
            spEditor.putString("account_id", account.getAccount_id());
            spEditor.putString("username", account.getUsername());
            spEditor.putString("contact_number", account.getContact_number());
            spEditor.putString("UID", account.getuId());
            spEditor.putBoolean("isConsultant", account.isCONSULTANT());
            spEditor.putString("profile_pic", account.getProfile_pic());

            if (account.isCONSULTANT()) {
                spEditor.putString("consultant_category", account.getConsultant_Category());
                spEditor.putString("consultant_office", account.getConsultant_Office());
                spEditor.putString("consultant_start_time", account.getConsultant_StartTime());
                spEditor.putString("consultant_end_time", account.getConsultant_EndTime());
                spEditor.putString("consultant_fee", account.getConsultant_Fee());
                spEditor.putString("consultant_qualification", account.getConsultant_Qualification());
            }
        }
        spEditor.apply();
    }

    public void setUserLoggedIn(boolean loggedIn) {
        userLocalDatabase.edit().putBoolean("loggedIn", loggedIn).apply();
    }

    public void clearUserData() {
        userLocalDatabase.edit().clear().apply();
    }

    public boolean getUserLoggedIn() {
        return userLocalDatabase.getBoolean("loggedIn", false);
    }
}
