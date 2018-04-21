package com.binarylab.mycontacts;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.binarylab.mycontacts.model.Contact;
import com.binarylab.mycontacts.model.DBHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddContactActivity extends AppCompatActivity {

    private EditText date;
    private EditText name;
    private EditText surname;
    private EditText phone;
    private EditText email;
    private EditText address;
    private EditText note;

    private ImageView favorites;
    boolean isFavorite = false;

    private boolean edit;
    private Contact contact;

    public AddContactActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        final Context context = this;

        Toolbar toolbar = findViewById(R.id.newContactToolbar);
        toolbar.setNavigationIcon(R.drawable.ic_close_24dp);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME |
                ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_USE_LOGO);

       date = findViewById(R.id.etBirthday);
        date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){

                    DatePickerDialog dialog = new DatePickerDialog(context);
                    dialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                            String blah = ""+dayOfMonth+"/"+month+"/"+year;
                            date.setText(blah);
                        }
                    });

                    dialog.show();

                }
            }
        });

        name = findViewById(R.id.etName);
        surname = findViewById(R.id.etSurname);
        phone = findViewById(R.id.etPhone);
        email = findViewById(R.id.etEmail);
        address = findViewById(R.id.etAddress);
        note = findViewById(R.id.etNotes);
        favorites = findViewById(R.id.addFavoritesImage);

        favorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFavorite = !isFavorite;
                if(isFavorite)
                    favorites.setBackgroundResource(R.drawable.ic_star_24dp);
                else
                    favorites.setBackgroundResource(R.drawable.ic_star_border_24dp);

            }
        });

        edit = getIntent().getExtras().getBoolean("edit");

        if(edit){
            DBHelper db = new DBHelper(this);
            contact = db.getContact(getIntent().getExtras().getInt("contactID"));

            name.setText(contact.getName());
            surname.setText(contact.getSurname());
            phone.setText(contact.getPhoneNumber());
            email.setText(contact.getEmail());
            address.setText(contact.getAddress());
            note.setText(contact.getNotes());

            if(contact.isFavorite())
                favorites.setBackgroundResource(R.drawable.ic_star_24dp);
            else
                favorites.setBackgroundResource(R.drawable.ic_star_border_24dp);

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            if (contact.getBirthday() != null) {
                date.setText(dateFormat.format(contact.getBirthday()));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_contact_confirm_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add_contact_confirm) {

            if(!edit) {
                Date dater = null;
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    dater = dateFormat.parse(date.getText().toString());
                } catch (ParseException ignored) {

                }

                Contact contact = new Contact.Builder(1).
                        setName(name.getText().toString()).setSurname(surname.getText().toString())
                        .setPhoneNumber(phone.getText().toString()).setEmail(email.getText().toString())
                        .setAddress(address.getText().toString()).setBirthday(dater)
                        .setNotes(note.getText().toString()).setFavorite(isFavorite).build();

                DBHelper db = new DBHelper(getBaseContext());
                db.insertContact(contact);
                finish();
                return true;
            }else{
                Date dater = null;
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    dater = dateFormat.parse(date.getText().toString());
                } catch (ParseException ignored) {

                }

                contact.setName(name.getText().toString());
                contact.setSurname(surname.getText().toString());
                contact.setPhoneNumber(phone.getText().toString());
                contact.setEmail(email.getText().toString());
                contact.setAddress(address.getText().toString());
                contact.setBirthday(dater);
                contact.setNotes(note.getText().toString());
                contact.setFavorite(isFavorite);

                DBHelper db = new DBHelper(getBaseContext());
                db.updateContact(contact);
                finish();
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

}
