package com.spacECE.spaceceedu.Authentication;

import android.util.Log;
import androidx.annotation.Nullable;

public class Account {
    private String account_id = null;
    private String username = null;
    private String contact_number = null;
    private String profile_pic = null;
    private boolean CONSULTANT = false;
    private String U_ID;
    private String Consultant_Category;
    private String Consultant_Office;
    private String Consultant_StartTime;
    private String Consultant_EndTime;
    private String Consultant_Language;
    private String Consultant_Fee;
    private String Consultant_Qualification;

    public Account(String account_id, String username, String contact_number, boolean CONSULTANT, String profile_pic) {
        this.account_id = account_id;
        this.U_ID = account_id; // Ensure U_ID is initialized
        this.username = username;
        this.contact_number = contact_number;
        this.CONSULTANT = CONSULTANT;
        this.profile_pic = profile_pic;
        Log.i("ACCOUNT:", " GENERATED :- " + account_id + " / " + username + " / " + contact_number + " / " + profile_pic + " / ");
    }

    public Account(String account_id, String username, String contact_number, boolean CONSULTANT, String profile_pic,
                   String Consultant_Category, @Nullable String Consultant_Office, String Consultant_StartTime, String Consultant_EndTime,
                   String Consultant_Language, String Consultant_Fee, String Consultant_Qualification) {

        this.account_id = account_id;
        this.U_ID = account_id; // Ensure U_ID is initialized
        this.username = username;
        this.contact_number = contact_number;
        this.CONSULTANT = CONSULTANT;
        this.profile_pic = profile_pic;
        this.Consultant_Category = Consultant_Category;
        this.Consultant_Office = Consultant_Office;
        this.Consultant_StartTime = Consultant_StartTime;
        this.Consultant_EndTime = Consultant_EndTime;
        this.Consultant_Language = Consultant_Language;
        this.Consultant_Fee = Consultant_Fee;
        this.Consultant_Qualification = Consultant_Qualification;

        Log.i("ACCOUNT:", " GENERATED :- " + account_id + " / " + username + " / " + contact_number + " / " + profile_pic + " / " +
                Consultant_Category + " / " + Consultant_Office + " / " + Consultant_StartTime + " / " + Consultant_EndTime + " / " +
                Consultant_Language + " / " + Consultant_Fee + " / " + Consultant_Qualification);
    }

    public String getuId() {
        return U_ID;
    }

    public void setuId(String uId) {
        this.U_ID = uId;
    }

    public String getAccount_id() {
        return account_id;
    }

    public String getUsername() {
        return username;
    }

    public String getContact_number() {
        return contact_number;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public boolean isCONSULTANT() {
        return CONSULTANT;
    }

    public String getConsultant_Category() {
        return Consultant_Category;
    }

    public String getConsultant_Office() {
        return Consultant_Office;
    }

    public String getConsultant_StartTime() {
        return Consultant_StartTime;
    }

    public String getConsultant_EndTime() {
        return Consultant_EndTime;
    }

    public String getConsultant_Language() {
        return Consultant_Language;
    }

    public String getConsultant_Fee() {
        return Consultant_Fee;
    }

    public String getConsultant_Qualification() {
        return Consultant_Qualification;
    }
}
