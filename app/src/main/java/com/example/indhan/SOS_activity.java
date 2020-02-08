package com.example.indhan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class SOS_activity extends AppCompatActivity {

    static ArrayList<String> sos_contacts;
    EditText numberEditText;
    ImageView contactPickImageView;
    Button addButton;
    static final int PICK_CONTACT=1;
    static RecyclerView sosRecyclerView;
    static SOSListAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos_activity);

        numberEditText = findViewById(R.id.numberEditText);
        contactPickImageView = findViewById(R.id.contactPickImageView);
        addButton = findViewById(R.id.addContactsButton);
        sosRecyclerView = findViewById(R.id.sosRecyclerView);

        sos_contacts = new ArrayList<>();
        adapter = new SOSListAdapter(sos_contacts);


        sosRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        sosRecyclerView.setAdapter(adapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sos_contacts.size() == 5) {
                    Toast.makeText(SOS_activity.this, "Upto 5 SOS contacts allowed", Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                } else {
                    sos_contacts.add(numberEditText.getText().toString());
                    adapter.notifyDataSetChanged();
                }
            }
        });

        contactPickImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, PICK_CONTACT);
            }
        });

    }


    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(reqCode, resultCode, data);

            switch (reqCode) {
                case (PICK_CONTACT):
                    if (resultCode == Activity.RESULT_OK) {

                        Uri contactData = data.getData();
                        Cursor c = managedQuery(contactData, null, null, null, null);
                        if (c.moveToFirst()) {


                            String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

                            String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                            if (hasPhone.equalsIgnoreCase("1")) {
                                Cursor phones = getContentResolver().query(
                                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                                        null, null);
                                phones.moveToFirst();
                                sos_contacts.add(phones.getString(phones.getColumnIndex("data1")));
                                Log.i("sos_contacts", sos_contacts.toString());
                                adapter.notifyDataSetChanged();
                            }
                            String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));


                        }
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
