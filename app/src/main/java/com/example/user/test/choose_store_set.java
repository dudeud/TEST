package com.example.user.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class choose_store_set extends AppCompatActivity {
    private DatabaseReference myRef;
    private FirebaseDatabase database;
    EditText phone_sign_up;
    EditText name_sign_up,store_name,store_address;
    Button sign_up_2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_store_set);

        phone_sign_up = (EditText) findViewById( R.id.phone_sign_up );
        phone_sign_up.setText( global.account );

        name_sign_up = (EditText) findViewById( R.id.name_sign_up );
        store_name = (EditText) findViewById( R.id.store_name );
        store_address = (EditText) findViewById( R.id.store_address );

        sign_up_2 = (Button)findViewById( R.id.sign_up_2 );
        sign_up_2.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String name_sign_up_ = name_sign_up.getText().toString().trim();
                final String store_name_ = store_name.getText().toString().trim();
                final String store_address_ = store_address.getText().toString().trim();
                final String  phone = phone_sign_up.getText().toString().trim();

                database = FirebaseDatabase.getInstance();
                myRef = database.getReference("store_information").child( phone ); //填寫店家註冊
                myRef.addListenerForSingleValueEvent( new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists())
                        {
                            Toast.makeText( getApplicationContext(), "電話已註冊", Toast.LENGTH_SHORT ).show();

                        }

                        else if(dataSnapshot.exists()==false)
                        {

                            if (name_sign_up_.isEmpty() || store_name_.isEmpty() || store_address_.isEmpty()) {
                                Toast.makeText( getApplicationContext(), "請輸入完整資料", Toast.LENGTH_SHORT ).show();
                            }
                            else {
                                myRef.child( "store" ).setValue( store_name_ );

                                myRef = database.getReference("store_information2").child( phone ).push();
                                myRef.child( "master" ).setValue( name_sign_up_ );
                                myRef.child( "store" ).setValue( store_name_ );
                                myRef.child( "address" ).setValue( store_address_ );
                                myRef.child( "phone" ).setValue( phone );

                                Toast.makeText( getApplicationContext(), "登記成功", Toast.LENGTH_SHORT ).show();
                                finish();

                            }

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                } );

            }
        } );

    }
}
