package io.github.iyado.accounterx.inputactivities;


import static io.github.iyado.accounterx.AccounterApplication.noti;
import static io.github.iyado.accounterx.inputactivities.AddRestrictionActivity.doublito;
import static io.github.iyado.accounterx.MainActivity.detectCurAllReturnDolar;
import static io.github.iyado.accounterx.utils.AllInOne.loadCurrencies;
import static io.github.iyado.accounterx.MainActivity.usersReference;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import io.github.iyado.accounterx.adapters.CurrencyNameAdapter;
import io.github.iyado.accounterx.utils.CurrencyDetails;
import io.github.iyado.accounterx.utils.Informations;
import io.github.iyado.accounterx.utils.Prog;
import io.github.iyado.accounterx.R;


public class AddUserActivity extends AppCompatActivity {

    Button saveButton;
   public NumberPicker userid;
   EditText usernamee;
   EditText fullnameArea;
   EditText useraccounte;
    RadioButton chf,cho,chprim,chsec;

    Map<String,Object> map;
    Spinner spinner;
    private boolean isFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        userid       = findViewById(R.id.userid);
        usernamee    = findViewById(R.id.username);
        useraccounte = findViewById(R.id.useraccount);
        chf          = findViewById(R.id.chf);
        cho          = findViewById(R.id.cho);
        chprim       = findViewById(R.id.chb1);
        chsec        = findViewById(R.id.chb2);
        fullnameArea = findViewById(R.id.fullname);
        saveButton   = findViewById(R.id.saveButton);
        spinner      = findViewById(R.id.curr);
        ArrayList<String> currencies = new ArrayList<>();
        for (CurrencyDetails currencyDetails : loadCurrencies().values()){
            currencies.add(currencyDetails.getName());
        }
        CurrencyNameAdapter exAdapter = new CurrencyNameAdapter(this,null);
        exAdapter.setBackupStrings(currencies);
        spinner.setAdapter(exAdapter);

        map          = new HashMap<>();
        saveButton   .setOnClickListener(view -> saveData());

        initWheel();
    }

    public double hello(double r){
        if (chf.isChecked()){
            return -r;
        }
        if (cho.isChecked()){
            return r;
        }
        return 0;
    }

    public String type(){
        if (chprim.isChecked()){
            return "prim";
        }
        if (chsec.isChecked()){
            return "seco";
        }
        return "";

    }
    private void initWheel(){
        userid.setMinValue(0);
        userid.setMaxValue(1000);
    }
    public void saveData(){

         int id = userid.getValue();
          String username = usernamee.getText().toString().trim();
          String account = useraccounte.getText().toString();
          String fullname = fullnameArea.getText().toString();

         String cur = spinner.getSelectedItem().toString();
         if (!username.trim().isEmpty() &&
                 !fullname.trim().isEmpty()) {

             if (chf.isChecked() && cho.isChecked()){
                 noti(this,"لا يمكنك تحديد الاثنين معاَ");
             }else if (!chf.isChecked() && !cho.isChecked()){
                 noti(this,"اختر احد النوعين اما له او عليه");
             }else {

                 if (chprim.isChecked() && chsec.isChecked()) {
                     noti(this, "لا يمكنك تحديد الاثنين معاَ");
                 } else if (!chprim.isChecked() && !chsec.isChecked()) {
                     noti(this, "اختر احد النوعين اما له او عليه");
                 } else {
                     if (account.isEmpty()){
                         account = "0";
                     }
                     AlertDialog.Builder builder = new AlertDialog.Builder(AddUserActivity.this);
                     builder.setCancelable(false);
                     builder.setView(R.layout.toast_layout);
                     AlertDialog dialog = builder.create();
                     dialog.show();
                     uploadData(this, String.valueOf(id), username,fullname, doublito(account), cur);
                     dialog.dismiss();
                 }
             }
         }
    }

    /** @noinspection DataFlowIssue*/
    public void uploadData(@NonNull AppCompatActivity activity, @NonNull String id,
                           @NonNull String username,
                           @NonNull String fullname,
                           double account ,
                           @NonNull String cur){

        Informations userinfo = new Informations(id, type(), fullname);

        long yourmilliseconds = System.currentTimeMillis();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy");
        Date resultdate = new Date(yourmilliseconds);
        String time = String.valueOf(yourmilliseconds);
        String date = sdf.format(resultdate);

        Prog progF = new Prog( username,username,account,
                cur,
                0,0,"افتتاحي","",
                (hello((account))),
                time,date);

        Prog progT = new Prog( username,username,account,
                cur,
                0,0,"افتتاحي","",
                -hello((account)),
                time,date);

        usersReference.child(username)
                        .get().addOnCompleteListener(task -> {
                            if (task.getResult().exists()){
                                noti(AddUserActivity.this,username+"\n اسم المستخدم موجود مسبقاَ");
                            }else {
                                if (isFound) {
                                    noti(AddUserActivity.this, id + "\n رقم الصندوق موجود مسبقاَ");

                                } else {
                                    usersReference
                                            .child(username)
                                            .setValue(userinfo).addOnCompleteListener(taskx -> {
                                                if (taskx.isSuccessful()) {
                                                    DatabaseReference newUser = usersReference
                                                            .child(username);
                                                            newUser.child("accounts")
                                                            .child(date)
                                                            .child(time)
                                                            .setValue(progF)
                                                            .addOnCompleteListener(runnable -> {
                                                                if (runnable.isSuccessful()) {

                                                                    newUser.child("account")
                                                                            .child(cur)
                                                                            .child("count").get()
                                                                            .addOnSuccessListener(dataSnapshot1x -> {
                                                                                double count1 = 0.0d;
                                                                                if (dataSnapshot1x.exists()) {
                                                                                    count1 = dataSnapshot1x.getValue(Double.class);
                                                                                }
                                                                                count1 += hello(account);
                                                                                newUser.child("account")
                                                                                        .child(cur)
                                                                                        .child("count").setValue(count1);

                                                                                newUser.child("all").get().addOnSuccessListener(command -> {
                                                                                    double count1x = 0.0d;
                                                                                    if (command.exists())
                                                                                        count1x = command.getValue(Double.class);
                                                                                    count1x += detectCurAllReturnDolar(cur, hello(account));
                                                                                    newUser.child("all").setValue(count1x);
                                                                                });
                                                                            }).addOnFailureListener(e -> noti(activity, e.getLocalizedMessage()));

                                                                }
                                                            });

                                                    DatabaseReference capitalUser = usersReference
                                                            .child("رأس المال");
                                                            capitalUser.child("accounts")
                                                            .child(date)
                                                            .child(time)
                                                            .setValue(progT)
                                                            .addOnCompleteListener(runnablex -> {
                                                                if (runnablex.isSuccessful()) {

                                                                    capitalUser.child("account")
                                                                            .child(cur)
                                                                            .child("count").get()
                                                                            .addOnSuccessListener(dataSnapshot1x -> {
                                                                                double count1 = 0.0d;
                                                                                if (dataSnapshot1x.exists())
                                                                                    count1 = dataSnapshot1x.getValue(Double.class);
                                                                                count1 -= hello(account);
                                                                                capitalUser.child("account")
                                                                                        .child(cur)
                                                                                        .child("count").setValue(count1);

                                                                                capitalUser.child("all").get().addOnSuccessListener(command -> {
                                                                                    double count1x = 0.0d;
                                                                                    if (command.exists())
                                                                                        count1x = command.getValue(Double.class);
                                                                                    count1x += detectCurAllReturnDolar(cur, -hello(account));
                                                                                    capitalUser.child("all").setValue(count1x);
                                                                                });
                                                                            }).addOnFailureListener(e -> noti(activity, e.getLocalizedMessage()));

                                                                    noti(activity, " \n تمت اضافة" + username + " بنجاح");
                                                                    activity.finish();
                                                                }
                                                            });
                                                }else {
                                                    if (taskx.getException()!= null) {
                                                        noti(activity, taskx.getException().getMessage());
                                                    }
                                                }
                                            })
                                            .addOnFailureListener(e ->noti(activity, e.getMessage()));
                                }
                            }
                        });
    }

    @SuppressWarnings("unused")
    public void setFound(boolean found) {
        isFound = found;
    }
}
