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

    public void setCount(double count) {
        this.count = count;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    @SuppressWarnings("unused")
    public String getCurrencyName() {
        return currencyName;
    }
}
