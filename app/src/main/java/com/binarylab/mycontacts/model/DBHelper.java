package com.binarylab.mycontacts.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "GIMLI";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create tables
        db.execSQL(Contact.CREATE_TABLE);
        db.execSQL(Group.CREATE_TABLE);
        db.execSQL(Group.CREATE_CONTACT_GROUPS);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS contact_groups");
        db.execSQL("DROP TABLE IF EXISTS " + Contact.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Group.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }


    public long insertContact(Contact contact){
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        // `id` will be inserted automatically.
        // no need to add them
        values.put(Contact.COLUMN_NAME, contact.getName());
        values.put(Contact.COLUMN_SURNAME, contact.getSurname());
        values.put(Contact.COLUMN_PHONE_NUMBER, contact.getPhoneNumber());
        values.put(Contact.COLUMN_EMAIL, contact.getEmail());
        values.put(Contact.COLUMN_ADDRESS, contact.getAddress());
        values.put(Contact.COLUMN_FAVORTE, contact.isFavorite()?1:0);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(contact.getBirthday() == null)
            values.put(Contact.COLUMN_BIRTHDAY, "NULL");
        else
            values.put(Contact.COLUMN_BIRTHDAY, dateFormat.format(contact.getBirthday()));
        values.put(Contact.COLUMN_NOTES, contact.getNotes());

        // insert row
        long id = db.insert(Contact.TABLE_NAME, null, values);

        //Insert group if exist
        for(Group group:contact.getGroups()){
            values = new ContentValues();
            values.put("contactid", id);
            values.put("groupid", group.getId());
            db.insert("user_groups",null, values);
        }

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public Contact getContact(int id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Contact.TABLE_NAME,
                new String[]{Contact.COLUMN_ID, Contact.COLUMN_NAME, Contact.COLUMN_SURNAME,
                        Contact.COLUMN_PHONE_NUMBER, Contact.COLUMN_EMAIL, Contact.COLUMN_ADDRESS,
                        Contact.COLUMN_BIRTHDAY, Contact.COLUMN_NOTES, Contact.COLUMN_FAVORTE},
                Contact.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare contact object
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateTime = cursor.getString(cursor.getColumnIndex(Contact.COLUMN_BIRTHDAY));
        Date date;
        try {
            date = dateFormat.parse(dateTime);
        } catch (ParseException ignored) {
            date = null;
        }
        Contact contact = new Contact.Builder(cursor.getInt(cursor.getColumnIndex(Contact.COLUMN_ID)))
                .setName(cursor.getString(cursor.getColumnIndex(Contact.COLUMN_NAME)))
                .setSurname(cursor.getString(cursor.getColumnIndex(Contact.COLUMN_SURNAME)))
                .setPhoneNumber(cursor.getString(cursor.getColumnIndex(Contact.COLUMN_PHONE_NUMBER)))
                .setEmail(cursor.getString(cursor.getColumnIndex(Contact.COLUMN_EMAIL)))
                .setAddress(cursor.getString(cursor.getColumnIndex(Contact.COLUMN_ADDRESS)))
                .setNotes(cursor.getString(cursor.getColumnIndex(Contact.COLUMN_NOTES)))
                .setFavorite(cursor.getInt(cursor.getColumnIndex(Contact.COLUMN_FAVORTE))!=0)
                .setBirthday(date).build();

        // close the db connection
        cursor.close();

        return contact;
    }

    public List<Contact> getAllContacts() {
        List<Contact> contacts = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Contact.TABLE_NAME + " ORDER BY " +
                Contact.COLUMN_NAME + " ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateTime = cursor.getString(cursor.getColumnIndex(Contact.COLUMN_BIRTHDAY));
                Date date;
                try {
                    date = dateFormat.parse(dateTime);
                } catch (ParseException ignored) {
                   date = null;
                }
                Contact contact = new Contact.Builder(cursor.getInt(cursor.getColumnIndex(Contact.COLUMN_ID)))
                        .setName(cursor.getString(cursor.getColumnIndex(Contact.COLUMN_NAME)))
                        .setSurname(cursor.getString(cursor.getColumnIndex(Contact.COLUMN_SURNAME)))
                        .setPhoneNumber(cursor.getString(cursor.getColumnIndex(Contact.COLUMN_PHONE_NUMBER)))
                        .setEmail(cursor.getString(cursor.getColumnIndex(Contact.COLUMN_EMAIL)))
                        .setAddress(cursor.getString(cursor.getColumnIndex(Contact.COLUMN_ADDRESS)))
                        .setNotes(cursor.getString(cursor.getColumnIndex(Contact.COLUMN_NOTES)))
                        .setFavorite(cursor.getInt(cursor.getColumnIndex(Contact.COLUMN_FAVORTE))!=0)
                        .setBirthday(date).build();

                contacts.add(contact);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return contacts list
        return contacts;
    }

    public List<Contact> getFavoriteConntacts() {
        List<Contact> contacts = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Contact.TABLE_NAME + " WHERE "+Contact.COLUMN_FAVORTE+"=1 ORDER BY " +
                Contact.COLUMN_NAME + " ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateTime = cursor.getString(cursor.getColumnIndex(Contact.COLUMN_BIRTHDAY));
                Date date;
                try {
                    date = dateFormat.parse(dateTime);
                } catch (ParseException ignored) {
                    date = null;
                }
                Contact contact = new Contact.Builder(cursor.getInt(cursor.getColumnIndex(Contact.COLUMN_ID)))
                        .setName(cursor.getString(cursor.getColumnIndex(Contact.COLUMN_NAME)))
                        .setSurname(cursor.getString(cursor.getColumnIndex(Contact.COLUMN_SURNAME)))
                        .setPhoneNumber(cursor.getString(cursor.getColumnIndex(Contact.COLUMN_PHONE_NUMBER)))
                        .setEmail(cursor.getString(cursor.getColumnIndex(Contact.COLUMN_EMAIL)))
                        .setAddress(cursor.getString(cursor.getColumnIndex(Contact.COLUMN_ADDRESS)))
                        .setNotes(cursor.getString(cursor.getColumnIndex(Contact.COLUMN_NOTES)))
                        .setFavorite(cursor.getInt(cursor.getColumnIndex(Contact.COLUMN_FAVORTE))!=0)
                        .setBirthday(date).build();

                contacts.add(contact);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return contacts list
        return contacts;
    }

    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + Contact.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        // return count
        return count;
    }

    public int updateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Contact.COLUMN_NAME, contact.getName());
        values.put(Contact.COLUMN_SURNAME, contact.getSurname());
        values.put(Contact.COLUMN_PHONE_NUMBER, contact.getPhoneNumber());
        values.put(Contact.COLUMN_EMAIL, contact.getEmail());
        values.put(Contact.COLUMN_ADDRESS, contact.getAddress());
        values.put(Contact.COLUMN_FAVORTE, contact.isFavorite()?1:0);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(contact.getBirthday() == null)
            values.put(Contact.COLUMN_BIRTHDAY, "NULL");
        else
            values.put(Contact.COLUMN_BIRTHDAY, dateFormat.format(contact.getBirthday()));
        values.put(Contact.COLUMN_NOTES, contact.getNotes());

        // updating row
        return db.update(Contact.TABLE_NAME, values, Contact.COLUMN_ID + " = ?",
                new String[]{String.valueOf(contact.getId())});
    }

    public void deleteContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Contact.TABLE_NAME, Contact.COLUMN_ID + " = ?",
                new String[]{String.valueOf(contact.getId())});
        db.close();
    }

}
