package io.github.iyado.accounterx.Inputactivities;


import static android.app.NotificationManager.IMPORTANCE_HIGH;
import static android.app.PendingIntent.FLAG_IMMUTABLE;
import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static io.github.iyado.accounterx.AccounterApplication.check_connection;
import static io.github.iyado.accounterx.AccounterApplication.noti;
import static io.github.iyado.accounterx.utils.AllInOne.loadCurrencies;
import static io.github.iyado.accounterx.Inputactivities.CurrencyConversationActivity.hii;
import static io.github.iyado.accounterx.utils.CutAndDiscount.exchanges;
import static io.github.iyado.accounterx.MainActivity.detectCurAllReturnDolar;
import static io.github.iyado.accounterx.MainActivity.localUsers;
import static io.github.iyado.accounterx.MainActivity.roundD;
import static io.github.iyado.accounterx.MainActivity.stringo;
import static io.github.iyado.accounterx.MainActivity.usersReference;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
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
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.NotificationCompat;
import androidx.core.app.Person;
import androidx.core.graphics.drawable.IconCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import io.github.iyado.accounterx.adapters.CurrencyNameAdapter;
import io.github.iyado.accounterx.adapters.LocalSpinnerAdapter;
import io.github.iyado.accounterx.utils.CurrencyDetails;
import io.github.iyado.accounterx.methods.GetUsers;
import io.github.iyado.accounterx.utils.Prog;
import io.github.iyado.accounterx.R;
import io.github.iyado.accounterx.adapters.CurrenciesAndCountsAdapter;


public class AddRestrictionActivity extends AppCompatActivity {


    ArrayList<String> localUsersx,localCurs;
    Spinner exss,exyy;
    CurrenciesAndCountsAdapter adapterF;
    CurrencyNameAdapter exAdapter,eyAdapter;
   TextView searchF;
    TextView searchT;
    TextView foq,tht;
    EditText cy1,cy2,nz1,nz2;
    EditText prof1,prof2,customer;
    EditText cutPrice;
    AppCompatSpinner spinnercut;
    Button adder;
    static HashMap<Integer, CurrencyDetails> hashMap;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {


        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_restriction);

        check_connection(this, getString(R.string.cant_add_without_internet));


        adder    = findViewById(R.id.button2);
        cy1      = findViewById(R.id.count1);
        cy2      = findViewById(R.id.count2);
        nz1      = findViewById(R.id.notice1);
        nz2      = findViewById(R.id.notice2);
        exss      = findViewById(R.id.spinner3);
        exyy      = findViewById(R.id.spinner4);
        prof1    = findViewById(R.id.profit1);
        prof2    = findViewById(R.id.profit2);
        customer = findViewById(R.id.customer);
        searchF  = findViewById(R.id.searchF);
        searchT  = findViewById(R.id.searchT);
        foq      = findViewById(R.id.segmafoq);
        tht      = findViewById(R.id.segmatht);
        spinnercut = findViewById(R.id.spinnercutcut);
        cutPrice = findViewById(R.id.cutPrice);


        hashMap = loadCurrencies();
        localCurs = new ArrayList<>(hashMap.size());
        initView();
        for (CurrencyDetails currencyDetails : hashMap.values()) {
            localCurs.add(currencyDetails.getName());
        }
        exAdapter.setBackupStrings(localCurs);
        eyAdapter.setBackupStrings(localCurs);
        exAdapter.notifyDataSetChanged();
        eyAdapter.notifyDataSetChanged();


        spinnercut.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (!cy1.getText().toString().isEmpty() && !cutPrice.getText().toString().isEmpty()){

                    double x;
                    if (position == 0){
                        x = doublito(cy1.getText().toString()) * doublito(cutPrice.getText().toString());

                    }else {
                        x = doublito(cy1.getText().toString()) / doublito(cutPrice.getText().toString());
                    }
                    cy2.setText(stringo(Math.floor(x*100)/100));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                noti(AddRestrictionActivity.this,getString(R.string.no_thing_selected));

            }
        });

        exss.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                exyy.setSelection(i,true);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                noti(AddRestrictionActivity.this,getString(R.string.no_thing_selected));
            }
        });

        exyy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String cury = exyy.getSelectedItem().toString();
                String curx = exss.getSelectedItem().toString();
                cutPrice.setText( hii(cury,curx,hashMap)+"");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                noti(AddRestrictionActivity.this,getString(R.string.no_thing_selected));

            }
        });

        searchF.setOnClickListener(view -> dothis(searchF));
       searchT.setOnClickListener(view -> dothis(searchT));
        cy1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (i > 0) {

                    if (!prof1.getText().toString().trim().isEmpty()){
                        foq.setText(String.valueOf(doublito(String.valueOf(charSequence))+doublito(prof1.getText().toString())));
                    }else {

                        foq.setText(charSequence);

                    }
                    if (spinnercut.getSelectedItem().toString().equals(getString(R.string.multiply))) {
                        cy2.setText(String.valueOf(Math.floor(100 * doublito(String.valueOf(charSequence)) *
                                doublito(cutPrice.getText().toString())) / 100));
                    }else if (spinnercut.getSelectedItem().toString().equals(getString(R.string.divide))){
                            cy2.setText(String.valueOf(Math.floor(100 * doublito(String.valueOf(charSequence)) /
                                    doublito(cutPrice.getText().toString()))/100));

                    }

                }
            }
            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        prof1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {



            }

            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable s) {

                if (s!=null && s.length()>0) {
                    if (!cy1.getText().toString().trim().isEmpty()) {
                        double x = doublito(cy1.getText().toString()) + doublito(s.toString());
                        foq.setText(getString(R.string.fromx) + " " + x);
                    }else {
                        foq.setText(getString(R.string.fromx) + " " + s);
                    }
                }


            }
        });
        prof2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (start>0){
                    if (!cy2.getText().toString().trim().isEmpty()) {


                        tht.setText(String.valueOf(doublito(String.valueOf(s))+doublito(cy2.getText().toString())));
                    }else {
                        tht.setText(s);
                    }
                }
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && s.length() > 0) {

                    double kayit;
                    double x1 = 0.0d;
                    double x2 = 0.0d;
                    for (CurrencyDetails currencyDetails : hashMap.values()) {

                        if (currencyDetails.getName().equals(exss.getSelectedItem().toString())) {
                            if (currencyDetails.getSymbol().equals("*")){
                                x1 = doublito(prof1.getText().toString())*currencyDetails.getRate();
                            }else {
                                x1 = doublito(prof1.getText().toString())/currencyDetails.getRate();
                            }
                        }
                        if (currencyDetails.getName().equals(exyy.getSelectedItem().toString())) {
                            if (currencyDetails.getSymbol().equals("*")){
                                x2 = doublito(prof2.getText().toString())*currencyDetails.getRate();
                            }else {
                                x2 = doublito(prof2.getText().toString())/currencyDetails.getRate();
                            }
                        }
                    }
                    kayit = x1-x2;

                    tht.setText(getString(R.string.tox) + " " + tht.getText().toString()+ "\n" + kayit);
                }
            }
        });

        adder.setOnClickListener(view ->
                FirebaseDatabase.getInstance().getReference(".info/connected")
                .addValueEventListener(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.VANILLA_ICE_CREAM)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Boolean connected = snapshot.getValue(Boolean.class);
                        if (Boolean.TRUE.equals(connected)){

            doAdd(
                    AddRestrictionActivity.this,
            usersReference,
            searchF,
            searchT,
            exss,
                    exyy,
                    cy1,
            cy2,
            prof1,
            prof2,
            customer,
            nz1, nz2);

                        }else {
                            noti(AddRestrictionActivity.this,getString(R.string.cant_add_without_internet));

                            blankAll(getString(R.string.u_r_not_connected)+"/n"+getString(R.string.adder_to_waiting_list));
                                }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        noti(AddRestrictionActivity.this,error.getMessage());
                    }
                }));

    }

    private void blankAll(String constraction){
        searchF.setText("");
        searchT.setText("");
        exss.setPrompt("");
        exyy.setPrompt("");
        cy1.setText("");
        cy2.setText("");
        prof1.setText("");
        prof2.setText("");
        customer.setText("");
        nz1.setText("");
        nz2.setText("");
        noti(this,constraction);

    }

    private void initView(){



        adapterF = new CurrenciesAndCountsAdapter(searchF,null);

        localUsers.addGetUsersListener(new GetUsers() {
            @Override
            public void onGetUsers(@NonNull ArrayList<String> users) {
               localUsersx = users;
            }

            @Override
            public void onGetUsersError(@NonNull String error) {

                localUsersx = new ArrayList<>();
                localUsersx.add(error);
            }});

        adapterF.setBackupStrings(localUsersx);

        exAdapter = new CurrencyNameAdapter(this,new ArrayList<>());
        eyAdapter = new CurrencyNameAdapter(this,new ArrayList<>());
        exss.setAdapter(exAdapter);
        exyy.setAdapter(eyAdapter);

        exss.setEnabled(true);
        exyy.setEnabled(true);
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
            public @Unmodifiable Object getItem(int position) {
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
    }


    @SuppressLint("NotifyDataSetChanged")
    private void dothis(TextView search) {

        adapterF.setTextViewx(search);

        Dialog alertDialog = new Dialog(AddRestrictionActivity.this);
        alertDialog.setContentView(R.layout.dialog_x);
        alertDialog.setCancelable(true);
        Window window = alertDialog.getWindow();
        if (window != null) {
            window.setLayout(getWindow().getAttributes().width, getWindow().getAttributes().height);

            window.setNavigationBarColor(Color.RED);
        }
        alertDialog.show();
        adapterF.setDialog(alertDialog);
        SearchView searchView = alertDialog.findViewById(R.id.serx);
        RecyclerView listView = alertDialog.findViewById(R.id.listax);

        listView.setLayoutManager(new LinearLayoutManager(alertDialog.getContext()));
        listView.setAdapter(adapterF);
        adapterF.notifyDataSetChanged();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                ArrayList<String> searchString = new ArrayList<>();
                for (String string : localUsersx){
                    if (string.toLowerCase().contains(newText.toLowerCase())){
                        searchString.add(string);
                    }
                }
                adapterF.setBackupStrings(searchString);

                return true;
            }
        });


        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {



            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }



    @RequiresApi(api = Build.VERSION_CODES.VANILLA_ICE_CREAM)
    @SuppressLint("NotifyDataSetChanged")
    public void doAdd(@NonNull AppCompatActivity activity, @NonNull DatabaseReference reference, @NonNull TextView sender,
                             @NonNull TextView receiver,
                             @NonNull Spinner exsend,
                             @NonNull Spinner exrec,
                             @NonNull EditText countF,
                             @NonNull EditText countT,
                             @NonNull EditText profitF,
                             @NonNull EditText profitT,
                             @NonNull EditText customer,
                             @NonNull EditText nz1,
                             @NonNull EditText nz2) {


        long yourmilliseconds = System.currentTimeMillis();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy");
        Date resultdate = new Date(yourmilliseconds);

        String time = String.valueOf(yourmilliseconds);
        String date = sdf.format(resultdate);

        String countf = countF.getText().toString();
        String countt = countT.getText().toString();
        String profitf = profitF.getText().toString();
        String profitt = profitT.getText().toString();
        String senderx = sender.getText().toString();
        String recieverx = receiver.getText().toString();

        if (countF.getText().length()<1) {
            countf = "0";
        }
        if (countT.getText().length()<1) {
            countt = "0";
        }
        if (profitF.getText().length()<1) {
            profitf = "0";
        }
        if (profitT.getText().length()<1) {
            profitt = "0";
        }
        double s1 = doublito(countf) + doublito(profitf);
        double s2 = 0 - (doublito(countt) + doublito(profitt));

        Prog progX = new Prog(sender.getText().toString(), receiver.getText().toString(), doublito(profitf),
                exsend.getSelectedItem().toString(),
                0, 0, customer.getText().toString(), nz1.getText().toString(), -doublito(profitf),
                time, date);

        Prog progY = new Prog(sender.getText().toString(), receiver.getText().toString(), doublito(profitt),
                exrec.getSelectedItem().toString(),
                0, 0, customer.getText().toString(), nz1.getText().toString(), doublito(profitt),
                time, date);

        Prog progF = new Prog(sender.getText().toString(), receiver.getText().toString(), doublito(countf),
                exsend.getSelectedItem().toString(),
                doublito(profitf), doublito(profitt), customer.getText().toString(), nz1.getText().toString(),
                s1,
                time, date);

        Prog progT = new Prog(sender.getText().toString(), receiver.getText().toString(), doublito(countt),
                exrec.getSelectedItem().toString(),
                doublito(profitf), doublito(profitt), customer.getText().toString(), nz2.getText().toString(),
                s2,
                time, date);

        if (!sender.getText().toString().equals(activity.getString(R.string.sendcenter)) &&
                !receiver.getText().toString().equals(activity.getString(R.string.recievecenter)) && !sender.getText().toString().equals(receiver.getText().toString()))

        {

            String bmw = "مركز قطع ";
            String bmw0;
            double bmw2 = 0.0d;
            double bmw1 = 0.0d;
            double newpr = 0.0d;
            double cutfrq = 0.0d;
            for (CurrencyDetails currencyDetails : hashMap.values()) {
                if (currencyDetails.getName().equals(exrec.getSelectedItem().toString())) {
                    double rate = currencyDetails.getRate();
                    newpr = doublito(countt) / rate;
                    cutfrq = doublito(countf) - newpr;
                }
            }
            for (CurrencyDetails currencyDetails : hashMap.values()) {
                if (currencyDetails.getName().equals(exsend.getSelectedItem().toString())) {
                    bmw1 = currencyDetails.getRate();
                }
                if (currencyDetails.getName().equals(exrec.getSelectedItem().toString())) {
                    bmw2 = currencyDetails.getRate();
                }

            }

            Prog progsT = new Prog(sender.getText().toString(), receiver.getText().toString(), roundD(newpr),
                    exsend.getSelectedItem().toString(),
                    0, 0, "قص من "+exsend.getSelectedItem().toString()+"الى "+exrec.getSelectedItem().toString()+"سعر القص "+cutPrice.getText().toString(), "",
                    -roundD(newpr),
                    time, date);
            Prog progsF = new Prog(sender.getText().toString(), receiver.getText().toString(), roundD(cutfrq),
                    exsend.getSelectedItem().toString(),
                    0, 0, String.format("قص من %sالى %sسعر القص %s", exsend.getSelectedItem().toString(), exrec.getSelectedItem().toString(), cutPrice.getText().toString()), "",
                    -roundD(cutfrq),
                    time, date);

            if (bmw1 != 1.0) {
                bmw0 = bmw + exsend.getSelectedItem().toString();
            } else {
                bmw0 = bmw + exrec.getSelectedItem().toString();
            }

            String finalBmw = bmw0;
            double finalCutfrq = cutfrq;

            Prog progFx = new Prog(sender.getText().toString(), receiver.getText().toString(), doublito(countf),
                    exsend.getSelectedItem().toString(),
                    0, 0, String.format("قص من %sالى %sسعر القص %s", exsend.getSelectedItem().toString(), exrec.getSelectedItem().toString(), cutPrice.getText().toString()), "",
                    -doublito(countf),
                    time, date);

            Prog progTx = new Prog(sender.getText().toString(), receiver.getText().toString(), doublito(countt),
                    exrec.getSelectedItem().toString(),
                    0, 0, String.format("قص من %sالى %sسعر القص %s", exsend.getSelectedItem().toString(), exrec.getSelectedItem().toString(), cutPrice.getText().toString()), "",
                    doublito(countt),
                    time, date);



            if (!exsend.getSelectedItem().toString().equals(exrec.getSelectedItem().toString()) && !exrec.getSelectedItem().toString().equals(exchanges[0]) && !exsend.getSelectedItem().toString().equals(exchanges[0])){


                    String ccur1 = bmw+exsend.getSelectedItem().toString();
                    String ccur2 = bmw+exrec.getSelectedItem().toString();
                    double dmoqafea = roundD( doublito(countt)/bmw2);
                    String finalCountt1 = countt;
                    String finalProfitf1 = profitf;
                    String finalProfitt1 = profitt;
                    //from here;----->

                String finalCountf = countf;
                String finalCountt2 = countt;
                usersReference.child(ccur2).child("account")
                            .child(exrec.getSelectedItem().toString())
                            .child("count").get()
                            .addOnSuccessListener(dataSnapshot -> {
                                if (dataSnapshot.exists()) {
                                    //noinspection DataFlowIssue
                                    double count = dataSnapshot.getValue(Double.class);
                                    if (count == 0 || Math.abs(doublito(finalCountt1)) > Math.abs(count)) {
                                        noti(this, "لا يوجد رصيد كافي في مركز القطع");
                                    } else {
                                        if (doublito(finalProfitf1) != 0) {
                                            DatabaseReference thisUser = reference.child("قيود محققة");
                                                    thisUser.child("accounts")
                                                    .child(date)
                                                    .child(time)
                                                    .setValue(progX).addOnSuccessListener(unused1 -> {
                                                       thisUser.child("account")
                                                               .child(exsend.getSelectedItem().toString())
                                                               .child("count").get()
                                                               .addOnSuccessListener(dataSnapshot1x -> {
                                                                   double count1 = 0.0d;
                                                                   if (dataSnapshot1x.exists()) {
                                                                       count1 = dataSnapshot1x.getValue(Double.class);
                                                                   }
                                                               count1 -= doublito(finalProfitf1);
                                                               thisUser.child("account")
                                                               .child(exsend.getSelectedItem().toString())
                                                               .child("count").setValue(count1);

                                                                   thisUser.child("all").get().addOnSuccessListener(command -> {
                                                                       double count1x = 0.0d;
                                                                       if (command.exists()) {
                                                                           count1x = command.getValue(Double.class);
                                                                       }
                                                                       count1x += detectCurAllReturnDolar(exsend.getSelectedItem().toString(), -doublito(finalProfitf1));
                                                                       thisUser.child("all").setValue(count1x);
                                                                   });

                                                               }).addOnFailureListener(e -> noti(activity, e.getLocalizedMessage()));



                                                        if (doublito(finalProfitt1) != 0) {
                                                            reference.child("قيود محققة")
                                                                    .child("accounts")
                                                                    .child(date)
                                                                    .child(String.valueOf(System.currentTimeMillis()))
                                                                    .setValue(progY);
                                                            thisUser.child("account")
                                                                    .child(exrec.getSelectedItem().toString())
                                                                    .child("count").get()
                                                                    .addOnSuccessListener(dataSnapshot2x -> {
                                                                        double count2 = 0.0d;
                                                                        if (dataSnapshot2x.exists()) {
                                                                            count2 = dataSnapshot2x.getValue(Double.class);
                                                                        }
                                                                count2 += doublito(finalProfitt1);
                                                                thisUser.child("account")
                                                                        .child(exrec.getSelectedItem().toString())
                                                                        .child("count").setValue(count2);
                                                                        thisUser.child("all").get().addOnSuccessListener(command -> {
                                                                            double count1x = 0.0d;
                                                                            if (command.exists()) {
                                                                                count1x = command.getValue(Double.class);
                                                                            }
                                                                            count1x += detectCurAllReturnDolar(exrec.getSelectedItem().toString(), doublito(finalProfitt1));
                                                                            thisUser.child("all").setValue(count1x);
                                                                        });

                                                                    }).addOnFailureListener(e -> noti(activity, e.getLocalizedMessage()));

                                                        }
                                                    });
                                        }

                                        Prog progmoq = new Prog(sender.getText().toString(), receiver.getText().toString(), dmoqafea,
                                                exchanges[0],
                                                0, 0, String.format("قص من %sالى %sسعر القص %s", exsend.getSelectedItem().toString(), exrec.getSelectedItem().toString(), cutPrice.getText().toString()), "",
                                                dmoqafea,
                                                time, date);
                                        Prog progmoq1 = new Prog(sender.getText().toString(), receiver.getText().toString(), dmoqafea,
                                                exchanges[0],
                                                0, 0, String.format("قص من %sالى %sسعر القص %s", exsend.getSelectedItem().toString(), exrec.getSelectedItem().toString(), cutPrice.getText().toString()), "",
                                                -dmoqafea,
                                                time, date);


                                        DatabaseReference senderUser = reference.child(senderx);
                                                senderUser.child("accounts")
                                                .child(date)
                                                .child(time)
                                                .setValue(progF).addOnSuccessListener(unused1 -> {
                                                    senderUser.child("account")
                                                            .child(exsend.getSelectedItem().toString())
                                                            .child("count").get()
                                                            .addOnSuccessListener(dataSnapshot1x -> {
                                                                double count1 = 0.0d;
                                                                if (dataSnapshot1x.exists()) {
                                                                    count1 = dataSnapshot1x.getValue(Double.class);
                                                                }
                                                                count1 += s1;
                                                                senderUser.child("account")
                                                                        .child(exsend.getSelectedItem().toString())
                                                                        .child("count").setValue(count1);
                                                                senderUser.child("all").get().addOnSuccessListener(command -> {
                                                                    double count1x = 0.0d;
                                                                    if (command.exists()) {
                                                                        count1x = command.getValue(Double.class);
                                                                    }
                                                                    count1x += detectCurAllReturnDolar(exsend.getSelectedItem().toString(), s1);
                                                                    senderUser.child("all").setValue(count1x);
                                                                });
                                                            });

                                                            DatabaseReference receiverUser = reference.child(recieverx);
                                                                    receiverUser.child("accounts")
                                                                    .child(date)
                                                                    .child(time)
                                                                    .setValue(progT).addOnSuccessListener(unused -> receiverUser.child("account")
                                                                            .child(exrec.getSelectedItem().toString())
                                                                            .child("count").get()
                                                                            .addOnSuccessListener(dataSnapshot2x -> {

                                                                                double count2 = 0.0d;
                                                                                if (dataSnapshot2x.exists()) {
                                                                                    count2 = dataSnapshot2x.getValue(Double.class);
                                                                                }
                                                                                count2 += s2;
                                                                                receiverUser.child("account")
                                                                                        .child(exrec.getSelectedItem().toString())
                                                                                        .child("count").setValue(count2);

                                                                                senderUser.child("all").get().addOnSuccessListener(command -> {
                                                                                    double count1x = 0.0d;
                                                                                    if (command.exists()) {
                                                                                        count1x = command.getValue(Double.class);
                                                                                    }
                                                                                    count1x += detectCurAllReturnDolar(exrec.getSelectedItem().toString(), s2);
                                                                                    senderUser.child("all").setValue(count1x);
                                                                                });


                                                                            }).addOnFailureListener(e -> noti(activity, e.getLocalizedMessage())));

                                                                        DatabaseReference localUser = usersReference
                                                                                .child(ccur1);
                                                                                localUser.child("accounts")
                                                                                .child(date)
                                                                                .child(time)
                                                                                .setValue(progFx)
                                                                                .addOnSuccessListener(unused1x -> {
                                                                                    localUser.child("account")
                                                                                            .child(exsend.getSelectedItem().toString())
                                                                                            .child("count").get()
                                                                                            .addOnSuccessListener(dataSnapshot3x -> {
                                                                                                double count3 = 0.0d;
                                                                                                if (dataSnapshot3x.exists()) {
                                                                                                    count3 = dataSnapshot3x.getValue(Double.class);
                                                                                                }
                                                                                                count3 -= doublito(finalCountf);
                                                                                                localUser.child("account")
                                                                                                        .child(exsend.getSelectedItem().toString())
                                                                                                        .child("count").setValue(count3);

                                                                                                senderUser.child("all").get().addOnSuccessListener(command -> {
                                                                                                    double count1x = 0.0d;
                                                                                                    if (command.exists()) {
                                                                                                        count1x = command.getValue(Double.class);
                                                                                                    }
                                                                                                    count1x += detectCurAllReturnDolar(exsend.getSelectedItem().toString(), -doublito(finalCountf));
                                                                                                    senderUser.child("all").setValue(count1x);
                                                                                                });
                                                                                            });
                                                                                    usersReference
                                                                                            .child(ccur1)
                                                                                            .child("accounts")
                                                                                            .child(date)
                                                                                            .child(String.valueOf(System.currentTimeMillis()))
                                                                                            .setValue(progmoq)
                                                                                            .addOnSuccessListener(unused2 -> {

                                                                                                localUser.child("account")
                                                                                                        .child(exchanges[0])
                                                                                                        .child("count").get()
                                                                                                        .addOnSuccessListener(dataSnapshot4x -> {
                                                                                                            double count4 = 0.0d;
                                                                                                            if (dataSnapshot4x.exists()) {
                                                                                                                count4 = dataSnapshot4x.getValue(Double.class);
                                                                                                            }
                                                                                                            count4 += dmoqafea;
                                                                                                            localUser.child("account")
                                                                                                                    .child(exchanges[0])
                                                                                                                    .child("count").setValue(count4);

                                                                                                            localUser.child("all").get().addOnSuccessListener(command -> {
                                                                                                                double count1x = 0.0d;
                                                                                                                if (command.exists()) {
                                                                                                                    count1x = command.getValue(Double.class);
                                                                                                                }
                                                                                                                count1x += detectCurAllReturnDolar(exchanges[0], dmoqafea);
                                                                                                                localUser.child("all").setValue(count1x);
                                                                                                            });
                                                                                                        });
                                                                                                DatabaseReference localUser2 = usersReference
                                                                                                        .child(ccur2);
                                                                                                        localUser2.child("accounts")
                                                                                                        .child(date)
                                                                                                        .child(time)
                                                                                                        .setValue(progTx)
                                                                                                        .addOnSuccessListener(unused3 -> {

                                                                                                            localUser2.child("account")
                                                                                                                    .child(exrec.getSelectedItem().toString())
                                                                                                                    .child("count").get()
                                                                                                                    .addOnSuccessListener(dataSnapshot5x -> {
                                                                                                                        double count5 = 0.0d;
                                                                                                                        if (dataSnapshot5x.exists()) {
                                                                                                                            count5 = dataSnapshot5x.getValue(Double.class);
                                                                                                                        }
                                                                                                                        count5 +=doublito(finalCountt2);
                                                                                                                        localUser2.child("account")
                                                                                                                                .child(exrec.getSelectedItem().toString())
                                                                                                                                .child("count").setValue(count5);

                                                                                                                        localUser2.child("all").get().addOnSuccessListener(command -> {
                                                                                                                            double count1x = 0.0d;
                                                                                                                            if (command.exists()) {
                                                                                                                                count1x = command.getValue(Double.class);
                                                                                                                            }
                                                                                                                            count1x += detectCurAllReturnDolar(exrec.getSelectedItem().toString(), doublito(finalCountt2));
                                                                                                                            localUser2.child("all").setValue(count1x);
                                                                                                                        });
                                                                                                                    });
                                                                                                                    usersReference
                                                                                                                            .child(ccur2)
                                                                                                                            .child("accounts")
                                                                                                                            .child(date)
                                                                                                                            .child(String.valueOf(System.currentTimeMillis()))
                                                                                                                            .setValue(progmoq1).addOnSuccessListener(unused4 -> localUser2.child("account")
                                                                                                                                    .child(exchanges[0])
                                                                                                                            .child("count").get()

                                                                                                                                    .addOnSuccessListener(dataSnapshot6x -> {
                                                                                                                                        double count6 = 0.0d;
                                                                                                                                        if (dataSnapshot6x.exists()) {
                                                                                                                                            count6 = dataSnapshot6x.getValue(Double.class);
                                                                                                                                        }
                                                                                                                                        count6 -= dmoqafea;
                                                                                                                                        localUser2.child("account")
                                                                                                                                                .child(exchanges[0])
                                                                                                                                                .child("count").setValue(count6);

                                                                                                                                        localUser2.child("all").get().addOnSuccessListener(command -> {
                                                                                                                                            double count1x = 0.0d;
                                                                                                                                            if (command.exists()) {
                                                                                                                                                count1x = command.getValue(Double.class);
                                                                                                                                            }
                                                                                                                                            count1x += detectCurAllReturnDolar(exchanges[0], -dmoqafea);
                                                                                                                                            localUser2.child("all").setValue(count1x);
                                                                                                                                        });
                                                                                                                                    })
                                                                                                                        .addOnFailureListener(e -> noti(activity, e.getLocalizedMessage()))
                                                                                                        ).addOnFailureListener(e -> noti(activity, e.getLocalizedMessage()));
                                                                                            })
                                                                                            .addOnFailureListener(e -> noti(activity, e.getLocalizedMessage()));
                                                                                })
                                                                                .addOnFailureListener(e -> noti(activity, e.getLocalizedMessage()));
                                                                    }).addOnFailureListener(e -> noti(activity, e.getLocalizedMessage()));
                                                        });
                                    }
                                }
                                else {
                                    noti(this,getString(R.string.no_user));
                                }
                            });

            }


            if (exsend.getSelectedItem().toString().equals(exrec.getSelectedItem().toString())) {

                Prog progXX = new Prog(sender.getText().toString(), receiver.getText().toString(), roundD(doublito(profitt)-doublito(profitf)),
                        exsend.getSelectedItem().toString(),
                        0, 0, customer.getText().toString(), nz1.getText().toString(), roundD(doublito(profitt)-doublito(profitf)),
                        time, date);


                DatabaseReference senderUser = reference.child(sender.getText().toString());
                        senderUser.child("accounts")
                        .child(date)
                        .child(time)
                        .setValue(progF);
                        senderUser.child("account")
                                .child(exsend.getSelectedItem().toString())
                        .child("count").get()
                                .addOnSuccessListener(dataSnapshot1x -> {

                                            double count1 = 0.0d;
                                            if (dataSnapshot1x.exists()){
                                                count1 = dataSnapshot1x.getValue(Double.class);
                                        }
                                    count1 += s1;
                                    senderUser.child("account")
                                            .child(exsend.getSelectedItem().toString())
                                            .child("count").setValue(count1);

                                    senderUser.child("all").get().addOnSuccessListener(command -> {
                                        double count1x = 0.0d;
                                        if (command.exists()) {
                                            count1x = command.getValue(Double.class);
                                        }
                                        count1x += detectCurAllReturnDolar(exsend.getSelectedItem().toString(), s1);
                                        senderUser.child("all").setValue(count1x);
                                    });
                                }).addOnFailureListener(e -> noti(activity, e.getLocalizedMessage()));

                DatabaseReference receiverUser = reference.child(receiver.getText().toString());
                        receiverUser.child("accounts")
                        .child(date)
                        .child(time)
                        .setValue(progT);
                        receiverUser.child("account")
                                .child(exrec.getSelectedItem().toString())
                        .child("count").get()
                                .addOnSuccessListener(dataSnapshot2x -> {
                                            double count2 = 0.0d;
                                            if (dataSnapshot2x.exists()){
                                                count2 = dataSnapshot2x.getValue(Double.class);
                                        }
                                    count2 += s2;
                                    receiverUser.child("account")
                                            .child(exrec.getSelectedItem().toString())
                                            .child("count").setValue(count2);

                                    receiverUser.child("all").get().addOnSuccessListener(commandc -> {
                                        double count1xx = 0.0d;
                                        if (commandc.exists()) {
                                            count1xx = commandc.getValue(Double.class);
                                        }
                                        count1xx += detectCurAllReturnDolar(exrec.getSelectedItem().toString(), s2);
                                        receiverUser.child("all").setValue(count1xx);
                                    });
                                }).addOnFailureListener(e -> noti(activity, e.getLocalizedMessage()));

                if (doublito(profitf) != 0 || doublito(profitt) != 0) {
                    DatabaseReference thisUser = reference.child("قيود محققة");
                    String finalProfitt2 = profitt;
                    String finalProfitf2 = profitf;
                    thisUser.child("accounts")
                            .child(date)
                            .child(time)
                            .setValue(progXX)
                            .addOnCompleteListener(runnable1 -> {
                                if (runnable1.isSuccessful()) {
                                    thisUser.child("account")
                                            .child(exsend.getSelectedItem().toString())
                                            .child("count").get()
                                            .addOnSuccessListener(dataSnapshot1x -> {

                                                double count1 = 0.0d;
                                                if (dataSnapshot1x.exists()) {
                                                    count1 = dataSnapshot1x.getValue(Double.class);
                                                }
                                                count1 +=  roundD(doublito(finalProfitt2)-doublito(finalProfitf2));
                                                thisUser.child("account")
                                                        .child(exsend.getSelectedItem().toString())
                                                        .child("count").setValue(count1);

                                                thisUser.child("all").get().addOnSuccessListener(command -> {
                                                    double count1x = 0.0d;
                                                    if (command.exists()) {
                                                        count1x = command.getValue(Double.class);
                                                    }
                                                    count1x += detectCurAllReturnDolar(exsend.getSelectedItem().toString(), roundD(doublito(finalProfitt2)-doublito(finalProfitf2)));
                                                    thisUser.child("all").setValue(count1x);
                                                });
                                            }).addOnFailureListener(e -> noti(activity, e.getLocalizedMessage()));

                                     noti(activity, s1 +   getString(R.string.added));
                                     blankAll(time);
                                }
                            });
                }
            }
            else {


                if (exsend.getSelectedItem().toString().equals(exchanges[0])) {
                    String finalCountt = countt;
                    String finalProfitf = profitf;
                    String finalProfitt = profitt;
                    double finalNewpr = newpr;
                    double finalCutfrq1 = cutfrq;
                    usersReference.child(bmw0).child("account").child(exrec.getSelectedItem().toString()).child("count").get().addOnSuccessListener(dataSnapshot -> {
                        if (dataSnapshot.exists()) {
                            //noinspection DataFlowIssue
                            double count = dataSnapshot.getValue(Double.class);
                            if (count == 0 || Math.abs(doublito(finalCountt)) > Math.abs(count)) {
                                noti(this, "لا يوجد رصيد كافي في مركز القطع");
                            } else {

                                DatabaseReference senderUser = reference.child(senderx);
                                        senderUser.child("accounts")
                                        .child(date)
                                        .child(String.valueOf(System.currentTimeMillis()))
                                        .setValue(progF);
                                        senderUser.child("account")
                                                .child(exsend.getSelectedItem().toString())
                                        .child("count").get()
                                                .addOnSuccessListener(dataSnapshot1x -> {
                                                            double count1 = 0.0d;
                                                            if (dataSnapshot1x.exists()) {
                                                                count1 = dataSnapshot1x.getValue(Double.class);
                                                            }
                                                            count1 += s1;
                                                            senderUser.child("account")
                                                                    .child(exsend.getSelectedItem().toString())
                                                                    .child("count").setValue(count1);
                                                    senderUser.child("all").get().addOnSuccessListener(command -> {
                                                        double count1x = 0.0d;
                                                        if (command.exists()) {
                                                            count1x = command.getValue(Double.class);
                                                        }
                                                        count1x += detectCurAllReturnDolar(exsend.getSelectedItem().toString(), s1);
                                                        senderUser.child("all").setValue(count1x);
                                                    });
                                                        }).addOnFailureListener(e -> noti(activity, e.getLocalizedMessage()));


                                DatabaseReference receiverUser = reference.child(recieverx);
                                        receiverUser.child("accounts")
                                        .child(date)
                                        .child(String.valueOf(System.currentTimeMillis()))
                                        .setValue(progT);
                                        receiverUser.child("account")
                                                .child(exrec.getSelectedItem().toString())
                                        .child("count").get()
                                                .addOnSuccessListener(dataSnapshot2x -> {
                                                    double count2 = 0.0d;
                                                    if (dataSnapshot2x.exists()) {
                                                        count2 = dataSnapshot2x.getValue(Double.class);
                                                    }
                                                    count2 += s2;
                                                    receiverUser.child("account")
                                                            .child(exrec.getSelectedItem().toString())
                                                            .child("count").setValue(count2);

                                                    receiverUser.child("all").get().addOnSuccessListener(command -> {
                                                        double count1x = 0.0d;
                                                        if (command.exists()) {
                                                            count1x = command.getValue(Double.class);
                                                        }
                                                        count1x += detectCurAllReturnDolar(exrec.getSelectedItem().toString(), s2);
                                                        receiverUser.child("all").setValue(count1x);
                                                    });
                                                }).addOnFailureListener(e -> noti(activity, e.getLocalizedMessage()));

                                if (doublito(finalProfitf) != 0) {
                                    DatabaseReference thisUser = reference.child("قيود محققة");
                                            thisUser.child("accounts")
                                            .child(date)
                                            .child(time)
                                            .setValue(progX);
                                            thisUser.child("account")
                                                    .child(exsend.getSelectedItem().toString())
                                            .child("count").get()

                                                    .addOnSuccessListener(dataSnapshot1x -> {
                                                        double count1 = 0.0d;
                                                        if (dataSnapshot1x.exists()) {
                                                            count1 = dataSnapshot1x.getValue(Double.class);
                                                        }
                                                        count1 -= doublito(finalProfitf);
                                                        thisUser.child("account")
                                                                .child(exsend.getSelectedItem().toString())
                                                                .child("count").setValue(count1);

                                                        thisUser.child("all").get().addOnSuccessListener(command -> {
                                                            double count1x = 0.0d;
                                                            if (command.exists()) {
                                                                count1x = command.getValue(Double.class);
                                                            }
                                                            count1x += detectCurAllReturnDolar(exsend.getSelectedItem().toString(), -doublito(finalProfitf));
                                                            thisUser.child("all").setValue(count1x);
                                                        });
                                                    });
                                }
                                if (doublito(finalProfitt) != 0) {
                                    DatabaseReference thisUser = reference.child("قيود محققة");
                                            thisUser.child("accounts")
                                            .child(date)
                                            .child(String.valueOf(System.currentTimeMillis()))
                                            .setValue(progY);
                                    thisUser.child("account")
                                            .child(exrec.getSelectedItem().toString())
                                    .child("count").get()
                                            .addOnSuccessListener(dataSnapshot2x -> {
                                                double count2 = 0.0d;
                                                if (dataSnapshot2x.exists()) {
                                                    count2 = dataSnapshot2x.getValue(Double.class);
                                                }
                                                count2 += doublito(finalProfitt);
                                                thisUser.child("account")
                                                        .child(exrec.getSelectedItem().toString())
                                                        .child("count").setValue(count2);

                                                thisUser.child("all").get().addOnSuccessListener(command -> {
                                                    double count1x = 0.0d;
                                                    if (command.exists()) {
                                                        count1x = command.getValue(Double.class);
                                                    }
                                                    count1x += detectCurAllReturnDolar(exrec.getSelectedItem().toString(), doublito(finalProfitt));
                                                    thisUser.child("all").setValue(count1x);
                                                });
                                            });
                                }
                                String nop;
                                if (finalCutfrq < 0) {
                                    nop = "فروق صرف سلبية";
                                } else {
                                    nop = "فروق صرف ايجابية";
                                }
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
                                                .child(exrec.getSelectedItem().toString())
                                        .child("count").get()
                                                .addOnSuccessListener(dataSnapshot1x -> {
                                                    double count1 = 0.0d;
                                                    if (dataSnapshot1x.exists()) {
                                                        count1 = dataSnapshot1x.getValue(Double.class);
                                                    }
                                                    count1 += doublito(finalCountt);
                                                    finalUser.child("account")
                                                            .child(exrec.getSelectedItem().toString())
                                                            .child("count").setValue(count1);

                                                    finalUser.child("all").get().addOnSuccessListener(command -> {
                                                        double count1x = 0.0d;
                                                        if (command.exists()) {
                                                            count1x = command.getValue(Double.class);
                                                        }
                                                        count1x += detectCurAllReturnDolar(exrec.getSelectedItem().toString(), doublito(finalCountt));
                                                        finalUser.child("all").setValue(count1x);
                                                    });
                                                });


                                usersReference
                                        .child(finalBmw)
                                        .child("accounts")
                                        .child(date)
                                        .child(String.valueOf(System.currentTimeMillis()))
                                        .setValue(progsT)
                                        .addOnSuccessListener(unused ->
                                                finalUser.child("account")
                                                        .child(exsend.getSelectedItem().toString())
                                                        .child("count").get()
                                                        .addOnSuccessListener(dataSnapshot2x ->{

                                                                double count2 = 0.0d;
                                                                if (dataSnapshot2x.exists()) {
                                                                    count2 = dataSnapshot2x.getValue(Double.class);
                                                                }
                                                                count2 -= finalNewpr;
                                                                finalUser.child("account")
                                                                        .child(exsend.getSelectedItem().toString())
                                                                        .child("count").setValue(count2);

                                                                finalUser.child("all").get().addOnSuccessListener(command -> {
                                                                double count1x = 0.0d;
                                                                if (command.exists()) {
                                                                    count1x = command.getValue(Double.class);
                                                                }
                                                                count1x += detectCurAllReturnDolar(exsend.getSelectedItem().toString(), -finalNewpr);
                                                                finalUser.child("all").setValue(count1x);
                                                            });
                                                        }).addOnFailureListener(e -> noti(activity, e.getLocalizedMessage())));


                                                DatabaseReference nopUser = usersReference
                                                        .child(nop);
                                                        nopUser.child("accounts")
                                                        .child(date)
                                                        .child(time)
                                                        .setValue(progsF).addOnSuccessListener(unused1 -> nopUser.child("account")
                                                                .child(exsend.getSelectedItem().toString())
                                                                .child("count").get()
                                                                .addOnSuccessListener(dataSnapshot1x -> {
                                                                    double count1 = 0.0d;
                                                                    if (dataSnapshot1x.exists()) {
                                                                        count1 = dataSnapshot1x.getValue(Double.class);
                                                                    }
                                                                    count1 -= finalCutfrq1;

                                                                    nopUser.child("account")
                                                                            .child(exsend.getSelectedItem().toString())
                                                                            .child("count").setValue(count1);

                                                                    nopUser.child("all").get().addOnSuccessListener(command -> {
                                                                        double count1x = 0.0d;
                                                                        if (command.exists()) {
                                                                            count1x = command.getValue(Double.class);
                                                                        }
                                                                        count1x += detectCurAllReturnDolar(exsend.getSelectedItem().toString(), -finalCutfrq1);
                                                                        nopUser.child("all").setValue(count1x);
                                                                    });

                                                                }).addOnFailureListener(command -> noti(activity, command.getLocalizedMessage())))

                                                        .addOnFailureListener(e -> noti(activity, e.getLocalizedMessage()));
                            }
                        }
                    });
                }

                if (exrec.getSelectedItem().toString().equals(exchanges[0])) {

                    if (doublito(profitf) != 0) {
                        DatabaseReference thisUser = reference.child("قيود محققة");
                                thisUser.child("accounts")
                                .child(date)
                                .child(time)
                                .setValue(progX);
                        String finalProfitf3 = profitf;
                        thisUser.child("account")
                                .child(exsend.getSelectedItem().toString())
                        .child("count").get()
                                .addOnSuccessListener(dataSnapshot1x -> {
                                    double count1 = 0.0d;
                                    if (dataSnapshot1x.exists()) {
                                        count1 = dataSnapshot1x.getValue(Double.class);
                                    }
                                    count1 -= doublito(finalProfitf3);
                                    thisUser.child("account")
                                            .child(exsend.getSelectedItem().toString())
                                            .child("count").setValue(count1);

                                    thisUser.child("all").get().addOnSuccessListener(command -> {
                                        double count1x = 0.0d;
                                        if (command.exists()) {
                                            count1x = command.getValue(Double.class);
                                        }
                                        count1x += detectCurAllReturnDolar(exsend.getSelectedItem().toString(), -doublito(finalProfitf3));
                                        thisUser.child("all").setValue(count1x);
                                    });
                                }).addOnFailureListener(e -> noti(activity, e.getLocalizedMessage()));

                    }
                    if (doublito(profitt) != 0) {
                        DatabaseReference thisUser = reference.child("قيود محققة");
                                thisUser.child("accounts")
                                .child(date)
                                .child(String.valueOf(System.currentTimeMillis()))
                                .setValue(progY);
                        String finalProfitt3 = profitt;
                        thisUser.child("account")
                                        .child(exrec.getSelectedItem().toString())
                                .child("count").get()
                                        .addOnSuccessListener(dataSnapshot2x -> {
                                            double count2 = 0.0d;
                                            if (dataSnapshot2x.exists()) {
                                                count2 = dataSnapshot2x.getValue(Double.class);
                                            }
                                            count2 += doublito(finalProfitt3);
                                            thisUser.child("account")
                                                    .child(exrec.getSelectedItem().toString())
                                                    .child("count").setValue(count2);

                                            thisUser.child("all").get().addOnSuccessListener(command -> {
                                                double count1x = 0.0d;
                                                if (command.exists()) {
                                                    count1x = command.getValue(Double.class);
                                                }
                                                count1x += detectCurAllReturnDolar(exrec.getSelectedItem().toString(), doublito(finalProfitt3));
                                                thisUser.child("all").setValue(count1x);
                                            });
                                        }).addOnFailureListener(e -> noti(activity, e.getLocalizedMessage()));

                    }

                    DatabaseReference bmwUser = usersReference
                            .child(bmw0);
                            bmwUser.child("accounts")
                            .child(date)
                            .child(time)
                            .setValue(progTx);
                    String finalCountt3 = countt;
                    bmwUser.child("account")
                                    .child(exrec.getSelectedItem().toString())
                            .child("count").get()
                                    .addOnSuccessListener(dataSnapshot1x -> {
                                        double count1=0.0d;
                                        if (dataSnapshot1x.exists()) {
                                            count1 = dataSnapshot1x.getValue(Double.class);
                                        }
                                        count1 += doublito(finalCountt3);
                                        bmwUser.child("account")
                                                .child(exrec.getSelectedItem().toString())
                                                .child("count").setValue(count1);

                                        bmwUser.child("all").get().addOnSuccessListener(command -> {
                                            double count1x = 0.0d;
                                            if (command.exists()) {
                                                count1x = command.getValue(Double.class);
                                            }
                                            count1x += detectCurAllReturnDolar(exrec.getSelectedItem().toString(), doublito(finalCountt3));
                                            bmwUser.child("all").setValue(count1x);
                                        });
                                    });

                    DatabaseReference senderUser = reference.child(sender.getText().toString());
                            senderUser.child("accounts")
                            .child(date)
                            .child(time)
                            .setValue(progF);

                            senderUser.child("account")
                                    .child(exsend.getSelectedItem().toString())
                            .child("count").get()
                                    .addOnSuccessListener(dataSnapshot1x -> {
                                                double count1 = 0.0d;
                                                if (dataSnapshot1x.exists()) {
                                                    count1 = dataSnapshot1x.getValue(Double.class);
                                                }
                                                count1 += s1;
                                                senderUser.child("account")
                                                        .child(exsend.getSelectedItem().toString())
                                                        .child("count").setValue(count1);

                                        senderUser.child("all").get().addOnSuccessListener(command -> {
                                            double count1x = 0.0d;
                                            if (command.exists()) {
                                                count1x = command.getValue(Double.class);
                                            }
                                            count1x += detectCurAllReturnDolar(exsend.getSelectedItem().toString(), s1);
                                            senderUser.child("all").setValue(count1x);
                                        });
                                            }).addOnFailureListener(e -> noti(activity, e.getLocalizedMessage()));


                    DatabaseReference receiverUser = reference.child(receiver.getText().toString());
                            receiverUser.child("accounts")
                            .child(date)
                            .child(time)
                            .setValue(progT);
                            receiverUser.child("account")
                                    .child(exrec.getSelectedItem().toString())
                            .child("count").get()
                                    .addOnSuccessListener(dataSnapshot2x -> {
                                                double count2 = 0.0d;
                                                if (dataSnapshot2x.exists()) {
                                                    count2 = dataSnapshot2x.getValue(Double.class);
                                                }
                                                count2 += s2;
                                                receiverUser.child("account")
                                                        .child(exrec.getSelectedItem().toString())
                                                        .child("count").setValue(count2);

                                        receiverUser.child("all").get().addOnSuccessListener(command -> {
                                            double count1x = 0.0d;
                                            if (command.exists()) {
                                                count1x = command.getValue(Double.class);
                                            }
                                            count1x += detectCurAllReturnDolar(exrec.getSelectedItem().toString(), s2);
                                            receiverUser.child("all").setValue(count1x);
                                        });

                                            }).addOnFailureListener(e -> noti(activity, e.getLocalizedMessage()));

                    DatabaseReference finalUser = usersReference
                            .child(finalBmw);
                    String finalCountf1 = countf;
                    finalUser.child("accounts")
                            .child(date)
                            .child(String.valueOf(System.currentTimeMillis()))
                            .setValue(progFx).addOnSuccessListener(unused -> finalUser.child("account")
                                    .child(exsend.getSelectedItem().toString())
                                    .child("count").get().addOnSuccessListener(dataSnapshot -> {
                                        double count1 = 0.0d;
                                        if (dataSnapshot.exists()) {
                                            count1 = dataSnapshot.getValue(Double.class);
                                        }
                                        count1 -= doublito(finalCountf1);
                                        finalUser.child("account")
                                                .child(exsend.getSelectedItem().toString())
                                                .child("count").setValue(count1);

                                        finalUser.child("all").get().addOnSuccessListener(command -> {
                                            double count1x = 0.0d;
                                            if (command.exists()) {
                                                count1x = command.getValue(Double.class);
                                            }
                                            count1x += detectCurAllReturnDolar(exsend.getSelectedItem().toString(), -doublito(finalCountf1));
                                            finalUser.child("all").setValue(count1x);
                                        });

                                    }).addOnFailureListener(e -> noti(activity, e.getLocalizedMessage())))

                            .addOnFailureListener(e -> noti(activity, e.getLocalizedMessage()));
                }


            }


            NotificationChannel channel = new NotificationChannel("sy", "sy", IMPORTANCE_HIGH);
            channel.setShowBadge(true);
            channel.enableLights(true);
            channel.setShowBadge(true);
            channel.enableVibration(true);
            channel.setLightColor(getColor(R.color.whjred));

            NotificationCompat.Style style = new NotificationCompat.InboxStyle().addLine(senderx).addLine(recieverx).addLine(s1+"").setBigContentTitle(s2+"");

            NotificationCompat.Action action = new NotificationCompat.Action(R.drawable.shapefxred, "ok", null);
            Intent intent = new Intent(this, AddRestrictionActivity.class);
            intent.setFlags(FLAG_ACTIVITY_CLEAR_TASK|FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,FLAG_IMMUTABLE);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(AddRestrictionActivity.this, "sy");
                        builder.setColor(getColor(R.color.light_blue_600))
                        .setContentTitle(" تم اقتطاع مبلغ " + (-s2) + " " + exrec.getSelectedItem().toString()
                                + " من رصيد العميل " + " " + receiver.getText().toString())
                        .setContentText(" تم اضافة مبلغ " + s1 + " " + exsend.getSelectedItem().toString()
                                + " الى رصيد العميل " + " " + sender.getText().toString())
                        .setAutoCancel(true)
                                .addAction(action)
                                .addPerson(new Person.Builder().setBot(true).setIcon(IconCompat.createWithResource(this,R.drawable.shapefx)).setImportant(true).setName("x").build())
                        .setSmallIcon(R.drawable.shapefxred)
                        .setColorized(true)
                                .setStyle(style)
                        .setVibrate(new long[]{100,200,50,250})
                                .setContentIntent(pendingIntent)
                                .setFullScreenIntent(pendingIntent, true)
                        .build();


                NotificationManager managerCompat = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                managerCompat.createNotificationChannel(channel);
                managerCompat.notify(new Random().nextInt(), builder.build());
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(300);

            blankAll(time+" تمت اضافة القيد ");

        }




        else {
           noti(activity,"اضف مركز مرسل و مركز مستقبل");
        }
    }


    public static double doublito(@NonNull String s) {
        if (s.matches("")) {
            return 0;
        }
        return Double.parseDouble(s);
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
