package io.github.iyado.accounterx.utils;


import androidx.annotation.NonNull;
import java.io.Serializable;


public class CurrencyDetails implements Serializable {

    private String name;
    private String symbol;
    private String code;
    private double rate;
    private int id;
    private String flag;
    private String color;

    @SuppressWarnings("unused")
    private CurrencyDetails() {
    }

    public CurrencyDetails(int id,String name, String code,String symbol, double rate, String flag,String color) {
        this.name = name;
        this.symbol = symbol;
        this.code = code;
        this.rate = rate;
        this.id = id;
        this.flag = flag;
        this.color = color;
    }
    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    }



    public String getFlag() {
      return flag;
    }
    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getName() {
        return name;
   }
    public String getSymbol() {
        return symbol;
    }
   public String getCode() {
       return code;
    }
    public double getRate() {
        return rate;
    }
    public int getId() {
        return id;
    }

    public void setName(String name) {this.name = name;
    }
   public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public void setRate(double rate) {
        this.rate = rate;
        }
    public void setId(int id) {
        this.id = id;
    }
    @NonNull
    @Override

    public String toString() {
        return "CurrencyDetails{" +
                "name='" + name + '\'' +
                ", symbol='" + symbol + '\'' +
                ", code='" + code + '\'' +
                ", rate='" + rate + '\'' +
                ", id=" + id +
                '}';
    }

}
