package io.github.iyado.accounterx.utils;


import android.annotation.SuppressLint;
import androidx.annotation.NonNull;
import java.util.ArrayList;

import io.github.iyado.accounterx.methods.GetUsers;


public class LocalUsers {

    private ArrayList<String> usernames;
    private String error;
    @SuppressLint("unused")
    public LocalUsers(){

    }

    public void setUsernamess(ArrayList<String> users) {
        this.usernames = users;
    }

// --Commented out by Inspection START (04/06/2025 12:58):
//    public void setError(String error) {
//        this.error = error;
//    }
// --Commented out by Inspection STOP (04/06/2025 12:58)

// --Commented out by Inspection START (04/06/2025 12:58):
//    public ArrayList<String> getUsernamesrs() {
//        return usernames;
//    }
// --Commented out by Inspection STOP (04/06/2025 12:58)

// --Commented out by Inspection START (04/06/2025 12:58):
//    public String getError() {
//        return error;
//    }
// --Commented out by Inspection STOP (04/06/2025 12:58)

    @SuppressLint("NotifyDataSetChanged")
    public void addGetUsersListener(@NonNull GetUsers listener) {
        if (usernames != null) {
            listener.onGetUsers(usernames);
        }
        if (error != null) {
            listener.onGetUsersError(error);
        }
    }

}
