package com.example.termproject;

import java.util.ArrayList;
import java.util.List;

public class Account {
    public String _id;
    public String name;

    // i <3 storing passwords in plain-text
    public String password;
    public String accountType;

    public static ArrayList<String> classes = new ArrayList<String>();

    public Account(String _id, String name, String password, String accountType) {
        this._id = _id;
        this.name = name;
        this.password = password;
        this.accountType = accountType;
    }

    //no argument constructor for firebase queries
    public Account() {}

    public Account(String _id, String name, String password) {
        this._id = _id;
        this.name = name;
        this.password = password;
    }

    public Account(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_id() {
        return this._id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccountType() {
        return this.accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public ArrayList<String> getClasses() {
        return this.classes;
    }

    public void setClasses(ArrayList<String> classes) {
        this.classes = classes;
    }

    public boolean enrolledInClass(String id) {
        if(this.classes.size()>0) {
            return this.classes.contains(id);
        }

        return false;
    }

    public void addClass(String id) {
        this.classes.add(id);
    }

    public void removeClass(String id) {
        this.classes.remove(id);
    }
}
