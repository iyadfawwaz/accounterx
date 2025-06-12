package io.github.iyado.accounterx;


import static io.github.iyado.accounterx.AccounterApplication.noti;
import static io.github.iyado.accounterx.utils.CutAndDiscount.exchanges;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import io.github.iyado.accounterx.databinding.ActivityRegisterBinding;
import io.github.iyado.accounterx.utils.CurrencyDetails;
import io.github.iyado.accounterx.utils.Informations;
import io.github.iyado.accounterx.utils.SingleCurrency;


public class RegisterActivity extends AppCompatActivity {

    TextView alert;
    EditText emailAddress,password;
    Button register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_FULLSCREEN);

        WindowInsetsControllerCompat windowInsetsControllerCompat = WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());

        windowInsetsControllerCompat.hide(WindowInsetsCompat.Type.systemBars());

        super.onCreate(savedInstanceState);

        ActivityRegisterBinding binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        alert = binding.included.alerto;
        emailAddress = binding.included.email;
        password = binding.included.password;
        register = binding.included.reg;

        register.setOnClickListener(v -> regi());

    }

    private void regi() {
        if (!TextUtils.isEmpty(emailAddress.getText()) && !TextUtils.isEmpty(password.getText())){
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailAddress.getText().toString(),
                    password.getText().toString())
                    .addOnCompleteListener(command -> {
                       if (command.isSuccessful()){
                           Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).sendEmailVerification();
                           noti(this,"تم ارسال رسالة تفعيل البريد الالكتروني");

                           finish();
                       }else {
                           alert.setText(Objects.requireNonNull(command.getException()).getLocalizedMessage());
                           noti(this,command.getException().getMessage());
                       }
                    }).addOnFailureListener(command -> noti(this,command.getLocalizedMessage()));
        }
    }


    public static void setDefaultBoxes(@NonNull Context context, @NonNull String uid) {

        DatabaseReference referencex = FirebaseDatabase.getInstance()
                .getReference()
                .child(uid);
        referencex.child("currencies")
                .child(exchanges[0]).setValue(new CurrencyDetails(0,exchanges[0],"USD","*",1.0,"","#c22c33"));
        DatabaseReference reference = referencex
                .child("users");
        reference.child("قيود محققة")
                .setValue(new Informations("0",
                        "prim",
                        "profs"));

        reference.child("قيود محققة")
                .child("account")
                        .child(exchanges[0])
                                .setValue(new SingleCurrency(0));

        reference.child("رأس المال")
                .setValue(new Informations("1",
                        "prim",
                        "all"));
        reference.child("رأس المال")
                .child("account")
                .child(exchanges[0])
                .setValue(new SingleCurrency(0));

        reference.child("مصروف")
                .setValue(new Informations("2",
                        "prim",
                        "mas"));

        reference.child("مصروف")
                .child("account")
                .child(exchanges[0])
                .setValue(new SingleCurrency(0));

        reference.child(context.getString(R.string.profanddeprof))
                .setValue(new Informations("3",
                        "prim",
                        "mas"));

        reference.child(context.getString(R.string.profanddeprof))
                .child("account")
                .child(exchanges[0])
                .setValue(new SingleCurrency(0));

    }

}