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

public class sign_up extends AppCompatActivity {
    EditText phone,pw,pw_check,id,name;
    private Button button;
    private DatabaseReference myRef;
    private FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        phone=(EditText)findViewById(R.id.phone_sign_up);
        pw=(EditText)findViewById(R.id.password_sign_up);
        pw_check=(EditText)findViewById(R.id.password_check_sign_up);
        name=(EditText)findViewById(R.id.name_sign_up);

        button = (Button)findViewById(R.id.sign_up_2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String phone_input = phone.getText().toString().trim();
                final String pw_input = pw.getText().toString().trim();
                final String pw_check_input = pw_check.getText().toString().trim();
                final String name_input = name.getText().toString().trim();

                database = FirebaseDatabase.getInstance();
                myRef = database.getReference("users").child(phone_input);
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) { //註冊帳號 以及已存在與資料未填寫完整提示
                        try {
                            if (dataSnapshot.exists()) {
                                Toast.makeText(getApplicationContext(), "電話已存在", Toast.LENGTH_SHORT).show();
                            }
                            else if (dataSnapshot.exists() == false)
                            {
                                if (phone_input.isEmpty() || pw_input.isEmpty() || pw_check_input.isEmpty() || name_input.isEmpty())
                                {
                                    Toast.makeText(getApplicationContext(), "請輸入完整資料", Toast.LENGTH_SHORT).show();
                                }
                                else if(pw_check_input.equals(pw_input) == false)
                                {
                                    Toast.makeText(getApplicationContext(), "請確認密碼", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    myRef.child("電話").setValue(phone_input);
                                    myRef.child("密碼").setValue(pw_input);
                                    myRef.child("姓名").setValue(name_input);
                                    Toast.makeText(getApplicationContext(), "註冊成功", Toast.LENGTH_SHORT).show();
                                    finish();
                                }

                            }
                        }
                        catch (Exception e)
                        {
                            Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
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
