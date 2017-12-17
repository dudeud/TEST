package com.example.user.test;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class choose_client_store_2 extends choose_client_myfavorite {
    Toolbar toolbar;
    DatabaseReference myRef;
    String STORE,PHONE,ADDRESS;
    TextView store,phone,address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_client_store_2);

        toolbar("商店資訊");

        store = (TextView) findViewById(R.id.textView_store);
        phone = (TextView) findViewById(R.id.textView_phone);
        address = (TextView) findViewById(R.id.textView_address);

        myRef = FirebaseDatabase.getInstance().getReference( "store_information2" ).child( global.key );
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {  //讀出資料
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds :dataSnapshot.getChildren())
                {
                    STORE=ds.child("store").getValue().toString();
                    PHONE=ds.child("phone").getValue().toString();
                    ADDRESS=ds.child("address").getValue().toString();

                }
                store.setText(STORE);
                phone.setText(PHONE);
                address.setText(ADDRESS);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu2, menu);
        return true;
    }
}
