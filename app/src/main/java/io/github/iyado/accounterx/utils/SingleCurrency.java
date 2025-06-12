package io.github.iyado.accounterx.utils;


import java.io.Serializable;


public class SingleCurrency implements Serializable {

    private double count;
    private String currencyName;

    @SuppressWarnings("unused")
    private SingleCurrency(){

    }

    public SingleCurrency(double count){
        this.count = count;
    }

    public double getCount() {
        return count;
    }

// --Commented out by Inspection START (12/06/2025 14:15):
//    public void setCount(double count) {
//        this.count = count;
//    }
// --Commented out by Inspection STOP (12/06/2025 14:15)

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

// --Commented out by Inspection START (12/06/2025 14:15):
//    public String getCurrencyName() {
//        return currencyName;
//    }
// --Commented out by Inspection STOP (12/06/2025 14:15)
}
