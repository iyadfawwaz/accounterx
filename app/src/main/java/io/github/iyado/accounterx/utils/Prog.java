package io.github.iyado.accounterx.utils;

import java.io.Serializable;

public class Prog implements Serializable {
    private String notice;
    private String customer;
    private String sender;
    private String reciever;
    private double sprofit;
    private double rprofit;
    private String ex;
    private String name;
    private double count;
    private double sumAll;
    private String key;
    private String date;
    private boolean isChecked;
    @SuppressWarnings("unused")
    private Prog(){

    }

    public Prog(String sender,String reciever,double count,
                String ex, double sprofit,double rprofit,
                String customer,String notice,double sumAll,
                String key,String date) {
        this.date = date;
        this.key = key;
        this.count = count;
        this.ex = ex;
        this.notice = notice;
        this.customer = customer;
        this.reciever = reciever;
        this.sender = sender;
        this.rprofit = rprofit;
        this.sprofit = sprofit;
        this.sumAll = sumAll;
        this.isChecked = false;
    }


    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @SuppressWarnings("unused")
    public void setEx(String ex) {
        this.ex = ex;
    }

    @SuppressWarnings("unused")
    public void setNotice(String notice) {
        this.notice = notice;
    }

    @SuppressWarnings("unused")
    public String getNotice() {
        return notice;
    }


    public String getEx() {
        return ex;
    }

    public String getSender() {
        return sender;
    }

    @SuppressWarnings("unused")
    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReciever() {
        return reciever;
    }

    @SuppressWarnings("unused")
    public void setReciever(String reciever) {
        this.reciever = reciever;
    }

    public String getCustomer() {
        return customer;
    }

    @SuppressWarnings("unused")
    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getKey() {
        return key;
    }

    @SuppressWarnings("unused")
    public void setKey(String key) {
        this.key = key;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @SuppressWarnings("unused")
    public double getSprofit() {
        return sprofit;
    }

    @SuppressWarnings("unused")
    public void setSprofit(double sprofit) {
        this.sprofit = sprofit;
    }

    @SuppressWarnings("unused")
    public double getRprofit() {
        return rprofit;
    }

    @SuppressWarnings("unused")
    public void setRprofit(double rprofit) {
        this.rprofit = rprofit;
    }


    public double getCount() {
      return count;
  }


    public void setCount(double count) {

        this.count = count;
    }

    public double getSumAll() {
        return sumAll;
   }
    @SuppressWarnings("unused")
   public void setSumAll(double sumAll) {

        this.sumAll = sumAll;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
