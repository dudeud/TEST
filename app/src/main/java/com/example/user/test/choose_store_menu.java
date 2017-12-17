package com.example.user.test;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class choose_store_menu extends choose_client_myfavorite {
    RecyclerView recyclertype, recyclervalue;
    String key,key_type;
    DatabaseReference myRef2;
    ArrayAdapter<String> areasAdapter;
    Spinner areaSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_store_menu);

        toolbar("我的菜單");
        recyclertype = (RecyclerView) findViewById(R.id.recyclertype);
        recyclervalue = (RecyclerView) findViewById(R.id.recyclervalue);

        RecyclerViewSetting(recyclertype);
        RecyclerViewSetting(recyclervalue);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) { //TOOLBAR設計點擊列表
                switch (item.getItemId()){

                    case R.id.type:
                        addtype();
                        break;

                    case  R.id.menu:
                        addmenu();;
                        break;
                }
                return false;
            }
        });



        myRef = FirebaseDatabase.getInstance().getReference( "type" ).child( global.account );
        adapter= new  FirebaseRecyclerAdapter<menu,ViewHolder>(menu.class,R.layout.type_row, ViewHolder.class,myRef){
            @Override
            protected void populateViewHolder(ViewHolder viewHolder, menu menu, int position) {
                viewHolder.settype( menu );
            }
            @Override
            public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
                final ViewHolder viewHolder = super.onCreateViewHolder( parent, viewType );
                viewHolder.setOnClickListener( new ViewHolder.ClickListener(){
                    @Override
                    public void onItemClick(View view, int position) {

                        key =getRef(position).getKey().toString();
                        myRef = FirebaseDatabase.getInstance().getReference( "type" ).child( global.account ).child( key );
                        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    key_type = ds.getValue().toString();
                                }

                                recyclervalue.setAdapter(null);
                                myRef= FirebaseDatabase.getInstance().getReference( "menu" ).child( global.account ).child(key_type);
                                adapter = new FirebaseRecyclerAdapter<menu, ViewHolder>(menu.class,R.layout.row,ViewHolder.class,myRef) {
                                    @Override
                                    protected void populateViewHolder(ViewHolder viewHolder, menu menu, int position) {
                                        viewHolder.setValues(menu);

                                    }
                                    @Override
                                    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
                                        final ViewHolder viewHolder = super.onCreateViewHolder( parent, viewType );
                                        viewHolder.setOnClickListener( new ViewHolder.ClickListener() {
                                            @Override
                                            public void onItemClick(View view, int position) {

                                            }

                                            @Override
                                            public void onItemLongClick(View view, int position) {

                                                final String KEY = getRef( position ).getKey();
                                                myRef =FirebaseDatabase.getInstance().getReference( "menu" ).child( global.account ).child( key_type ).child(KEY);

                                                final String[] a = {"移除"};
                                                AlertDialog.Builder dialog_list = new AlertDialog.Builder(choose_store_menu.this);
                                                dialog_list.setItems(a, new DialogInterface.OnClickListener(){

                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        myRef.removeValue();

                                                    }
                                                });
                                                dialog_list.show();
                                            }
                                        });
                                       return  viewHolder;
                                    }
                                };
                                recyclervalue.setAdapter(adapter);


                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        String KEY = getRef( position ).getKey();


                        myRef = FirebaseDatabase.getInstance().getReference( "type" ).child( global.account ).child( KEY );
                        myRef.addListenerForSingleValueEvent( new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    key_type = ds.getValue().toString();
                                }
                                myRef2 = FirebaseDatabase.getInstance().getReference( "menu" ).child( global.account ).child(key_type );

                                final String[] a = {"移除此類別(包含底下菜單)"};
                                AlertDialog.Builder dialog_list = new AlertDialog.Builder(choose_store_menu.this);
                                dialog_list.setItems(a, new DialogInterface.OnClickListener(){

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        myRef.removeValue();
                                        myRef2.removeValue();
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
        recyclertype.setAdapter(adapter);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu3, menu);
        return true;
    }

    public void addtype(){ //新增分類

        LayoutInflater inflater = LayoutInflater.from(choose_store_menu.this);
        final View v = inflater.inflate(R.layout.addtype, null);
        final EditText addtype = (EditText) (v.findViewById(R.id.addtype));

        AlertDialog.Builder dialog_list = new AlertDialog.Builder(choose_store_menu.this);
        dialog_list.setTitle( "新增類別" );
        dialog_list.setView( v );
        dialog_list.setPositiveButton("新增", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String type = addtype.getText().toString().trim();
                if(type.isEmpty())
                {
                    Toast.makeText( getApplicationContext(), "不能為空", Toast.LENGTH_SHORT ).show();
                }
                else{
                    myRef = FirebaseDatabase.getInstance().getReference( "type" ).child( global.account ).push();
                    myRef.child( "type" ).setValue( type );
                }
            }
        });
        dialog_list.show();
    }

    public  void addmenu(){ //新增菜單
        LayoutInflater inflater = LayoutInflater.from(choose_store_menu.this);
        final View v = inflater.inflate(R.layout.alert_menu, null);

        final EditText food = (EditText) (v.findViewById(R.id.editTextFOOD));
        final EditText cost = (EditText) (v.findViewById(R.id.editTextcost));
        areaSpinner = (Spinner) v.findViewById(R.id.spinner_menu);


        AlertDialog.Builder dialog_list = new AlertDialog.Builder(choose_store_menu.this);
        dialog_list.setTitle("新增菜單");
        dialog_list.setView( v );

        myRef = FirebaseDatabase.getInstance().getReference( "type" ).child( global.account );             //spinner
        myRef.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<String> areas = new ArrayList<String>();

                for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {
                    String areaName = areaSnapshot.child("type").getValue(String.class);
                    areas.add(areaName);
                }

                areasAdapter = new ArrayAdapter<String>(choose_store_menu.this, android.R.layout.simple_spinner_item, areas);
                areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                areaSpinner.setAdapter(areasAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        } );

        dialog_list.setPositiveButton("新增", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String food_ = food.getText().toString().trim();
                final String cost_ = cost.getText().toString().trim();
                final String text = areaSpinner.getSelectedItem().toString();


                if(food_.isEmpty() || cost_.isEmpty()) {
                    Toast.makeText( getApplicationContext(),"不能為空", Toast.LENGTH_SHORT ).show();
                }
                else{
                    myRef = FirebaseDatabase.getInstance().getReference( "menu" ).child( global.account ).child(text).push();
                    myRef.child( "food" ).setValue( food_ );
                    myRef.child( "cost" ).setValue( Integer.parseInt( cost_ ) );

                }

            }
        });
        dialog_list.show();



    }
}
