package io.github.iyado.accounterx;


import android.app.Application;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.github.iyado.accounterx.utils.AccounterAlert;


public class AccounterApplication extends Application {


    public static void noti(@NonNull AppCompatActivity activity, String message) {
       AccounterAlert.getInstance(activity).show(message);
       Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
    }
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    public static void check_connection(AppCompatActivity activity, @NonNull String warning){
        FirebaseDatabase.getInstance().getReference(".info/connected")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Boolean connected = snapshot.getValue(Boolean.class);
                        if (connected == null || !connected){
                            Toast.makeText(activity, warning, Toast.LENGTH_LONG).show();
                            activity.finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(activity, error.getMessage(), Toast.LENGTH_LONG).show();
                        activity.finish();

                    }
                });
    }

}
