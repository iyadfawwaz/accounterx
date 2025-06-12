package io.github.iyado.accounterx.utils;


import static io.github.iyado.accounterx.AccounterApplication.noti;

import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import org.jetbrains.annotations.Contract;

import java.util.Objects;

import io.github.iyado.accounterx.R;


public final class AccountManagement {

    private static FirebaseAuth firebaseAuth;
    private static Exception error;
    private final AppCompatActivity activity;


    private AccountManagement(AppCompatActivity activity ){
        this.activity = activity;

    }


    @NonNull
    public static AccountManagement getInstance(AppCompatActivity activity) {

        firebaseAuth = FirebaseAuth.getInstance();
        return new AccountManagement(activity);
    }

    @NonNull
    @Contract(value = "_, _ -> new", pure = true)
    public AccountManagement signUp(String email, String password) {

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        Objects.requireNonNull(task.getResult().getUser()).sendEmailVerification();
                        noti(activity,activity.getString(R.string.email_sent));
                    } else {
                        error = task.getException();
                        assert error != null;
                        noti(activity, error.getMessage());

                    }

                });


        return this;
    }

    @NonNull
    @Contract(value = "_, _ -> new", pure = true)
    public AccountManagement signIn(String email, String password) {

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        noti(activity,activity.getString(R.string.signed_in));

                    }else {
                        error = task.getException();
                        assert error != null;
                        noti(activity, error.getMessage());
                    }
                });


        return this;
    }

    @NonNull
    @Contract(value = " -> new", pure = true)
    public  AccountManagement signOut() {

        firebaseAuth.signOut();
        return this;
    }

    @NonNull
    @Contract(value = " -> new", pure = true)
    public AccountManagement sendEmailVerification() {

        if (firebaseAuth.getCurrentUser() == null) {
            noti(activity,activity.getString(R.string.no_user));
            error = new Exception( activity.getString(R.string.no_user));
            return this;
        }

        firebaseAuth.getCurrentUser().sendEmailVerification();

        return this;
    }

    @NonNull
    @Contract(value = "_ -> new", pure = true)
    public AccountManagement sendPasswordResetEmail(@NonNull String email) {

        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        noti(activity,activity.getString(R.string.email_sent));
                    }else {
                        error = task.getException();
                        assert error != null;
                        noti(activity, error.getMessage());
                    }
                });

        return this;
    }



    @NonNull
    public AccountManagement updateProfile(String displayName, String photoUrl, String phoneNumber, String email, String password, String uid) {

        if (firebaseAuth.getCurrentUser() == null) {
            noti(activity,activity.getString(R.string.no_user));
            error = new Exception(activity.getString(R.string.no_user));
            return this;
        }

        if(!firebaseAuth.getCurrentUser().isEmailVerified()){
            noti(activity,activity.getString(R.string.email_not_verified));
            error = new Exception(activity.getString(R.string.email_not_verified));

        }
        firebaseAuth.getCurrentUser().verifyBeforeUpdateEmail(email);
        firebaseAuth.getCurrentUser().updatePassword(password);
        firebaseAuth.getCurrentUser().getUid();
        firebaseAuth.getCurrentUser().updateProfile(
                new UserProfileChangeRequest.Builder()
                        .setDisplayName(displayName)
                        .setPhotoUri(Uri.parse(photoUrl))
                        .build()
        ).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                noti(activity,activity.getString(R.string.updated));
            }else {
                error = task.getException();
                assert error != null;
                noti(activity, error.getMessage());

            }

        });




        return this;
    }

    @NonNull
    @Contract(value = " -> new", pure = true)
    public AccountManagement delete() {

        if (firebaseAuth.getCurrentUser() == null) {

            noti(activity,activity.getString(R.string.no_user));
            error = new Exception(activity.getString(R.string.no_user));
            return this;
        }

        firebaseAuth.getCurrentUser().delete()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                       noti(activity,activity.getString(R.string.deleted));
                    }else {
                        error = task.getException();
                        assert error != null;
                        noti(activity, error.getMessage());
                    }
                });

        return this;
    }








}
