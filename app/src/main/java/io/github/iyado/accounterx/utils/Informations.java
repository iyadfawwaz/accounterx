package io.github.iyado.accounterx.utils;


import java.io.Serializable;
import java.util.Map;


public class Informations implements Serializable {

    private String uid;
    private Map<String,Object> account;
    private String username;
    private String type;
    private String fullname;
    private Map<String,Object> accounts;
    private double all;

    @SuppressWarnings("unused")
    public Informations(String uid, String type, String fullname) {
        this.uid = uid;
        this.type = type;
        this.fullname = fullname;
    }

    public double getAll() {
        return all;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUid() {
        return uid;
    }

   public String getType() {
        return type;
    }

    public String getFullname() {
        return fullname;
    }

    @SuppressWarnings("unused")
    public void setUid(String uid) {
        this.uid = uid;
    }
    public void setType(String type) {
        this.type = type;
    }
    @SuppressWarnings("unused")
    public void setFullname(String fullname) {
        this.fullname = fullname;
        }

        @SuppressWarnings("unused")
    public Map<String, Object> getAccounts() {
       return accounts;
    }

    @SuppressWarnings("unused")
    public void setAccounts(Map<String, Object> accounts) {
       this.accounts = accounts;
    }

    public Map<String, Object> getAccount() {
        return account;
    }


    public void setAccount(Map<String, Object> account) {
        this.account = account;
    }

    @SuppressWarnings("unused")
    public void setAll(double all) {
        this.all = all;
    }
}
