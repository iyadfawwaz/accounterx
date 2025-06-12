package io.github.iyado.accounterx.utils;


import android.annotation.SuppressLint;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.snackbar.Snackbar;


public class AccounterAlert {

    private final Snackbar snackbar;
    final AppCompatActivity c;
    @SuppressLint("InflateParams")
    private AccounterAlert(@NonNull AppCompatActivity c) {
        this.c = c;
        snackbar = Snackbar.make(c.getWindow().getDecorView(), "", Snackbar.LENGTH_LONG);
    }

    private static AccounterAlert alert;
    public static AccounterAlert getInstance(AppCompatActivity c) {
        if (alert == null) {
            alert = new AccounterAlert(c);

        }
        return alert;
    }

    @SuppressLint("ResourceAsColor")
    public void show(String message) {
        snackbar.setText(message);
        snackbar.show();

    }

}
