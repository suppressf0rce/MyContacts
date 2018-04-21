package com.binarylab.mycontacts;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.binarylab.mycontacts.model.Contact;
import com.binarylab.mycontacts.model.DBHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ViewContactActivity extends AppCompatActivity {

    private TextView date;
    private TextView name;
    private TextView surname;
    private TextView phone;
    private TextView email;
    private TextView address;
    private TextView note;

    private ImageView call;
    private ImageView sendMessage;
    private ImageView favorites;

    private Contact contact;
    int contactId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contact);

        final Context context = this;

        Toolbar toolbar = findViewById(R.id.newContactToolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME |
                ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_USE_LOGO);

        date = findViewById(R.id.tvBirthday);
        name = findViewById(R.id.tvName);
        surname = findViewById(R.id.tvSurname);
        phone = findViewById(R.id.tvPhone);
        email = findViewById(R.id.tvEmail);
        address = findViewById(R.id.tvAddress);
        note = findViewById(R.id.tvNotes);
        favorites = findViewById(R.id.viewFavoritesImage);
        call = findViewById(R.id.call);
        sendMessage = findViewById(R.id.sendMessage);

        Intent intent = getIntent();
        contactId = intent.getExtras().getInt("contactID");


        //Call phone action
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+contact.getPhoneNumber()));

                if (ActivityCompat.checkSelfPermission(ViewContactActivity.this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(ViewContactActivity.this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            1);

                    return;
                }
                startActivity(callIntent);
            }
        });

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + contact.getPhoneNumber()));;
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        DBHelper db = new DBHelper(this);
        contact = db.getContact(contactId);

        ((ConstraintLayout) name.getParent()).setVisibility(View.GONE);

        if (contact.getName() != null && !contact.getName().equals("")) {
            name.setText(contact.getName());
            ((ConstraintLayout) name.getParent()).setVisibility(View.VISIBLE);
        } else {
            name.setVisibility(View.GONE);
        }
        if (contact.getSurname() != null && !contact.getSurname().equals("")) {
            surname.setText(contact.getSurname());
            ((ConstraintLayout) surname.getParent()).setVisibility(View.VISIBLE);
        } else {
            surname.setVisibility(View.GONE);
        }
        if (contact.getPhoneNumber() != null && !contact.getPhoneNumber().equals("")) {
            phone.setText(contact.getPhoneNumber());
            call.setVisibility(View.VISIBLE);
            sendMessage.setVisibility(View.VISIBLE);
            ((ConstraintLayout) phone.getParent()).setVisibility(View.VISIBLE);
        }else{
            ((ConstraintLayout) phone.getParent()).setVisibility(View.GONE);
            call.setVisibility(View.GONE);
            sendMessage.setVisibility(View.GONE);
        }
        if (contact.getEmail() != null && !contact.getEmail().equals("")) {
            email.setText(contact.getEmail());
            ((ConstraintLayout) email.getParent()).setVisibility(View.VISIBLE);
        }else
            ((ConstraintLayout) email.getParent()).setVisibility(View.GONE);
        if (contact.getAddress() != null && !contact.getAddress().equals("")) {
            address.setText(contact.getAddress());
            ((ConstraintLayout) address.getParent()).setVisibility(View.VISIBLE);
        }else
            ((ConstraintLayout) address.getParent()).setVisibility(View.GONE);
        if (contact.getNotes() != null && !contact.getNotes().equals("")) {
            note.setText(contact.getNotes());
            ((ConstraintLayout) note.getParent()).setVisibility(View.VISIBLE);
        }else
            ((ConstraintLayout) note.getParent()).setVisibility(View.GONE);

        if (contact.isFavorite())
            favorites.setBackgroundResource(R.drawable.ic_star_24dp);
        else
            favorites.setBackgroundResource(R.drawable.ic_star_border_24dp);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        if (contact.getBirthday() != null) {
            ((ConstraintLayout) date.getParent()).setVisibility(View.VISIBLE);
            date.setText(dateFormat.format(contact.getBirthday()));
        }else
            ((ConstraintLayout) note.getParent()).setVisibility(View.GONE);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:"+contact.getPhoneNumber()));
                    startActivity(callIntent);

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(ViewContactActivity.this, "Permission denied to make a calls", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_contact, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.view_contact_delete) {


            new AlertDialog.Builder(this)
                    .setTitle("Delete Contact")
                    .setMessage("Do you really want to delete this contact?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            DBHelper db = new DBHelper(ViewContactActivity.this);
                            db.deleteContact(contact);
                            finish();
                        }})
                    .setNegativeButton(android.R.string.no, null).show();
        }

        if(id == R.id.view_contact_edit){

            Intent intent = new Intent(ViewContactActivity.this, AddContactActivity.class);
            intent.putExtra("edit",true);
            intent.putExtra("contactID", contact.getId());
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }
}
