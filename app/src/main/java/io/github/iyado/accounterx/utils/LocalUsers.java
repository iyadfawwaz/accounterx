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

    public void setError(String error) {
        this.error = error;
    }

    @SuppressWarnings("unused")
    public ArrayList<String> getUsernamesrs() {
        return usernames;
    }

    public String getError() {
        return error;
   }

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
