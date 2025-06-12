package io.github.iyado.accounterx.Inputactivities;


import static io.github.iyado.accounterx.AccounterApplication.check_connection;
import static io.github.iyado.accounterx.AccounterApplication.noti;
import static io.github.iyado.accounterx.Inputactivities.AddRestrictionActivity.doublito;
import static io.github.iyado.accounterx.MainActivity.detectCurAllReturnDolar;
import static io.github.iyado.accounterx.MainActivity.localCur;
import static io.github.iyado.accounterx.utils.AllInOne.initCutPrice;
import static io.github.iyado.accounterx.utils.AllInOne.initSymbol;
import static io.github.iyado.accounterx.utils.AllInOne.loadCurrencies;
import static io.github.iyado.accounterx.utils.CutAndDiscount.exchanges;
import static io.github.iyado.accounterx.MainActivity.localUsers;
import static io.github.iyado.accounterx.MainActivity.roundD;
import static io.github.iyado.accounterx.MainActivity.stringo;
import static io.github.iyado.accounterx.MainActivity.usersReference;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DatabaseReference;

import org.jetbrains.annotations.Contract;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import io.github.iyado.accounterx.utils.AllInOne;
import io.github.iyado.accounterx.adapters.CurrencyNameAdapter;
import io.github.iyado.accounterx.utils.CurrencyDetails;
import io.github.iyado.accounterx.adapters.CurrenciesInPrinterAdapter;
import io.github.iyado.accounterx.methods.GetCurrenceis;
import io.github.iyado.accounterx.methods.GetUsers;
import io.github.iyado.accounterx.utils.Prog;
import io.github.iyado.accounterx.R;
import io.github.iyado.accounterx.adapters.CurrenciesAndCountsAdapter;


public class CurrencyConversationActivity extends AppCompatActivity {

    Button cut;
    EditText pr1, pr2, cutpr;
    Spinner ex1, ex2;
    MaterialTextView textView;
    CurrencyNameAdapter cnameAdapter;
    String user;
    CurrencyNameAdapter exAdapter1, exAdapter2;
    AppCompatSpinner spinnercut;
    RecyclerView recyclerView;
    CurrenciesInPrinterAdapter cursNameAdapter;
    CurrenciesAndCountsAdapter adapterF;
    ArrayList<String> cnames;
    HashMap<Integer, CurrencyDetails> hashMap;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_FULLSCREEN);

        WindowInsetsControllerCompat windowInsetsControllerCompat = WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());

        windowInsetsControllerCompat.hide(WindowInsetsCompat.Type.systemBars());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convertion);

        check_connection(this, getString(R.string.cant_cut_without_internet));

        pr1 = findViewById(R.id.price1);
        pr2 = findViewById(R.id.price2);
        cutpr = findViewById(R.id.cutPrice);
        cut = findViewById(R.id.cuto);
        ex1 = findViewById(R.id.spinner01);
        ex2 = findViewById(R.id.spinner02);
        textView = findViewById(R.id.multiAutoCompleteTextView);
        spinnercut = findViewById(R.id.spinnercutcut);
        recyclerView = findViewById(R.id.inlinerecd);

        hashMap = loadCurrencies();

        initializeSpinner();
        AllInOne.taskxx().addTaskxListener(new GetCurrenceis() {
            @Override
            public void getCurrency(HashMap<Integer, CurrencyDetails> currencyDetails) {
                hashMap.putAll(currencyDetails);
            }

            @Override
            public void getError(Exception error) {

                noti(CurrencyConversationActivity.this, error.getMessage());
            }
        });

        ArrayList<String> cursn = new ArrayList<>(hashMap.size());
        for (CurrencyDetails currencyDetails : hashMap.values()) {
            cursn.add(currencyDetails.getName());
        }
        exAdapter1.setBackupStrings(cursn);
        exAdapter2.setBackupStrings(cursn);
        exAdapter2.notifyDataSetChanged();
        exAdapter1.notifyDataSetChanged();
        cursNameAdapter = new CurrenciesInPrinterAdapter("user",hashMap);
        cursNameAdapter.setWhere();
        recyclerView.setLayoutManager(new LinearLayoutManager(CurrencyConversationActivity.this,LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setAdapter(cursNameAdapter);

        if (getIntent().getStringExtra("user") != null) {
            textView.setText(getIntent().getStringExtra("user"));
            cnameAdapter.notifyDataSetChanged();
            cursNameAdapter.setUser(getIntent().getStringExtra("user"));
            cursNameAdapter.notifyDataSetChanged();

        } else {
            textView.setText(getString(R.string.select_user));
        }

        spinnercut.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!pr1.getText().toString().isEmpty() && !cutpr.getText().toString().isEmpty()){

                    double x;
                    if (position == 0){
                        x = doublito(pr1.getText().toString()) * doublito(cutpr.getText().toString());
                    }else {
                        x = doublito(pr1.getText().toString()) / doublito(cutpr.getText().toString());
                    }
                    pr2.setText(stringo(Math.floor(x*100)/100));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        textView.setOnClickListener(v -> searcho(textView, cnames));

        ex2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setCountInPrice(Objects.requireNonNull(((CurrencyNameAdapter) parent.getAdapter()).getItem(position)),pr2);
                String cury = ex2.getSelectedItem().toString();
                String curx = ex1.getSelectedItem().toString();
                if (cury.equals(curx)) {
                    cutpr.setText(1.0 + "");
                } else {
                    cutpr.setText( Math.floor( hii(cury, curx,hashMap)*100)/100+"");
                }

                if (!TextUtils.isEmpty(cutpr.getText()) && !TextUtils.isEmpty(pr1.getText())) {

                    pr2.setText(stringo(doublito(pr1.getText().toString()) * doublito(cutpr.getText().toString())));
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ex1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setCountInPrice(Objects.requireNonNull(((CurrencyNameAdapter) parent.getAdapter()).getItem(position)),pr1);
                String cury = ex2.getSelectedItem().toString();
                String curx = ex1.getSelectedItem().toString();
                if (cury.equals(curx)) {
                    cutpr.setText(1.0 + "");
                } else {
                    cutpr.setText( Math.floor( hii(curx, cury,hashMap)*100)/100+"");
                }

                if (!TextUtils.isEmpty(cutpr.getText()) && !TextUtils.isEmpty(pr1.getText())) {
                    pr2.setText(stringo(doublito(pr1.getText().toString()) * doublito(cutpr.getText().toString())));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Snackbar.make(parent.getRootView(), "لم يتم اختيار العملة", Snackbar.LENGTH_LONG).show();
            }
        });


        pr1.setOnFocusChangeListener((v, hasFocus) -> {

                String s = pr1.getText().toString();
                if (!cutpr.getText().toString().trim().isEmpty() && s.length() > 1 && Double.parseDouble(s) > 0) {
                    double h = 0;
                    try {
                        if (doublito(s) > 0) {
                            if (spinnercut.getSelectedItemPosition() ==0) {
                                h = doublito(cutpr.getText().toString()) * doublito(s);
                            } else {
                                h = Double.parseDouble(s) / doublito(cutpr.getText().toString());
                            }
                        }
                    } catch (NumberFormatException numberFormatException) {
                        noti(CurrencyConversationActivity.this, numberFormatException.getMessage());
                    }
                    pr2.setText(String.valueOf(roundD(h)));



                }
        });


        pr2.setOnFocusChangeListener((v, hasFocus) -> {

            if (!hasFocus){
                String s = pr2.getText().toString();
                if (!cutpr.getText().toString().trim().isEmpty() && s.length() > 1 && Double.parseDouble(s) > 0) {
                    double h = 0;
                    try {


                        if (doublito(s) > 0 && cutpr.getText().length()>0) {
                            if (spinnercut.getSelectedItemPosition() ==0) {

                                h = doublito(cutpr.getText().toString()) * doublito(s);

                            } else {


                                h = Double.parseDouble(s) / doublito(cutpr.getText().toString());
                            }
                        }
                    } catch (NumberFormatException numberFormatException) {
                        noti(CurrencyConversationActivity.this, numberFormatException.getMessage());
                    }
                    pr1.setText(String.valueOf(roundD(h)));



                }
            }

        });

        cut.setOnClickListener(v -> new MaterialAlertDialogBuilder(this, R.style.roundedImageViewRounded)
                .setTitle(getString(R.string.sure))
                .setMessage(getString(R.string.sure))
                .setNegativeButton(getString(R.string.cancel), null)

                .setPositiveButton(getString(R.string.save), (dialog, which) -> doCut())
                .create().show());
    }

    /** @noinspection DataFlowIssue*/
    public static double hii(@NonNull String cur11, String cur22, @NonNull HashMap<Integer, CurrencyDetails> hashMap) {

        double h;
        String cur1 = "", cur2 = "";

        for (CurrencyDetails currencyDetails : hashMap.values()) {
            if (currencyDetails.getName().equals(cur11)) {
                cur1 = currencyDetails.getCode();
            }
            if (currencyDetails.getName().equals(cur22)) {
                cur2 = currencyDetails.getCode();
            }
        }



            if (Objects.equals(initSymbol().get(cur1), "*") && Objects.equals(initSymbol().get(cur2), "*")) {

                if (initCutPrice().get(cur1) > initCutPrice().get(cur2)) {
                    h = initCutPrice().get(cur1) / initCutPrice().get(cur2);
                } else {
                    h = initCutPrice().get(cur2) / initCutPrice().get(cur1);
                }
            } else if (initSymbol().get(cur1).equals("/") && initSymbol().get(cur2).equals("/")) {
                if (initCutPrice().get(cur1) > initCutPrice().get(cur2)) {
                    initCutPrice().get(cur2);
                    initCutPrice().get(cur1);
                } else {
                    initCutPrice().get(cur1);
                    initCutPrice().get(cur2);
                }

                h = initCutPrice().get(cur1) / initCutPrice().get(cur2);
            } else {
                h = initCutPrice().get(cur1) * initCutPrice().get(cur2);
            }

        return h;
    }



    public void searcho(@NonNull TextView search, @NonNull ArrayList<String> arrayList) {
        adapterF = new CurrenciesAndCountsAdapter(search, arrayList);
        Dialog alertDialog = new Dialog(this);
        alertDialog.setContentView(R.layout.dialog_x);
        alertDialog.setCancelable(true);
        Window window = alertDialog.getWindow();
        if (window != null) {
            window.setLayout(getWindow().getAttributes().width, getWindow().getAttributes().height);

            window.setNavigationBarColor(Color.RED);
        }
        alertDialog.show();
        adapterF.setDialog(alertDialog);
        adapterF.setCursNameAdapter(cursNameAdapter);
        SearchView searchView = alertDialog.findViewById(R.id.serx);
        RecyclerView listView = alertDialog.findViewById(R.id.listax);

        listView.setLayoutManager(new LinearLayoutManager(alertDialog.getContext()));
        listView.setAdapter(adapterF);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public boolean onQueryTextSubmit(String query) {
               // initCurs(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                ArrayList<String> searchString = new ArrayList<>();
                for (String string : arrayList) {
                    if (string.toLowerCase().contains(newText.toLowerCase())) {
                        searchString.add(string);
                    }
                }
                adapterF.setBackupStrings(searchString);

                return true;
            }
        });


    }


    private void doCut() {
        user = textView.getText().toString();
        if (!TextUtils.isEmpty(user)) {
            String p1 = pr1.getText().toString();
            String p2 = pr2.getText().toString();
            String cutp = cutpr.getText().toString();
            String e1 = ex1.getSelectedItem().toString();
            String e2 = ex2.getSelectedItem().toString();

            if (!TextUtils.isEmpty(p1) && !TextUtils.isEmpty(p2) && !TextUtils.isEmpty(cutp)) {


                uploadnewData(CurrencyConversationActivity.this, user, doublito(p1), doublito(p2), doublito(cutp), e1, e2);

                blankAll();
                noti(CurrencyConversationActivity.this, e2 + " " + p2 + " بقيمة \n" + e1 + " " + p1 + " لقد قمت بشراء ");

            }
        }
    }

    private void blankAll() {
        pr1.setText("");
        pr2.setText("");
        ex1.setPrompt("");
        cutpr.setText("");
        noti(this, "تم القص بنجاح");
    }

    //cut kayit;
    public void uploadnewData(@NonNull AppCompatActivity activity,
                              @NonNull String username,
                              double pr1,
                              double pr2,
                              double cut,
                              @NonNull String cur1,
                              @NonNull String cur2) {

        long yourmilliseconds = System.currentTimeMillis();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy");
        Date resultdate = new Date(yourmilliseconds);
        String time = String.valueOf(yourmilliseconds);
        String date = sdf.format(resultdate);

        Prog progF = new Prog(username, username, pr1,
                cur1,
                0, 0, "سعر القص " + cut, "",
                 pr1,
                time, date);
        Prog progT = new Prog(username, username, pr2,
                cur2,
                0, 0,  getString(R.string.cut_price) + cut, "",
                -pr2,
                time, date);



        Prog progFx = new Prog(username, username, pr1,
                cur1,
                0, 0,  getString(R.string.cut_price) + cut, "",
                 -pr1,
                time, date);
        Prog progTx = new Prog(username, username, pr2,
                cur2,
                0, 0, getString(R.string.cut_price) + cut, "",
                pr2,
                time, date);
        String bmw = "مركز قطع ";
        String bmw0;
        double bmw2 = 0.0d;
        double bmw1 = 0.0d;
        double newpr = 0.0d;
        double cutfrq = 0.0d;
        for (CurrencyDetails currencyDetails : hashMap.values()) {
            if (currencyDetails.getName().equals(cur2)) {
                double rate = currencyDetails.getRate();
                newpr = pr2/rate;
                cutfrq = pr1 - newpr;
            }
        }

        Prog progsT = new Prog(username, username, roundD(newpr),
                cur1,
                0, 0, "سعر Nelson", "",
                -roundD(newpr),
                time, date);
        Prog progsF = new Prog(username, username, roundD(cutfrq),
                cur1,
                0, 0, "سعر Nelson", "",
                -roundD(cutfrq),
                time, date);



        for (CurrencyDetails currencyDetails : hashMap.values()) {
            if (currencyDetails.getName().equals(cur1)) {
                bmw1 = currencyDetails.getRate();
            }
            if (currencyDetails.getName().equals(cur2)) {
                bmw2 = currencyDetails.getRate();
            }

        }

       // String bmw1 = "فروق صرف";
        if (bmw1 != 1.0){
            bmw0 = bmw+cur1;
        }else {
            bmw0 = bmw+cur2;
        }

        String finalBmw = bmw0;

        double finalCutfrq = cutfrq;

        if (cur1.equals(exchanges[0])) {
            double finalNewpr = newpr;
            double finalCutfrq1 = cutfrq;
            usersReference.child(bmw0).child("account").child(cur2).child("count").get().addOnSuccessListener(dataSnapshot -> {
                if (dataSnapshot.exists()) {
                    //noinspection DataFlowIssue
                    double count = dataSnapshot.getValue(Double.class);
                    if ( count == 0 || Math.abs(pr2) > Math.abs(count)) {
                        noti(this, "لا يوجد رصيد كافي في مركز القطع");
                    } else {
                        String nop;
                        if (finalCutfrq < 0) {nop = "فروق صرف سلبية";} else {nop = "فروق صرف ايجابية";}
                        usersReference.child(nop).child("type").get().addOnSuccessListener(dataSnapshotl -> {
                            if (!dataSnapshotl.exists()) {
                                usersReference.child(finalBmw).child("type").setValue("prim");
                            }

                        });
                        DatabaseReference finalUser = usersReference
                                .child(finalBmw);
                                finalUser.child("accounts")
                                .child(date)
                                .child(time)
                                .setValue(progTx);

                        finalUser.child("account")
                                .child(cur2)
                                .child("count").get()
                                .addOnSuccessListener(dataSnapshot1x -> {
                                    double count1 = 0.0d;
                                    if (dataSnapshot1x.exists()) {
                                        count1 = dataSnapshot1x.getValue(Double.class);
                                    }
                                    count1 += pr2;
                                    finalUser.child("account")
                                            .child(cur2)
                                            .child("count").setValue(count1);

                                    finalUser.child("all").get().addOnSuccessListener(command -> {
                                        double count1x = 0.0d;
                                        if (command.exists()) {
                                            count1x = command.getValue(Double.class);
                                        }
                                        count1x += detectCurAllReturnDolar(cur2, pr2);
                                        finalUser.child("all").setValue(count1x);
                                    });
                                }).addOnFailureListener(e -> noti(activity, e.getLocalizedMessage()));

                        DatabaseReference userRef = usersReference
                                .child(username);
                                userRef.child("accounts")
                                .child(date)
                                .child(time)
                                .setValue(progF)
                                .addOnCompleteListener(task -> {

                                    if (task.isSuccessful()){

                                        userRef.child("account")
                                                .child(cur1)
                                                .child("count").get()
                                                .addOnSuccessListener(dataSnapshot1x -> {
                                                    double count1 = 0.0d;
                                                    if (dataSnapshot1x.exists()) {
                                                        count1 = dataSnapshot1x.getValue(Double.class);
                                                    }
                                                    count1 += pr1;
                                                    userRef.child("account")
                                                            .child(cur1)
                                                            .child("count").setValue(count1);

                                                    userRef.child("all").get().addOnSuccessListener(command -> {
                                                        double count1x = 0.0d;
                                                        if (command.exists()) {
                                                            count1x = command.getValue(Double.class);
                                                        }
                                                        count1x += detectCurAllReturnDolar(cur1, pr1);
                                                        userRef.child("all").setValue(count1x);
                                                    });
                                                }).addOnFailureListener(e -> noti(activity, e.getLocalizedMessage()));

                                        usersReference
                                                .child(username)
                                                .child("accounts")
                                                .child(date)
                                                .child(String.valueOf(System.currentTimeMillis()))
                                                .setValue(progT)
                                                .addOnSuccessListener(unused ->{

                                                    userRef.child("account")
                                                            .child(cur2)
                                                            .child("count").get()
                                                            .addOnSuccessListener(dataSnapshot1x -> {

                                                                double count1 = 0.0d;
                                                                if (dataSnapshot1x.exists()) {
                                                                    count1 = dataSnapshot1x.getValue(Double.class);
                                                                }
                                                                count1 -= pr2;
                                                                userRef.child("account")
                                                                        .child(cur2)
                                                                        .child("count").setValue(count1);
                                                                userRef.child("all").get().addOnSuccessListener(command -> {
                                                                    double count1x = 0.0d;
                                                                    if (command.exists()) {
                                                                        count1x = command.getValue(Double.class);

                                                                        }
                                                                        count1x += detectCurAllReturnDolar(cur2, -pr2);
                                                                        userRef.child("all").setValue(count1x);
                                                                    });
                                                                });


                                                    noti(this, "تم شراء مبلغ " + pr1 + " " + ex1.getSelectedItem());
                                                    noti(CurrencyConversationActivity.this," تم بيع مبلغ "+pr2+" "+ex2.getSelectedItem()+" بنجاح");
                                                }).addOnFailureListener(e -> noti(activity, e.getMessage()));}});
                        usersReference
                                .child(finalBmw)
                                .child("accounts")
                                .child(date)
                                .child(String.valueOf(System.currentTimeMillis()))
                                .setValue(progsT)
                                .addOnSuccessListener(unused -> {

                                    finalUser.child("account")
                                            .child(cur1)
                                            .child("count").get()
                                            .addOnSuccessListener(dataSnapshot1x -> {
                                                double count1f = 0.0d;
                                                if (dataSnapshot1x.exists()) {
                                                    count1f = dataSnapshot1x.getValue(Double.class);
                                                }
                                                count1f -= roundD(finalNewpr);
                                                finalUser.child("account")
                                                        .child(cur1)
                                                        .child("count").setValue(count1f);

                                                finalUser.child("all").get().addOnSuccessListener(command -> {
                                                    double count1x = 0.0d;
                                                    if (command.exists()) {
                                                        count1x = command.getValue(Double.class);
                                                    }
                                                    count1x += detectCurAllReturnDolar(cur1, -roundD(finalNewpr));
                                                    finalUser.child("all").setValue(count1x);
                                                });
                                            }).addOnFailureListener(e -> noti(activity, e.getLocalizedMessage()));

                                    DatabaseReference userNop = usersReference
                                            .child(nop);
                                            userNop.child("accounts")
                                            .child(date)
                                            .child(String.valueOf(System.currentTimeMillis()))
                                            .setValue(progsF)
                                            .addOnSuccessListener(unused1 -> {

                                                userNop.child("account")
                                                        .child(cur1)
                                                        .child("count").get()
                                                        .addOnSuccessListener(dataSnapshot1x -> {
                                                            double count1 = 0.0d;
                                                            if (dataSnapshot1x.exists()) {
                                                                count1 = dataSnapshot1x.getValue(Double.class);
                                                            }
                                                            count1 -= roundD(finalCutfrq1);
                                                            userNop.child("account")
                                                                    .child(cur1)
                                                                    .child("count").setValue(count1);

                                                            userNop.child("all").get().addOnSuccessListener(command -> {
                                                                double count1x = 0.0d;
                                                                if (command.exists()) {
                                                                    count1x = command.getValue(Double.class);
                                                                }
                                                                count1x += detectCurAllReturnDolar(cur1, -roundD(finalCutfrq1));
                                                                userNop.child("all").setValue(count1x);
                                                            });
                                                        }).addOnFailureListener(e -> noti(activity, e.getLocalizedMessage()));

                                            }).addOnFailureListener(e -> noti(activity, e.getMessage()));
                                }).addOnFailureListener(e -> noti(activity, e.getMessage()));
                    }
                }
            });
        }else if (cur2.equals(exchanges[0])) {
            {

                DatabaseReference userBmw = usersReference
                        .child(bmw0);
                        userBmw.child("accounts")
                        .child(date)
                        .child(time)
                        .setValue(progTx);

                        userBmw.child("account")
                        .child(cur2)
                        .child("count").get()
                        .addOnSuccessListener(dataSnapshot1x -> {

                            double count1 = 0.0d;
                            if (dataSnapshot1x.exists()) {
                                count1 = dataSnapshot1x.getValue(Double.class);
                            }
                            count1 += pr2;
                            userBmw.child("account")
                                    .child(cur2)
                                    .child("count").setValue(count1);
                            userBmw.child("all").get().addOnSuccessListener(command -> {
                                double count1x = 0.0d;
                                if (command.exists()) {
                                    count1x = command.getValue(Double.class);
                                }
                                count1x += detectCurAllReturnDolar(cur2, pr2);
                                userBmw.child("all").setValue(count1x);
                            });

                                    }).addOnFailureListener(activity, e -> noti(activity, e.getLocalizedMessage()));



                DatabaseReference userRef = usersReference
                        .child(username);
                        userRef.child("accounts")
                        .child(date)
                        .child(time)
                        .setValue(progF)
                        .addOnCompleteListener(task -> {

                            if (task.isSuccessful()) {

                                userRef.child("account")
                                        .child(cur1)
                                        .child("count").get()
                                        .addOnSuccessListener(dataSnapshot1x -> {

                                            double count1 = 0.0d;
                                            if (dataSnapshot1x.exists()) {
                                                count1 = dataSnapshot1x.getValue(Double.class);
                                            }
                                            count1 += pr1;
                                            userRef.child("account")
                                                    .child(cur1)
                                                    .child("count").setValue(count1);
                                            userRef.child("all").get().addOnSuccessListener(command -> {
                                                double count1x = 0.0d;
                                                if (command.exists()) {
                                                    count1x = command.getValue(Double.class);
                                                }
                                                count1x += detectCurAllReturnDolar(cur1, pr1);
                                                userRef.child("all").setValue(count1x);
                                            });
                                        }).addOnFailureListener(e -> noti(activity, e.getLocalizedMessage()));



                                usersReference
                                        .child(username)
                                        .child("accounts")
                                        .child(date)
                                        .child(String.valueOf(System.currentTimeMillis()))
                                        .setValue(progT)
                                        .addOnSuccessListener(unused -> {

                                            userRef.child("account")
                                                    .child(cur2)
                                                    .child("count").get()
                                                    .addOnSuccessListener(dataSnapshot1x -> {

                                                                double count1 = 0.0d;
                                                                if (dataSnapshot1x.exists()) {
                                                                    count1 = dataSnapshot1x.getValue(Double.class);
                                                                }
                                                                count1 -= pr2;
                                                                userRef.child("account")
                                                                        .child(cur2)
                                                                        .child("count").setValue(count1);
                                                                userRef.child("all").get().addOnSuccessListener(command -> {
                                                                    double count1x = 0.0d;
                                                                    if (command.exists()) {
                                                                        count1x = command.getValue(Double.class);
                                                                    }
                                                                    count1x += detectCurAllReturnDolar(cur2, -pr2);
                                                                    userRef.child("all").setValue(count1x);
                                                                });

                                                            }).addOnFailureListener(e -> noti(activity, e.getLocalizedMessage()));


                                            noti(this, "تم شراء مبلغ " + pr1 + " " + ex1.getSelectedItem());
                                            noti(CurrencyConversationActivity.this, " تم بيع مبلغ " + pr2 + " " + ex2.getSelectedItem() + " بنجاح");
                                        }).addOnFailureListener(e -> noti(activity, e.getMessage()));
                            }
                        });

                usersReference
                        .child(finalBmw)
                        .child("accounts")
                        .child(date)
                        .child(String.valueOf(System.currentTimeMillis()))
                        .setValue(progFx)
                        .addOnSuccessListener(unused -> {

                            userBmw.child("account")
                                    .child(cur1)
                                    .child("count").get().addOnSuccessListener(dataSnapshot1x -> {
                                        double count1 = 0.0d;
                                        if (dataSnapshot1x.exists()) {
                                            count1 = dataSnapshot1x.getValue(Double.class);
                                            }
                                        count1 -= pr1;
                                        userBmw.child("account")
                                                .child(cur1)
                                                .child("count").setValue(count1);
                                        userBmw.child("all").get().addOnSuccessListener(command -> {
                                            double count1x = 0.0d;
                                            if (command.exists()) {
                                                count1x = command.getValue(Double.class);
                                            }
                                            count1x += detectCurAllReturnDolar(cur1, -pr1);
                                            userBmw.child("all").setValue(count1x);
                                        });

                                    }).addOnFailureListener(e -> noti(activity, e.getLocalizedMessage()));

                        })

                        .addOnFailureListener(e -> noti(activity, e.getMessage()));
            }
        }else
        {

            String ccur1 = bmw+cur1;
            String ccur2 = bmw+cur2;
            double dmoqafea = roundD( pr2/bmw2);
            usersReference.child(ccur2).child("account").child(cur2).child("count").get().addOnSuccessListener(dataSnapshot -> {
                if (dataSnapshot.exists()) {
                    //noinspection DataFlowIssue
                    double count = dataSnapshot.getValue(Double.class);
                    if (count == 0 || Math.abs(pr2) > Math.abs(count)) {
                        noti(this, "لا يوجد رصيد كافي في مركز القطع");
                    } else {

                        Prog progmoq = new Prog(username, username, dmoqafea,
                                exchanges[0],
                                0, 0, getString(R.string.profanddeprof), "",
                                dmoqafea,
                                time, date);
                        Prog progmoq1 = new Prog(username, username, dmoqafea,
                                exchanges[0],
                                0, 0,  getString(R.string.profanddeprof), "",
                                -dmoqafea,
                                time, date);

                        DatabaseReference userRef = usersReference
                                .child(username);
                                userRef.child("accounts")
                                .child(date)
                                .child(time)
                                .setValue(progF)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {

                                        userRef.child("account")
                                                .child(cur1)
                                                .child("count").get()
                                                .addOnSuccessListener(dataSnapshot1x -> {
                                                    double count1 = 0.0d;
                                                    if (dataSnapshot1x.exists()) {
                                                        count1 = dataSnapshot1x.getValue(Double.class);
                                                    }
                                                    count1 += pr1;
                                                    userRef.child("account")
                                                            .child(cur1)
                                                            .child("count").setValue(count1);
                                                    userRef.child("all").get().addOnSuccessListener(command -> {
                                                        double count1x = 0.0d;
                                                        if (command.exists()) {
                                                            count1x = command.getValue(Double.class);
                                                            }
                                                        count1x += detectCurAllReturnDolar(cur1, pr1);
                                                        userRef.child("all").setValue(count1x);
                                                    });

                                                        }).addOnFailureListener(e -> noti(activity, e.getLocalizedMessage()));

                                        usersReference
                                                .child(username)
                                                .child("accounts")
                                                .child(date)
                                                .child(String.valueOf(System.currentTimeMillis()))
                                                .setValue(progT)
                                                .addOnSuccessListener(unused -> {

                                                    userRef.child("account")
                                                            .child(cur2)
                                                            .child("count").get()
                                                            .addOnSuccessListener(dataSnapshot1x -> {
                                                                double count1 = 0.0d;
                                                                if (dataSnapshot1x.exists()) {
                                                                    count1 = dataSnapshot1x.getValue(Double.class);
                                                                }
                                                                count1 -= pr2;
                                                                userRef.child("account")
                                                                        .child(cur2)
                                                                        .child("count").setValue(count1);
                                                                userRef.child("all").get().addOnSuccessListener(command -> {
                                                                    double count1x = 0.0d;
                                                                    if (command.exists()) {
                                                                        count1x = command.getValue(Double.class);
                                                                        }
                                                                    count1x += detectCurAllReturnDolar(cur2, -pr2);
                                                                    userRef.child("all").setValue(count1x);
                                                                });
                                                            }).addOnFailureListener(e -> noti(activity, e.getLocalizedMessage()));


                                                    DatabaseReference userCcur1 = usersReference
                                                            .child(ccur1);
                                                            userCcur1.child("accounts")
                                                            .child(date)
                                                            .child(time)
                                                            .setValue(progFx)
                                                            .addOnSuccessListener(unused1 -> {

                                                                userCcur1.child("account")
                                                                        .child(cur1)
                                                                        .child("count").get()
                                                                        .addOnSuccessListener(dataSnapshot1x -> {
                                                                            double count1 = 0.0d;
                                                                            if (dataSnapshot1x.exists()) {
                                                                                count1 = dataSnapshot1x.getValue(Double.class);
                                                                            }
                                                                            count1 -= pr1;
                                                                            userCcur1.child("account")
                                                                                    .child(cur1)
                                                                                    .child("count").setValue(count1);

                                                                            userCcur1.child("all").get().addOnSuccessListener(command -> {
                                                                                double count1x = 0.0d;
                                                                                if (command.exists()) {
                                                                                    count1x = command.getValue(Double.class);
                                                                                }
                                                                                count1x += detectCurAllReturnDolar(cur1, -pr1);
                                                                                userCcur1.child("all").setValue(count1x);
                                                                            }).addOnFailureListener(e -> noti(activity, e.getLocalizedMessage()));

                                                                                    }).addOnFailureListener(e -> noti(activity, e.getLocalizedMessage()));


                                                                usersReference
                                                                        .child(ccur1)
                                                                        .child("accounts")
                                                                        .child(date)
                                                                        .child(String.valueOf(System.currentTimeMillis()))
                                                                        .setValue(progmoq)
                                                                        .addOnSuccessListener(unused2 -> {

                                                                            userCcur1.child("account")
                                                                                    .child(exchanges[0])
                                                                                    .child("count").get()
                                                                                    .addOnSuccessListener(dataSnapshot1x -> {
                                                                                        double count1 = 0.0d;
                                                                                        if (dataSnapshot1x.exists()) {
                                                                                            count1 = dataSnapshot1x.getValue(Double.class);
                                                                                        }
                                                                                        count1 += dmoqafea;
                                                                                        userCcur1.child("account")
                                                                                                .child(exchanges[0])
                                                                                                .child("count").setValue(count1);

                                                                                        userCcur1.child("all").get().addOnSuccessListener(command -> {
                                                                                            double count1x = 0.0d;
                                                                                            if (command.exists()) {
                                                                                                count1x = command.getValue(Double.class);
                                                                                                }
                                                                                            count1x += detectCurAllReturnDolar(exchanges[0], dmoqafea);
                                                                                            userCcur1.child("all").setValue(count1x);
                                                                                        });
                                                                                    }).addOnFailureListener(e -> noti(activity, e.getLocalizedMessage()));


                                                                            DatabaseReference userCcur2 = usersReference
                                                                                    .child(ccur2);
                                                                                    userCcur2.child("accounts")
                                                                                    .child(date)
                                                                                    .child(time)
                                                                                    .setValue(progTx)
                                                                                    .addOnSuccessListener(unused3 -> {

                                                                                        userCcur2.child("account")
                                                                                                .child(cur2)
                                                                                                .child("count").get()
                                                                                                .addOnSuccessListener(dataSnapshot1x -> {
                                                                                                            double count1 = 0.0d;
                                                                                                            if (dataSnapshot1x.exists()) {
                                                                                                                count1 = dataSnapshot1x.getValue(Double.class);
                                                                                                            }
                                                                                                            count1 += pr2;
                                                                                                            userCcur2.child("account")
                                                                                                                    .child(cur2)
                                                                                                                    .child("count").setValue(count1);
                                                                                                            userCcur2.child("all").get().addOnSuccessListener(command -> {
                                                                                                                double count1x = 0.0d;
                                                                                                                if (command.exists()) {
                                                                                                                    count1x = command.getValue(Double.class);
                                                                                                                }
                                                                                                                count1x += detectCurAllReturnDolar(cur2, pr2);
                                                                                                                userCcur2.child("all").setValue(count1x);

                                                                                                            }).addOnFailureListener(e -> noti(activity, e.getLocalizedMessage()));
                                                                                                        }).addOnFailureListener(e -> noti(activity, e.getLocalizedMessage()));

                                                                                                usersReference
                                                                                                        .child(ccur2)
                                                                                                        .child("accounts")
                                                                                                        .child(date)
                                                                                                        .child(String.valueOf(System.currentTimeMillis()))
                                                                                                        .setValue(progmoq1).addOnFailureListener(command -> {

                                                                                                            userCcur2.child("account")
                                                                                                                    .child(exchanges[0])
                                                                                                                    .child("count").get()
                                                                                                                    .addOnSuccessListener(dataSnapshot1x -> {
                                                                                                                        double count1 = 0.0d;
                                                                                                                        if (dataSnapshot1x.exists()) {
                                                                                                                            count1 = dataSnapshot1x.getValue(Double.class);
                                                                                                                        }
                                                                                                                        count1 -= dmoqafea;
                                                                                                                        userCcur2.child("account")
                                                                                                                                .child(exchanges[0])
                                                                                                                                .child("count").setValue(count1);

                                                                                                                        userCcur2.child("all").get().addOnSuccessListener(commandx -> {
                                                                                                                            double count1x = 0.0d;
                                                                                                                            if (commandx.exists()) {
                                                                                                                                count1x = commandx.getValue(Double.class);
                                                                                                                            }
                                                                                                                            count1x += detectCurAllReturnDolar(exchanges[0], -dmoqafea);
                                                                                                                            userCcur2.child("all").setValue(count1x);
                                                                                                                        });
                                                                                                                    }).addOnFailureListener(e -> noti(activity, e.getLocalizedMessage()));

                                                                                                        })
                                                                                                        .addOnFailureListener(e -> noti(activity, e.getLocalizedMessage()));
                                                                                            }
                                                                                    ).addOnFailureListener(e -> noti(activity, e.getLocalizedMessage()));
                                                                        }).addOnFailureListener(e -> noti(activity, e.getLocalizedMessage()));
                                                            }).addOnFailureListener(e -> noti(activity, e.getLocalizedMessage()));
                                                }).addOnFailureListener(e -> noti(activity, e.getLocalizedMessage()));
                                    }
                                });
                    }
                }
            });

        }


    }



    public void setCountInPrice(@NonNull String cur, EditText area) {

            usersReference.child(textView.getText().toString())
                    .child("account")
                    .child(cur).child("count")
                    .get().addOnSuccessListener(dataSnapshot -> {
                        if (dataSnapshot.getValue() == null) return;
                        //noinspection DataFlowIssue
                        area.setText( stringo(dataSnapshot.getValue(Double.class)));
                    });
        }

    private void initializeSpinner(){


        spinnercut.setEnabled(true);
        spinnercut.setVisibility(View.VISIBLE);
        spinnercut.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return 2;
            }

            @NonNull
            @Contract(pure = true)
            @Override
            public Object getItem(int position) {
                String x;
                if (position == 0){
                    x = getString(R.string.multiply);
                }else {
                    x = getString(R.string.divide);
                }
                return x;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @SuppressLint("ViewHolder")
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.view_list_item, parent, false);
                TextView txtVw_DisplayName = view.findViewById(R.id.TxtVw_DisplayName);
                txtVw_DisplayName.setText(getItem(position).toString());

                return view;
            }
        });

        exAdapter1 = new CurrencyNameAdapter(this,new ArrayList<>());
        exAdapter2 = new CurrencyNameAdapter(this,new ArrayList<>());

        ex1.setAdapter(exAdapter1);
        ex2.setAdapter(exAdapter2);



        localUsers.addGetUsersListener(new GetUsers() {
            @Override
            public void onGetUsers(@NonNull ArrayList<String> users) {
                cnames = users;
            }

            @Override
            public void onGetUsersError(@NonNull String error) {

                cnames = new ArrayList<>();
                cnames.add(error);
            }
        });

        cnameAdapter = new CurrencyNameAdapter(this,cnames);

    }
}
