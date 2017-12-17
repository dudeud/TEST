package com.example.user.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private Button button,button1;
    private DatabaseReference myRef;
    private FirebaseDatabase database;
    EditText phone,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        phone=(EditText)findViewById(R.id.phone);
        password=(EditText)findViewById(R.id.password);



        button = (Button)findViewById(R.id.sign_up);  //進入註冊頁
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this ,sign_up.class);
                startActivity(intent);
            }
        });


        button1 =(Button)findViewById(R.id.login);
        button1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) { //登入 判斷帳號有無正確以及錯誤提示

                final String phone_ = phone.getText().toString().trim();
                final String password_ = password.getText().toString().trim();

                final Intent intent = new Intent();
                intent.setClass(MainActivity.this ,choose.class);
                database = FirebaseDatabase.getInstance();
                myRef = database.getReference("users").child(phone_);
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(phone_.isEmpty() || password_.isEmpty())
                        {
                            Toast.makeText(getApplicationContext(), "不能為空", Toast.LENGTH_SHORT).show();
                        }
                        else if(dataSnapshot.exists() )
                        {
                            if( dataSnapshot.child("密碼").getValue().equals(password_)) {
                                {
                                    startActivity(intent);
                                    global.account=phone_;
                                }
                            }
                            else
                                Toast.makeText(getApplicationContext(), "密碼錯誤", Toast.LENGTH_SHORT).show();

                        }
                        else if(dataSnapshot.exists()== false)

                        {
                            Toast.makeText(getApplicationContext(), "帳號錯誤", Toast.LENGTH_SHORT).show();

                        }


                        myRef.removeEventListener(this);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });
    }
}
