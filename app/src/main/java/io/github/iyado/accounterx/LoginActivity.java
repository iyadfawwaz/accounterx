package io.github.iyado.accounterx;


import static io.github.iyado.accounterx.AccounterApplication.noti;
import static io.github.iyado.accounterx.utils.CutAndDiscount.exchanges;
import static io.github.iyado.accounterx.RegisterActivity.setDefaultBoxes;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Objects;

import io.github.iyado.accounterx.utils.AccounterException;
import io.github.iyado.accounterx.utils.CurrencyDetails;


public class LoginActivity extends AppCompatActivity {

    Button login;
    TextView alert, reset, register;
    EditText user, pass;
    private AccounterException accounterException;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_FULLSCREEN);

        WindowInsetsControllerCompat windowInsetsControllerCompat = WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());

        windowInsetsControllerCompat.hide(WindowInsetsCompat.Type.systemBars());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = findViewById(R.id.login);
        alert = findViewById(R.id.alert);
        user = findViewById(R.id.userText2);
        pass = findViewById(R.id.passtText3);
        reset = findViewById(R.id.reset);
        register = findViewById(R.id.register);
        firebaseAuth = FirebaseAuth.getInstance();


        login.setOnClickListener(v -> doLogin());
        reset.setOnClickListener(v -> resetFun());
        register.setOnClickListener(v -> registerFun());
    }

    private void registerFun() {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    private void resetFun() {


        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setView(R.layout.reset_layout)
                .setNegativeButton(getString(R.string.cancel), (dialogInterface, i) -> dialogInterface.dismiss())
                .setTitle(getString(R.string.reset))
                .setMessage(getString(R.string.sure))
                .setIcon(R.drawable.baseline_auto_mode_24)
                .create();
        alertDialog.show();
        Button reset = alertDialog.findViewById(R.id.resetBtn);
        TextView errortm = alertDialog.findViewById(R.id.errortm);
        EditText inedittext = alertDialog.findViewById(R.id.inedittext);

        reset.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(inedittext.getText().toString())) {

                firebaseAuth.sendPasswordResetEmail(inedittext.getText().toString())
                        .addOnCompleteListener(task -> {

                            if (task.isSuccessful()) {
                                errortm.setText(getString(R.string.email_sent));
                                noti(this, getString(R.string.email_sent));

                            } else {
                                errortm.setText(Objects.requireNonNull(task.getException()).getLocalizedMessage());
                                noti(this, task.getException().getLocalizedMessage());
                            }

                        });


            } else {
                errortm.setText("يجب ملئ الحقل");
            }

        });


    }


    public void sna(@NonNull AppCompatActivity ignoredActivity, String message){
        Snackbar snackbar = Snackbar.make(this.getWindow().getDecorView().getRootView(), message, Snackbar.LENGTH_LONG);
        snackbar.setAnchorView(findViewById(R.id.alert));
        snackbar.setBehavior(new BaseTransientBottomBar.Behavior());

        snackbar.show();
    }
    private void doLogin() {

        if (!TextUtils.isEmpty(user.getText().toString()) &&
                !TextUtils.isEmpty(pass.getText().toString()) && TextUtils.getTrimmedLength(pass.getText().toString()) >= 6)
       {
            firebaseAuth.signInWithEmailAndPassword(user.getText().toString(),
                    pass.getText().toString())
                    .addOnCompleteListener(task -> {

                        if (task.isSuccessful()){

                                jumpx();
                        }else {
                         //   accounterException = new AccounterException(Objects.requireNonNull(task.getException()).getLocalizedMessage(),task.getException());
                            alert.setText(Objects.requireNonNull(task.getException()).getLocalizedMessage());
                            noti(this,getString(R.string.no_internet));
                            sna(this,getString(R.string.no_internet));
                        }
                    }).addOnFailureListener(e -> {
                       // accounterException = new AccounterException(e.getMessage(),e);
                        alert.setText(e.getLocalizedMessage());
                        noti(this,e.getLocalizedMessage());
                        sna(this,e.getLocalizedMessage());

                    });
        }else {
            alert.setText("يجب ملئ الحقول");
        }
    }
    private void jumpx(){
        Intent intent = new Intent(this,MainActivity.class);
            intent.putExtra("cname", Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid());

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

                reference.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
        .child("currencies").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (!task.getResult().exists()) {
                    reference.child("currencies")
                     .child(exchanges[0]).setValue(new CurrencyDetails(0,exchanges[0],"USD","*",1.0,"","#c22c33"));

                }
                }else {
                    accounterException = new AccounterException(Objects.requireNonNull(task.getException()).getLocalizedMessage(),task.getException());
                    alert.setText(accounterException.getLocalizedMessage());
                    noti(this,accounterException.getLocalizedMessage());
                }

        }).addOnFailureListener(e -> noti(this,e.getMessage()));



        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("users").get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        startActivity(intent);
                    }else {
                        setDefaultBoxes(this,FirebaseAuth.getInstance().getCurrentUser().getUid());
                        startActivity(intent);
                    }

            }else {
                    accounterException = new AccounterException(Objects.requireNonNull(task.getException()).getLocalizedMessage(),task.getException());
                    alert.setText(accounterException.getLocalizedMessage());
                    noti(this,accounterException.getLocalizedMessage());
                }
                }).addOnFailureListener(e -> {
                    accounterException = new AccounterException(e.getMessage(),e);
                    alert.setText(accounterException.getLocalizedMessage());
                noti(this,accounterException.getLocalizedMessage());
            });
    }

}
