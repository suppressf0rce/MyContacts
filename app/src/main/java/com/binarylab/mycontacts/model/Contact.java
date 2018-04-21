package com.binarylab.mycontacts.model;

import java.util.ArrayList;
import java.util.Date;

public class Contact {

    public static final String TABLE_NAME = "contacts";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_SURNAME = "surname";
    public static final String COLUMN_PHONE_NUMBER = "phoneNumber";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_BIRTHDAY = "birthday";
    public static final String COLUMN_NOTES = "notes";
    public static final String COLUMN_FAVORTE = "favorite";

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NAME + " TEXT,"
                    + COLUMN_SURNAME + " TEXT,"
                    + COLUMN_PHONE_NUMBER + " TEXT,"
                    + COLUMN_EMAIL + " TEXT,"
                    + COLUMN_ADDRESS + " TEXT,"
                    + COLUMN_BIRTHDAY + " DATETIME,"
                    + COLUMN_NOTES + " TEXT,"
                    + COLUMN_FAVORTE + " INTEGER"
                    + ")";

    private int id;

    private String name;
    private String surname;
    private String phoneNumber;

    private String email;
    private String address;

    private Date birthday;
    private String notes;

    private boolean isFavorite;

    private ArrayList<Group> groups;

    private Contact(Builder builder){

        id = builder.id;
        name = builder.name;
        surname = builder.surname;
        phoneNumber = builder.phoneNumber;
        email = builder.email;
        address = builder.address;
        birthday = builder.birthday;
        notes = builder.notes;
        isFavorite = builder.isFavorite;
        groups = new ArrayList<>();
    }

    public static class Builder{

        private int id;

        private String name;
        private String surname;
        private String phoneNumber;

        private String email;
        private String address;

        private Date birthday;
        private String notes;
        private boolean isFavorite;

        public Builder(int id){
            this.id = id;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setSurname(String surname) {
            this.surname = surname;
            return this;
        }

        public Builder setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setAddress(String address) {
            this.address = address;
            return this;
        }

        public Builder setBirthday(Date birthday) {
            this.birthday = birthday;
            return this;
        }

        public Builder setNotes(String notes) {
            this.notes = notes;
            return this;
        }

        public Builder setFavorite(boolean favorite){
            this.isFavorite = favorite;
            return this;
        }

        public Contact build(){
            return new Contact(this);
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public Date getBirthday() {
        return birthday;
    }

    public String getNotes() {
        return notes;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public ArrayList<Group> getGroups() {
        return groups;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
