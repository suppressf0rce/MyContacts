package com.binarylab.mycontacts;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.binarylab.mycontacts.model.Contact;
import com.binarylab.mycontacts.model.DBHelper;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContactsFragment extends Fragment {


    public ContactsFragment() {
        // Required empty public constructor
    }

    private View view;
    private ContactsAdapter mAdapter;
    private List<Contact> contactList = new ArrayList<>();
    private RecyclerView recyclerView;
    private TextView noContactsView;

    private Context context;

    private DBHelper db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_contacts, container, false);


        recyclerView = view.findViewById(R.id.rvContacts);
        noContactsView = view.findViewById(R.id.empty_contacts_view);

        db = new DBHelper(view.getContext());

        contactList.addAll(db.getAllContacts());

        FloatingActionButton fab = view.findViewById(R.id.fabContacts);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), AddContactActivity.class);
                i.putExtra("edit",false);
                startActivity(i);
            }
        });

        mAdapter = new ContactsAdapter(view.getContext(), contactList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(view.getContext(), LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(mAdapter);

        toggleEmptyContacts();

        /**
         * On long press on RecyclerView item, open alert dialog
         * with options to choose
         * Edit and Delete
         * */
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(view.getContext(),
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                Intent intent = new Intent(view.getContext(),ViewContactActivity.class);
                Contact contact = contactList.get(position);
                intent.putExtra("contactID", contact.getId());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {
                showActionsDialog(position);
            }
        }));

        this.context = context;
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshList();
    }

    private void refreshList(){
        contactList.clear();
        contactList.addAll(db.getAllContacts());
        mAdapter.notifyDataSetChanged();
        toggleEmptyContacts();
    }

    /**
     * Toggling list and empty contacts view
     */
    private void toggleEmptyContacts() {
        // you can check notesList.size() > 0

        if (contactList.size() > 0) {
            noContactsView.setVisibility(View.GONE);
        } else {
            noContactsView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Opens dialog with extra options
     * Add to Favorites - 0
     * Edit - 1
     * Delete
     */
    private void showActionsDialog(final int position) {
        String option = "";
        final Contact contact = contactList.get(position);
        if (contact.isFavorite())
            option = "Remove from Favorites";
        else
            option = "Add to Favorite";

        final String tmp = option;

        CharSequence colors[] = new CharSequence[]{option, "Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Choose option");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    //Option Favorites

                    if(tmp.startsWith("Add"))
                        contact.setFavorite(true);
                    else
                        contact.setFavorite(false);

                    DBHelper db = new DBHelper(getContext());
                    db.updateContact(contact);

                    ContactsFragment.super.onResume();
                } else if (which == 1) {
                    //Edit
                    Intent intent = new Intent(getContext(), AddContactActivity.class);
                    intent.putExtra("edit",true);
                    intent.putExtra("contactID", contact.getId());
                    startActivity(intent);
                } else {
                    //Delete
                    new AlertDialog.Builder(getContext())
                            .setTitle("Delete Contact")
                            .setMessage("Do you really want to delete this contact?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    DBHelper db = new DBHelper(getContext());
                                    db.deleteContact(contact);
                                    onResume();
                                }})
                            .setNegativeButton(android.R.string.no, null).show();
                }
            }


        });
        builder.show();
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
