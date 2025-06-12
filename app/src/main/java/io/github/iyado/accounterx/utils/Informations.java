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
// --Commented out by Inspection START (12/06/2025 14:15):
//    public void setType(String type) {
//        this.type = type;
//    }
// --Commented out by Inspection STOP (12/06/2025 14:15)
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

// --Commented out by Inspection START (12/06/2025 14:15):
// --Commented out by Inspection START (12/06/2025 14:15):
////    public Map<String, Object> getAccount() {
////        return account;
////    }
//// --Commented out by Inspection STOP (12/06/2025 14:15)
// --Commented out by Inspection STOP (12/06/2025 14:15)


    public void setAccount(Map<String, Object> account) {
        this.account = account;
    }

    @SuppressWarnings("unused")
    public void setAll(double all) {
        this.all = all;
    }
}
