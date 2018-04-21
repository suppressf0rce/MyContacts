package com.binarylab.mycontacts.model;

import java.util.ArrayList;

public class Group {

    public static final String TABLE_NAME = "groups";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NAME + " TEXT"
                    + ")";

    public static final String CREATE_CONTACT_GROUPS =
            "CREATE TABLE contact_groups("
                    + "contactid INTEGER,"
                    + "groupid INTEGER,"
                    + "FOREIGN KEY(contactid) REFERENCES "+Contact.TABLE_NAME+"("+Contact.COLUMN_ID+"),"
                    + "FOREIGN KEY(groupid) REFERENCES "+TABLE_NAME+"("+COLUMN_ID+")"
                    + ")";


    private int id;
    private String name;

    private ArrayList<Contact> contacts;

    public Group(int id, String name){
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Contact> getContacts() {
        return contacts;
    }
}
