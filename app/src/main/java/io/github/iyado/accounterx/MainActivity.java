package io.github.iyado.accounterx;


import static io.github.iyado.accounterx.AccounterApplication.noti;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

import io.github.iyado.accounterx.inputactivities.AddRestrictionActivity;
import io.github.iyado.accounterx.inputactivities.CurrencyConversationActivity;
import io.github.iyado.accounterx.inputactivities.AddUserActivity;
import io.github.iyado.accounterx.utils.CurrencyDetails;
import io.github.iyado.accounterx.utils.CurrencyManager;
import io.github.iyado.accounterx.utils.LocalUsers;
import io.github.iyado.accounterx.utils.Prog;
import io.github.iyado.accounterx.utils.SingleCurrency;


public class MainActivity extends AppCompatActivity {


    @SuppressLint("NotifyDataSetChanged")
    public static void insertDataToDatabase(@NonNull AppCompatActivity context) {


        usersReference.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot users : snapshot.getChildren()) {
                            String user = users.getKey();
                            if (user != null && !user.isEmpty() && user.length() > 1) {
                                privateInsert(context,users);

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        System.out.println(error.getMessage());
                        noti(context, error.getMessage());
                    }
                });
    }

    public static void privateInsert(@NonNull AppCompatActivity context,@NonNull DataSnapshot users){

        DatabaseReference reference = users
                .getRef();
        reference.child("accounts")
                .get().addOnSuccessListener(command -> {
                    double[] all0 = new double[localCur.size()];
                    if (!command.exists()) {
                        for (int i = 0; i < localCur.size(); i++) {
                            reference.child("account")
                                    .child(Objects.requireNonNull(localCur.get(Objects.requireNonNull(localCur.get(i)).getId())).getName())
                                    .setValue(new SingleCurrency(0));
                        }
                    }else {
                        command.getRef().addListenerForSingleValueEvent(new ValueEventListener() {


                            @SuppressLint("NotifyDataSetChanged")
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {



                                for (int i = 0; i < localCur.size(); i++) {
                                    all0[i] = 0.0d;
                                }

                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                    double[] all = new double[localCur.size()];
                                    for (int i = 0; i < localCur.size(); i++) {
                                        all[i] = 0.0d;
                                    }

                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                                        if (dataSnapshot1.exists()) {
                                            Prog xp = dataSnapshot1.getValue(Prog.class);
                                            if (xp != null && xp.getEx()!=null) {
                                                for (int i = 0; i < localCur.size(); i++) {
                                                    if (xp.getEx().equals(Objects.requireNonNull(localCur.get(i)).getName())) {
                                                        all0[Objects.requireNonNull(localCur.get(i)).getId()] += xp.getSumAll();
                                                    }
                                                }
                                            }

                                        }

                                    }

                                    for (int i = 0; i < localCur.size(); i++) {
                                        all0[i] += all[i];


                                        reference.child("account")
                                                .child(Objects.requireNonNull(localCur.get(Objects.requireNonNull(localCur.get(i)).getId())).getName())
                                                .setValue(new SingleCurrency(roundD(all0[Objects.requireNonNull(localCur.get(i)).getId()])));
                                    }
                                    reference.child("all")
                                            .setValue(sumallitemsindouble(all0));
                                }



                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                               noti(context,error.getMessage());
                            }
                        });
                    }
                    reference.child("all")
                            .setValue(roundD(sumallitemsindouble(all0)));
                });


    }

    public void insertInfo(AppCompatActivity context) {

        ArrayList<String> arrayListxx = new ArrayList<>();

        localUsers = new LocalUsers();

        usersReference.addChildEventListener(new ChildEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                arrayListxx.add(snapshot.getKey());
                localUsers.setUsernamess(arrayListxx);

            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                arrayListxx.remove(snapshot.getKey());
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                noti(context, error.getMessage());
            }
        });

    }

    /** @noinspection DataFlowIssue*/
    public static void seme(AppCompatActivity context, String currency, double count) {

        long yourmilliseconds = System.currentTimeMillis();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy");
        Date resultdate = new Date(yourmilliseconds);

        String time = String.valueOf(yourmilliseconds);
        String date = sdf.format(resultdate);

        Prog progX = new Prog("قص عملة", "قص عملة", count,
                currency,
                0, 0, "فروق صرف", "فروق صرف", count,
                time, date);

        if (count != 0) {
            DatabaseReference thisUser = usersReference.child("قيود محققة");
                    thisUser.child("accounts")
                    .child(date)
                    .child(time)
                    .setValue(progX)
                    .addOnCompleteListener(runnable1 -> {
                        if (runnable1.isSuccessful()) {
                            thisUser.child("account")
                                            .child(currency).child("count")
                                            .get().addOnSuccessListener(command -> {

                                                double x = 0.0d;
                                                if (command.exists()) {
                                                    x = command.getValue(Double.class);
                                                }
                                                x += count;
                                                thisUser.child("account")
                                                        .child(currency).child("count")
                                                        .setValue(x);

                                        thisUser.child("all").get().addOnSuccessListener(commandx -> {
                                            double count1x = 0.0d;
                                            if (commandx.exists()) {
                                                count1x = commandx.getValue(Double.class);
                                            }
                                            count1x += detectCurAllReturnDolar(currency, count);
                                            thisUser.child("all").setValue(count1x);
                                        });
                                            }).addOnFailureListener(e -> noti(context, e.getLocalizedMessage()));
                            noti(context, "تمت العملية ");

                        }
                    });
        }

    }

    @NonNull
    public static String stringo(double x){
        return String.valueOf(x);
    }
    public static double roundD(double d){
        return (double) Math.round(d * 100) /100;
    }
    public static double sumallitemsindouble(@NonNull double[] x) {
        double n=0.0d;
        for (int i = 0; i < x.length; i++) {
            if (Objects.requireNonNull(localCur.get(Objects.requireNonNull(localCur.get(i)).getId())).getSymbol().equals("*")) {
                if (Objects.requireNonNull(localCur.get(Objects.requireNonNull(localCur.get(i)).getId())).getRate() != 0) {
                    n += x[i] * Objects.requireNonNull(localCur.get(Objects.requireNonNull(localCur.get(i)).getId())).getRate();
                }
            } else {
                n += x[i] / Objects.requireNonNull(localCur.get(Objects.requireNonNull(localCur.get(i)).getId())).getRate();
            }
        }
        return roundD(n);
    }

    public static double detectCurAllReturnDolar(@NonNull String currencyName, double convertToDolar) {

        CurrencyDetails currencyDetails = localCurByName.get(currencyName);
        assert currencyDetails != null;
        String symbol = currencyDetails.getSymbol();
        double rate = currencyDetails.getRate();
        double n=0.0d;

            if (symbol.equals("*")) {
                if (rate != 0) {
                    n = convertToDolar * currencyDetails.getRate();
                }
            } else {
                if (rate != 0) {
                    n = convertToDolar / currencyDetails.getRate();
                }
            }

        return roundD(n);
    }

    Button addres,adduser,cut,print,boxes,ress,logout,details,currencies;
    ValueEventListener cursEventListener;
    public static HashMap<Integer, CurrencyDetails> localCur;
    public static HashMap<String,CurrencyDetails> localCurByName;
    public static DatabaseReference curReference;
    public static DatabaseReference databaseReference;
    public static DatabaseReference usersReference;
    public static String COMPANY_NAME;
    public static LocalUsers localUsers;
    RadioButton radioButton;
    LinearLayout whj;
    public static Vibrator vibrator;

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);
        addres=findViewById(R.id.addresfx);
        adduser=findViewById(R.id.adduserfx);
        cut=findViewById(R.id.cutfx);
        print=findViewById(R.id.printerfx);
        boxes=findViewById(R.id.jobfx);
        ress=findViewById(R.id.ressfx);
        logout=findViewById(R.id.logoutfx);
        details = findViewById(R.id.detailsfx);
        currencies =findViewById(R.id.currenciesfx);

        radioButton = findViewById(R.id.radioButton2);
        whj = findViewById(R.id.whj);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);




        requestPermissions(new String[]{Manifest.permission.READ_MEDIA_VIDEO, Manifest.permission.READ_CONTACTS, Manifest.permission.POST_NOTIFICATIONS,
                Manifest.permission.ACCESS_NOTIFICATION_POLICY, Manifest.permission.ACCESS_MEDIA_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION}, 201);

        HashMap<Integer, CurrencyDetails> hashMap = new HashMap<>();
        localCur = new HashMap<>();
        localCurByName = new HashMap<>();
        HashMap<String,CurrencyDetails> ccx = new HashMap<>();
        initView();
        insertInfo(this);
        //insertDataToDatabase(this);

        cursEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                hashMap.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.exists()) {
                        CurrencyDetails currencyDetails = dataSnapshot.getValue(CurrencyDetails.class);
                        assert currencyDetails != null;
                        hashMap.put(currencyDetails.getId(), currencyDetails);
                        ccx.put(currencyDetails.getName(),currencyDetails);

                    }

                }

                localCur.putAll(hashMap);
                localCurByName.putAll(ccx);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                noti(MainActivity.this,error.getMessage());

            }
        };

        curReference.addValueEventListener(cursEventListener);

        addres.setOnClickListener(v -> {
            vibrator.vibrate(100);
            startActivity(new Intent(MainActivity.this, AddRestrictionActivity.class));
        });

        adduser.setOnClickListener(v ->
        {
            vibrator.vibrate(100);
            startActivity(new Intent(MainActivity.this, AddUserActivity.class));
        });

        cut.setOnClickListener(v ->
        {
            vibrator.vibrate(100);
            startActivity(new Intent(MainActivity.this, CurrencyConversationActivity.class));
        });

        boxes.setOnClickListener(v ->
        {
            vibrator.vibrate(100);
            startActivity(new Intent(MainActivity.this, BoxesActivity.class));
        });

        ress.setOnClickListener(v ->
        {
            vibrator.vibrate(100);
            startActivity(new Intent(MainActivity.this, RestrictionPrintActivity.class));
        });

        currencies.setOnClickListener(v ->
        {
            vibrator.vibrate(100);
            startActivity(new Intent(MainActivity.this, CurrencyManager.class));
        });
        details.setOnClickListener(v ->
        {
            vibrator.vibrate(100);
            startActivity(new Intent(MainActivity.this, DetailActivity.class));
        });
        logout.setOnClickListener(v -> {
            vibrator.vibrate(100);
                    FirebaseAuth.getInstance().signOut();
                    finish();

                });

        print.setOnClickListener(v ->
        {
            vibrator.vibrate(100);
            startActivity(new Intent(MainActivity.this, PrintUserDetailsActivity.class));
        });

        radioButton.setOnClickListener(v ->
        {
            vibrator.vibrate(100);
            insertDataToDatabase(this);
        });

        FirebaseDatabase.getInstance().getReference(".info/connected")
                .addValueEventListener(new ValueEventListener() {
                    @SuppressLint("UseCompatLoadingForDrawables")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Boolean connected = snapshot.getValue(Boolean.class);
                        if (Boolean.TRUE.equals(connected)) {
                            runOnUiThread(() -> {
                                radioButton.setButtonTintList(ColorStateList.valueOf(getColor(R.color.greenfx)));
                                whj.setBackground(getDrawable(R.drawable.shapefx));

                            });
                        }else {
                            runOnUiThread(() -> {
                                radioButton.setButtonTintList(ColorStateList.valueOf(getColor(R.color.red)));
                                whj.setBackground(getDrawable(R.drawable.shapefxred));
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        noti(MainActivity.this,getString(R.string.u_r_not_connected)+error.getMessage());
                    }
                });

    }
    private void initView(){

        localCur = new HashMap<>();

        if (getIntent().getStringExtra("cname") != null){
            COMPANY_NAME = getIntent().getStringExtra("cname");
        }else {
            if (FirebaseAuth.getInstance().getCurrentUser()!= null) {
                COMPANY_NAME = FirebaseAuth.getInstance().getCurrentUser().getUid();
            }
        }

        databaseReference = FirebaseDatabase.getInstance().getReference().child(COMPANY_NAME);

        usersReference = databaseReference.child("users");
        curReference = databaseReference.child("currencies");


        // @insert date to database if not exists;
        databaseReference.child("email")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @SuppressLint("SimpleDateFormat")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.exists()){
                            databaseReference.child("email")
                                    .setValue(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail());
                            databaseReference.child("date")
                                    .setValue(new SimpleDateFormat("yyyy:MM").format(new Date(System.currentTimeMillis())));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

}
