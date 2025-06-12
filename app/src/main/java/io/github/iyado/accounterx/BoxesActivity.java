package io.github.iyado.accounterx;


import static io.github.iyado.accounterx.AccounterApplication.noti;
import static io.github.iyado.accounterx.inputactivities.AddRestrictionActivity.doublito;
import static io.github.iyado.accounterx.MainActivity.databaseReference;
import static io.github.iyado.accounterx.MainActivity.insertDataToDatabase;
import static io.github.iyado.accounterx.MainActivity.localCur;
import static io.github.iyado.accounterx.MainActivity.roundD;
import static io.github.iyado.accounterx.MainActivity.seme;
import static io.github.iyado.accounterx.MainActivity.usersReference;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.WindowCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicReference;

import io.github.iyado.accounterx.inputactivities.CurrencyConversationActivity;
import io.github.iyado.accounterx.inputactivities.AddUserActivity;
import io.github.iyado.accounterx.adapters.UsersAdapter;
import io.github.iyado.accounterx.utils.CurrencyDetails;
import io.github.iyado.accounterx.utils.CurrencyManager;
import io.github.iyado.accounterx.utils.Informations;


public class BoxesActivity extends AppCompatActivity {


    public static String KEY = "";
    public RadioButton radioButton;
    public Button dollarp, europ;
    public static String buyingUSD;
    public static String buyingEURO;
    public static String buyingSYP;
    public static HashMap<String, double[]> SUMS;
    BottomNavigationView bottomNavigationView;

    FloatingActionButton updater;
    RecyclerView recyclerView;
    static AlertDialog alertDialog;
    public static List<Informations> users;
    UsersAdapter adapter;
    @SuppressLint("StaticFieldLeak")
    public static UsersAdapter myAdapter;
    SearchView searchView;
    public Handler handler;
    TextView profitv, allProf;
    String profit;
    Intent starterIntent;
    ValueEventListener valueEventListener;


    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_FULLSCREEN);
        WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boxes);
        noti(this,"started....");

        SUMS = new HashMap<>();
        starterIntent = getIntent();

        recyclerView = findViewById(R.id.recyclerView);
        profitv = findViewById(R.id.profit);
        allProf = findViewById(R.id.textView6);
        searchView = findViewById(R.id.search);
        bottomNavigationView = findViewById(R.id.bottomnav);
        dollarp = findViewById(R.id.dollarprice);
        europ = findViewById(R.id.europrice);
        radioButton = findViewById(R.id.radioButton);
        updater = findViewById(R.id.updater);

        handler = new Handler(getMainLooper());


        initView();
        dialog(this);
        updateAll();

        valueEventListener = new ValueEventListener() {

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();

                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    Informations informations = itemSnapshot.getValue(Informations.class);

                    if (informations != null) {
                        informations.setUsername(itemSnapshot.getKey());
                    }

                    users.add(informations);
                }

                adapter.notifyDataSetChanged();
                myAdapter.notifyDataSetChanged();
                loadProfit();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                noti(BoxesActivity.this, error.getMessage());
                dialog(getApplicationContext()).dismiss();
            }

        };

        usersReference.orderByKey().addValueEventListener(valueEventListener);
        alertDialog.dismiss();

        dollarp.setOnClickListener(v -> {
            setOnlinePrice();
            setSYPPrice();
            setSYPpPrice();
            setTRYPrice();
            setTRYyPrice();
            noti(this,getString(R.string.updated));


        });
        dollarp.setOnLongClickListener(v -> {
            setSYPPrice();
            setOnlinePrice();
            setSYPpPrice();
            setTRYPrice();
            noti(this, getString(R.string.updated));
                    return false;
                }
        );
        dollarp.setOnLongClickListener(v -> {
            AlertDialog alertDialog1 = new AlertDialog.Builder(BoxesActivity.this)
                    .setView(R.layout.edit_key)
                    .setCancelable(false)
                    .setIcon(R.drawable.baseline_auto_mode_24)
                    .setMessage("edit key")
                    .create();
            alertDialog1.show();
            EditText editText = alertDialog1.findViewById(R.id.key_area);
            Button button = alertDialog1.findViewById(R.id.okkey);
            assert button != null;
            button.setOnClickListener(v1 -> {
                assert editText != null;
                String key = editText.getText().toString();
                KEY = key;
                SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("key", key);
                editor.apply();
                setOnlinePrice();
                setSYPpPrice();
                setSYPPrice();
                setTRYPrice();
                setTRYyPrice();
                alertDialog1.dismiss();
            });
            return true;
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return true;
            }
        });

        updater.setOnClickListener(v -> {
            updateAll();
            noti(this,getString(R.string.updated));

                });

        allProf.setOnClickListener(v -> {
            zero();
            startActivity(new Intent(this, DetailActivity.class));
        });

        allProf.setOnLongClickListener(v -> {

            dot();
            return true;
        });

        bottomNavigationView.setOnItemSelectedListener(item -> {
            noti(this, Objects.requireNonNull(item.getTitle()).toString());
            if (item.getItemId() == R.id.logout) {
                findViewById(R.id.include).setVisibility(View.VISIBLE);
            }
            return true;
        });

        bottomNavigationView.setOnItemReselectedListener(item -> {

            if (item.getItemId() == R.id.cut) {
               // dot();
                startActivity(new Intent(this, CurrencyManager.class));
            }
            if (item.getItemId() == R.id.logout) {
                FirebaseAuth.getInstance().signOut();
                finish();
            }
            if (item.getItemId() == R.id.doadduser) {
                Intent intent = new Intent(this, AddUserActivity.class);
                startActivity(intent);
            }
            if (item.getItemId() == R.id.docut) {
                dot();

                startActivity(new Intent(this, CurrencyConversationActivity.class));
            }
            if (item.getItemId() == R.id.doprint) {
                startActivity(new Intent(this, PrintUserDetailsActivity.class));
            }

        });


        profitv.setOnClickListener(v -> insertDataToDatabase(this));
        profitv.setOnLongClickListener(v -> {
            startActivity(new Intent(this,CurrencyManager.class));
            return true;
        });


        zero();
    }

    private void updateAll() {

        new Timer().schedule(new TimerTask() {
                                 @Override
                                 public void run() {
                                     handler.post(new Delayer());
                                 }


                             }
                , 0, 60000);
    }

    public class Delayer implements Runnable {

        @Override
        public void run() {
            setSYPPrice();
            setOnlinePrice();
            setSYPpPrice();
            setTRYPrice();
            setTRYyPrice();
        }
    }


    private void setSYPpPrice(){
        databaseReference.child("currencies").child("ليرة سورية")
                .child("rate")
                .get().addOnSuccessListener(dataSnapshot2 -> {
                    if (dataSnapshot2.exists()) {
                        //noinspection DataFlowIssue
                        double count = dataSnapshot2.getValue(Double.class);
                        runOnUiThread(() -> europ.setText(String.valueOf(roundD(count))));
                    } else {
                        runOnUiThread(() -> europ.setText("0"));

                    }

                });
    }

    private void setTRYyPrice(){
        DatabaseReference databaseReference1 =  usersReference.child("مركز قطع ليرة تركية")
                .child("account");
        databaseReference1.child("دولار أمريكي")
                .child("count").get().addOnSuccessListener(dataSnapshot -> {
                    if (dataSnapshot.exists()) {

                        //noinspection DataFlowIssue
                        double countD = dataSnapshot.getValue(Double.class);
                        databaseReference1.child("ليرة تركية")
                                .child("count").get().addOnSuccessListener(dataSnapshot1 -> {
                                    if (dataSnapshot1.exists()) {

                                        //noinspection DataFlowIssue
                                        double countSY = dataSnapshot1.getValue(Double.class);
                                        if (countSY != 0 && countD != 0) {
                                            double count = countSY / countD;
                                            databaseReference
                                                    .child("currencies")
                                                    .child("ليرة تركية")
                                                    .child("rate").setValue(roundD(Math.abs(count)));
                                        }
                                    }

                                });
                    }

                });
    }

    private void setSYPPrice(){
       DatabaseReference databaseReference1 =  usersReference.child("مركز قطع ليرة سورية")
                .child("account");
       databaseReference1.child("دولار أمريكي")
               .child("count").get().addOnSuccessListener(dataSnapshot -> {
                   if (dataSnapshot.exists()) {

                       //noinspection DataFlowIssue
                       double countD = dataSnapshot.getValue(Double.class);
                       databaseReference1.child("ليرة سورية")
                               .child("count").get().addOnSuccessListener(dataSnapshot1 -> {
                                   if (dataSnapshot1.exists()) {

                                       //noinspection DataFlowIssue
                                       double countSY = dataSnapshot1.getValue(Double.class);
                                       if (countSY != 0 && countD != 0) {
                                           double count = countSY / countD;
                                          databaseReference
                                                   .child("currencies")
                                                   .child("ليرة سورية")
                                                   .child("rate").setValue(roundD(Math.abs(count)));
                                       }
                                       }

                               });
                   }

               });
    }
    private void setTRYPrice(){
       databaseReference.child("currencies")
                .child("ليرة تركية")
                .child("rate")
                .get().addOnSuccessListener(dataSnapshot -> {
                    if (dataSnapshot.exists()) {
                        //noinspection DataFlowIssue
                        double count = dataSnapshot.getValue(Double.class);

                        runOnUiThread(() -> dollarp.setText(String.valueOf(roundD(count))));

                    } else {
                        runOnUiThread(() -> dollarp.setText("0"));

                    }

                }
                );


    }
    private void setOnlinePrice() {

        AtomicReference<JSONObject> jsonObject = new AtomicReference<>();
        AtomicReference<JSONObject> jsonObject1 = new AtomicReference<>(new JSONObject());
        Thread thread = new Thread(() -> {
            try {

                jsonObject.set(dotl());
                try {
                    String h = "";
                    if (jsonObject.get() != null) {
                        jsonObject.get().get("data");
                        h = jsonObject.get().get("data").toString().replace("[", "").replace("]", "");

                    }
                    jsonObject1.set(new JSONObject(h));
                } catch (JSONException e) {
                    //runOnUiThread(() -> noti(this, e.getMessage()));
                }

                runOnUiThread(() -> {
                            try {

                                JSONArray jsonArray = jsonObject.get().getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                                    if (jsonObject2.getString("code").equals("USD")) {
                                        FirebaseDatabase.getInstance().getReference()
                                                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                                                .child("currencies")
                                                .child("ليرة تركية")
                                                .child("rate").setValue(doublito(jsonObject2.get("buying").toString()));
                                    }

                                    jsonObject2.getString("code");//  europ.setText(jsonObject2.get("buying").toString());


                                }


                            } catch (JSONException e) {
                               // runOnUiThread(() -> noti(this, e.getMessage()));
                            }

                        }
                );
            } catch (IOException e) {
              //  runOnUiThread(() -> noti(this, e.getMessage()));

            }
        });
        thread.start();


    }


    private void dot() {

        AtomicReference<JSONObject> jsonObject = new AtomicReference<>();
        AtomicReference<JSONObject> jsonObject1 = new AtomicReference<>(new JSONObject());
        Thread thread = new Thread(() -> {
            try {

                jsonObject.set(dotl());
                try {
                    String h = "";
                    if (jsonObject.get() != null) {
                        jsonObject.get().get("data");
                        h = jsonObject.get().get("data").toString().replace("[", "").replace("]", "");

                    }
                    jsonObject1.set(new JSONObject(h));
                } catch (JSONException e) {
                    runOnUiThread(() -> noti(this, e.getMessage()));
                }

                runOnUiThread(() -> {
                            try {


                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("cut");
                                JSONArray jsonArray = jsonObject.get().getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                                    if (jsonObject2.getString("code").equals("USD")) {
                                        buyingUSD = jsonObject2.get("buying").toString();

                                    }

                                    if (jsonObject2.getString("code").equals("EUR")) {

                                        buyingEURO = jsonObject2.get("buying").toString();
                                    }
                                    if (jsonObject2.getString("code").equals("SYP")) {
                                        buyingSYP = jsonObject2.get("buying").toString();
                                    }

                                }

                                double x = doublito(buyingEURO) / doublito(buyingUSD);

                                reference.child("tl").setValue(doublito(buyingUSD));
                                FirebaseDatabase.getInstance().getReference().child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                                        .child("currencies").child("ليرة تركية").child("rate").setValue(doublito(buyingUSD));


                                reference.child("euro").setValue((double) Math.round(x * 1000) / 1000);

                                reference.child("syp").setValue(Math.round(1000 / doublito(buyingSYP)) / 1000);


                            } catch (JSONException e) {
                                noti(this, e.getMessage());
                            }

                        }
                );
            } catch (IOException e) {
                runOnUiThread(() -> noti(this, e.getMessage()));

            }
        });
        thread.start();


    }


    private JSONObject dotl() throws IOException {


        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        KEY = sharedPreferences.getString("key", "");
        JSONObject jsonObject = null;
        String path = "https://www.nosyapi.com/apiv2/service/economy/currency/exchange-rate?apiKey="+KEY;

        URL url = new URL(path);


        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

        InputStream inputStream = httpURLConnection.getInputStream();
        StringBuilder stringBuilder = new StringBuilder();

        //noinspection ResultOfMethodCallIgnored
        inputStream.read();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        bufferedReader.close();
        try {

            jsonObject = new JSONObject("{" + stringBuilder + "}");

        } catch (JSONException e) {
            noti(this, e.getMessage());
        }
        inputStream.close();
        httpURLConnection.disconnect();
        return jsonObject;

    }


    // load profit to view;
    private void loadProfit() {

        double d = 0.0d;

        for (Informations informations: users) {

            if (informations != null) {
                d = d + informations.getAll();
            }

        }


        double rere = roundD(d);

        profit = String.valueOf(rere);
        profitv.setText(profit);

        if (rere >= 0.1){
            profitv.setTextColor(Color.GREEN);
        }else if (rere <= -0.1){
            profitv.setTextColor(Color.RED);
        }else {
            profitv.setTextColor(Color.BLUE);
        }
    }

    // set profit to zero;
    private void zero(){
        if (!profitv.getText().toString().isEmpty()) {
            double balance = Double.parseDouble(profitv.getText().toString());
            for (CurrencyDetails currencyDetails : localCur.values()){
                if (currencyDetails.getRate()==1){
                    seme(this,currencyDetails.getName(),-balance);
                }
            }
        }
    }


// init dialog;
    @NonNull
    public static AlertDialog dialog(@NonNull Context context){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setView(android.R.layout.simple_list_item_1);
        alertDialog = builder.create();

        return alertDialog;
    }

    // init views
    private void initView(){

        searchView.clearFocus();
        users = new ArrayList<>();

        adapter = new UsersAdapter(this,this, users);
        myAdapter = adapter;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
    }


    @SuppressLint("NotifyDataSetChanged")
    public void searchList(String text){
        ArrayList<Informations> searchList = new ArrayList<>();
        for (Informations informations: users){
            if (informations.getUid()!= null && informations.getUid().toLowerCase().contains(text.toLowerCase()) ||
                    informations.getUsername().toLowerCase().contains(text.toLowerCase()) ||
                    informations.getFullname()!= null && !informations.getFullname().trim().isEmpty() &&
            informations.getFullname().toLowerCase().contains(text.toLowerCase())){
                searchList.add(informations);
            }
        }
        adapter.searchDataList(searchList);

    }



}