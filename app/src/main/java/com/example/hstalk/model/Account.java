package com.example.hstalk.model;

public class Account {
    private int idAccount;
    private String username;
    private String email;
    private String password;
    private String phonenumber;
    private String address;

    public Account(int idAccount, String username, String email, String password, String phonenumber, String address) {
        this.idAccount = idAccount;
        this.username = username;
        this.email = email;
        this.password = password;
        this.phonenumber = phonenumber;
        this.address = address;
    }

    public int getIdAccount() {
        return idAccount;
    }

    public void setIdAccount(int idAccount) {
        this.idAccount = idAccount;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
