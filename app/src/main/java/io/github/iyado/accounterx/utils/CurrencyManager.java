package io.github.iyado.accounterx.utils;


import static io.github.iyado.accounterx.AccounterApplication.noti;
import static io.github.iyado.accounterx.MainActivity.databaseReference;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

import io.github.iyado.accounterx.adapters.CurrencyManagerAdapter;
import io.github.iyado.accounterx.inputactivities.AddNewCurrencyActivity;
import io.github.iyado.accounterx.R;


/** @noinspection EmptyMethod*/
public class CurrencyManager extends AppCompatActivity {

    public FloatingActionButton floatingActionButton;
    public RecyclerView recyclerView;
    public CurrencyManagerAdapter adapter;
    public RecyclerView.LayoutManager layoutManager;
    public ArrayList<CurrencyDetails> currencyDetails;
    public static DatabaseReference cursReference;
    TextView textView1,textView2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_manager);
        recyclerView = findViewById(R.id.currrec);

        textView1 = findViewById(R.id.saar);
        textView2 = findViewById(R.id.moaaml);
        floatingActionButton = findViewById(R.id.addcur);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        currencyDetails = new ArrayList<>();
        adapter = new CurrencyManagerAdapter(this,currencyDetails);
        recyclerView.setAdapter(adapter);
        cursReference =  databaseReference.child("currencies");

        showCurrencyDetails();
        textView1.setOnClickListener(v -> addCurrency());
        textView2.setOnClickListener(v -> showCurrencyDetails());
        floatingActionButton.setOnClickListener(v -> addCurrency());


    }
    public void addCurrency(){

        Intent intent = new Intent(CurrencyManager.this, AddNewCurrencyActivity.class);
        startActivity(intent);

    }

    @SuppressWarnings("unused")
   public void deleteCurrency(String cur){

        cursReference.child(cur).removeValue();
    }
    @SuppressWarnings("unused")
    public void updateCurrency(){

    }
    @SuppressWarnings("unused")
    public void searchCurrency(){

    }
    @SuppressWarnings("unused")
    public void showCurrency(){

    }

    public void showCurrencyDetails(){

      cursReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        CurrencyDetails currencyDetailsx;
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            currencyDetailsx = dataSnapshot.getValue(CurrencyDetails.class);
                            currencyDetails.add(currencyDetailsx);
                        }
                        adapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        noti(CurrencyManager.this, error.getMessage());

                    }
                });
    }

}


