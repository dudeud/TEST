package com.example.user.test;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static java.lang.String.valueOf;


public class choose_client_menu extends choose_client_myfavorite {

    RecyclerView recycler_menu_class, recycler_menu_foodAndCost;
    DatabaseReference myRef;
    Spinner spinner;
    String store_select,store_key,store_type,KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_client_menu);

        spinner = (Spinner) findViewById(R.id.spinner_menu);
        recycler_menu_class = (RecyclerView) findViewById(R.id.recycler_menu);
        recycler_menu_foodAndCost = (RecyclerView) findViewById(R.id.recycler_menu_foodAndCost);

        RecyclerViewSetting(recycler_menu_class);
        RecyclerViewSetting(recycler_menu_foodAndCost);
        toolbar("我的菜單");

        Context a = choose_client_menu.this;
        myRef = FirebaseDatabase.getInstance().getReference("mylike").child(global.account);            // 先從我的最愛裡讀出已經加入最愛的訂單 顯示在SPINNER
        spinner(myRef, spinner, a);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {// 點擊spinner時連接資料庫得到店家帳號
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                store_select = spinner.getItemAtPosition(position).toString();

                myRef = FirebaseDatabase.getInstance().getReference( "mylike" ).child( global.account );
                myRef.orderByChild( "store" ).equalTo( store_select).addListenerForSingleValueEvent( new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds: dataSnapshot.getChildren())
                            store_key = ds.getRef().getKey();                             //店家帳號



                        myRef = FirebaseDatabase.getInstance().getReference( "type" ).child( store_key);//上一步的店家帳號 再來從資料庫拿取分類 點擊分類項目會得到該點擊的KEY

                                adapter = new FirebaseRecyclerAdapter<menu, ViewHolder>(menu.class,R.layout.type_row,ViewHolder.class,myRef) {         //outside beging
                            @Override
                            protected void populateViewHolder(ViewHolder viewHolder, menu menu, int position) {
                                viewHolder.settype( menu );
                            }
                            @Override
                            public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
                                final ViewHolder viewHolder = super.onCreateViewHolder( parent, viewType );
                                viewHolder.setOnClickListener( new ViewHolder.ClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {
                                        recycler_menu_foodAndCost.setAdapter(null);

                                        KEY = getRef( position ).getKey();
                                        myRef = FirebaseDatabase.getInstance().getReference( "type" ).child( store_key ).child( KEY );
                                        myRef.addListenerForSingleValueEvent( new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {

                                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                                    store_type = ds.getValue().toString();
                                                }
                                                //用KEY 來拿取食物價格
                                                //inside begin
                                                myRef= FirebaseDatabase.getInstance().getReference( "menu" ).child( store_key ).child(  store_type  );
                                                adapter = new FirebaseRecyclerAdapter<menu, ViewHolder>(menu.class,R.layout.row,ViewHolder.class,myRef) {
                                                    @Override
                                                    protected void populateViewHolder(ViewHolder viewHolder, menu menu, int position) {
                                                        viewHolder.setValues( menu );
                                                    }

                                                    @Override
                                                    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
                                                        final ViewHolder viewHolder = super.onCreateViewHolder( parent, viewType );
                                                        viewHolder.setOnClickListener( new ViewHolder.ClickListener()
                                                        {
                                                            @Override
                                                            public void onItemClick(View view, int position) {

                                                            }

                                                            @Override
                                                            public void onItemLongClick(View view, int position) {



                                                                final String KEY = getRef( position ).getKey();

                                                                myRef= FirebaseDatabase.getInstance().getReference( "menu" ).child( store_key).child(  store_type  ).child( KEY );
                                                                myRef.addListenerForSingleValueEvent( new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                                        menu menu = dataSnapshot.getValue( menu.class );
                                                                        final String a =  menu.getfood();
                                                                        final String b = valueOf(  menu.getcost());

                                                                        LayoutInflater inflater = LayoutInflater.from(choose_client_menu.this);
                                                                        final View v = inflater.inflate(R.layout.alert, null);
                                                                        final EditText editText = (EditText) (v.findViewById(R.id.ALERT));

                                                                        AlertDialog.Builder dialog_list = new AlertDialog.Builder(choose_client_menu.this);
                                                                        dialog_list.setTitle( "數量" );
                                                                        dialog_list.setView( v );
                                                                        dialog_list.setPositiveButton("加入購物車", new DialogInterface.OnClickListener(){
                                                                            @Override
                                                                            public void onClick(DialogInterface dialog, int which) {

                                                                                final String c = editText.getText().toString().trim();
                                                                                if(c.isEmpty() || c.equals("0")) {
                                                                                    Toast.makeText( getApplicationContext(), "不能為空或開頭為0", Toast.LENGTH_SHORT ).show();
                                                                                }
                                                                                else
                                                                                {
                                                                                    myRef = FirebaseDatabase.getInstance().getReference( "shopping_car" ).child( global.account ).child(  store_select ).child(a);
                                                                                    myRef.child("food").setValue( a );
                                                                                    myRef.child("cost").setValue(  Integer.parseInt(b) );
                                                                                    myRef.child("count").setValue( Integer.parseInt(c));

                                                                                }
                                                                            }
                                                                        });
                                                                        dialog_list.show();
                                                                    }

                                                                    @Override
                                                                    public void onCancelled(DatabaseError databaseError) {

                                                                    }
                                                                } );
                                                            }
                                                        });
                                                        return viewHolder;
                                                    }
                                                };
                                                recycler_menu_foodAndCost.setAdapter( adapter );
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        } );
                                    }

                                    @Override
                                    public void onItemLongClick(View view, int position) {

                                    }
                                } );
                                return viewHolder;
                            }
                        };
                        recycler_menu_class.setAdapter( adapter );


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                } );



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }





}



