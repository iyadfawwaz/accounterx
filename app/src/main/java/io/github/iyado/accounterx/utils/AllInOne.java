package io.github.iyado.accounterx.utils;


import static io.github.iyado.accounterx.MainActivity.localCur;

import androidx.annotation.NonNull;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import io.github.iyado.accounterx.methods.GetCurrenceis;


public class AllInOne {


    public static HashMap<Integer, CurrencyDetails> currencyDetailsHashMap;
    public static ArrayList<CurrencyDetails> cursName;
    public static HashMap<String,String> sss;
    public static HashMap<String,Double> xxx;

    public static HashMap<Integer,CurrencyDetails> hashMap;
    public static Taskx taskx;
    private AllInOne(){
        taskx = new Taskx();
    }

    public static ArrayList<CurrencyDetails> getCursName() {
        cursName.addAll(localCur.values());
        return cursName;
    }

    public static void setSss(HashMap<String, String> sss) {
        AllInOne.sss = sss;
    }

    @NonNull
    public static HashMap<String,String> initSymbol(){

        currencyDetailsHashMap = loadCurrencies();
        sss = new HashMap<>();
            for (CurrencyDetails currencyDetails : currencyDetailsHashMap.values()) {
                sss.put(currencyDetails.getCode(), currencyDetails.getSymbol());
            }
            setSss(sss);

        return sss;
    }

    public static HashMap<String, Double> initCutPrice() {
        xxx = new HashMap<>();
        for (CurrencyDetails currencyDetails : localCur.values()) {
            xxx.put(currencyDetails.getCode(),currencyDetails.getRate());
        }
        return xxx;
    }

    public static void setCurrencyDetailsHashMap(HashMap<Integer, CurrencyDetails> currencyDetailsHashMap) {
        AllInOne.currencyDetailsHashMap = currencyDetailsHashMap;
    }

    public static HashMap<Integer, CurrencyDetails> loadCurrencies() {
        return localCur;
    }

    public static void setXxx(HashMap<String, Double> xxx) {
        AllInOne.xxx = xxx;
    }

    public static Taskx taskxx(){
        //noinspection InstantiationOfUtilityClass
        new AllInOne();
        hashMap = new HashMap<>();
        FirebaseDatabase.getInstance().getReference()
                .child("currencies")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            if (dataSnapshot.exists()){
                                CurrencyDetails currencyDetails = dataSnapshot.getValue(CurrencyDetails.class);
                                assert currencyDetails != null;
                                hashMap.put(currencyDetails.getId(),currencyDetails);
                            }
                        }
                        taskx.setHashMap(hashMap);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        taskx.setError(error.toException());
                    }
                });
        return taskx;
    }



public static class Taskx {
    private HashMap<Integer, CurrencyDetails> hashMap;

    private Exception error;

    public Taskx() {

    }

    public Exception getError() {
        return error;
    }

    public HashMap<Integer, CurrencyDetails> getHashMap() {
        return hashMap;
    }

    public void setError(Exception error) {
        this.error = error;
    }

    public void setHashMap(HashMap<Integer, CurrencyDetails> hashMap) {
        this.hashMap = hashMap;
    }

    public void addTaskxListener(GetCurrenceis listener) {
        if (hashMap != null) {
            listener.getCurrency(hashMap);
        } else {
            if (error != null) {
                listener.getError(error);
            }
        }

    }
}
}
