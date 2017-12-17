package com.example.user.test;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class choose_client_shoppingcar extends choose_client_myfavorite {
    TextView MMMM ;
    Spinner spinner;
    ArrayAdapter<String> areasAdapter;
    Button bt;
    String getall,store_select,date,time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_client_shoppingcar);

        MMMM = (TextView) findViewById( R.id.textmmmm );
        spinner = (Spinner) findViewById( R.id.spinner_menu);
        bt = (Button) findViewById( R.id.button1 ) ;

        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sTimeFormat = new SimpleDateFormat("hh:mm:ss");

        date = sDateFormat.format(new java.util.Date());
        time = sTimeFormat.format(new java.util.Date());

        recycler = (RecyclerView) findViewById(R.id.recycler1);
        toolbar("購物車");
        RecyclerViewSetting(recycler);

        myRef = FirebaseDatabase.getInstance().getReference( "mylike" ).child( global.account );             //spinner
        myRef.addListenerForSingleValueEvent( new ValueEventListener() { //連接資料庫得到商店名稱 如果購物車無加入任何店家 SPINNER關閉不給點擊

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<String> areas = new ArrayList<String>();

                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                    String areaName = areaSnapshot.child( "store" ).getValue( String.class );
                    areas.add( areaName );
                }

                areasAdapter = new ArrayAdapter<String>( choose_client_shoppingcar.this, android.R.layout.simple_spinner_item, areas );
                areasAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
                spinner.setAdapter( areasAdapter );

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        } );

        spinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) { //SPINNER不為空 開啟
                store_select = spinner.getItemAtPosition( position ).toString();

                if(store_select !=null)
                {
                    bt.setEnabled(true);
                }
                myRef = FirebaseDatabase.getInstance().getReference( "shopping_car" ).child( global.account ).child(  store_select );
                recycler.removeAllViewsInLayout();
//獲取資料 以及計算金額
                MMMM.setText( null );
                adapter = new FirebaseRecyclerAdapter<menu, ViewHolder>( menu.class, R.layout.three_row, ViewHolder.class, myRef ) {
                    int all = 0;

                    @Override
                    protected void populateViewHolder(ViewHolder viewHolder, menu menu, int position) {
                        viewHolder.setthreeinfor( menu );

                        setallmoney( menu);
                        int get = setallmoney( menu );
                        all = all+get;
                        getall = String.valueOf( all );
                        MMMM.setText(  getall );
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

                                final String[] AAAA = {"移除"};
                                AlertDialog.Builder a = new AlertDialog.Builder( choose_client_shoppingcar.this );
                                a.setItems( AAAA, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        myRef = FirebaseDatabase.getInstance().getReference( "shopping_car" ).child( global.account ).child(  store_select ).child( KEY );
                                        myRef.removeValue();
                                    }
                                } );
                                a.show();
                            }
                        } );
                        return viewHolder;
                    }

                };

                recycler.setAdapter( adapter );
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        } );




        bt.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder a = new AlertDialog.Builder( choose_client_shoppingcar.this );
                a.setMessage( "確定送出" );
                a.setPositiveButton( "確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myRef = FirebaseDatabase.getInstance().getReference( "client_date_record" ).child( global.account ).child( date ).child( "date" );
                        myRef.setValue( date );
                        myRef = FirebaseDatabase.getInstance().getReference( "store_date_record" ).child( store_select  ).child( date ).child( "date" );
                        myRef.setValue( date );

                        myRef = FirebaseDatabase.getInstance().getReference( "client_record" ).child( global.account ).child( date ).child( time );
                        myRef.child( "store" ).setValue(  store_select);
                        myRef.child( "time" ).setValue( time );
                        myRef.child( "all" ).setValue( getall );



                        myRef = FirebaseDatabase.getInstance().getReference( "store_record" ).child( date).push();
                        myRef.child( "all" ).setValue( getall );
                        myRef.child( "time" ).setValue(time );
                        myRef.child( "account" ).setValue( global.account );

                        myRef = FirebaseDatabase.getInstance().getReference( "shopping_car" ).child( global.account ).child(  store_select );
                        myRef.addListenerForSingleValueEvent( new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                for(DataSnapshot ds : dataSnapshot.getChildren())
                                {
                                    String a = ds.child( "food" ).getValue( String.class );
                                    int b =  ds.child( "cost" ).getValue( int.class ) ;
                                    int c =  ds.child( "count" ).getValue( int.class ) ;


                                    myRef = FirebaseDatabase.getInstance().getReference( "client_record" ).child( global.account ).child(store_select ).child(date).child(time ).push();
                                    myRef.child( "food" ).setValue( a );
                                    myRef.child( "cost" ).setValue( b );
                                    myRef.child( "count" ).setValue( c );

                                    myRef = FirebaseDatabase.getInstance().getReference( "store_record" ).child( store_select ).child( date ).child( global.account ).child( time ).push();
                                    myRef.child( "food" ).setValue( a );
                                    myRef.child( "cost" ).setValue( b );
                                    myRef.child( "count" ).setValue( c );


                                }
                                myRef = FirebaseDatabase.getInstance().getReference( "shopping_car" ).child( global.account ).child(  store_select );
                                myRef.removeValue();
                                MMMM.setText( null );
                                myRef.removeEventListener( this);
                            }


                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        } );

                    }
                } );
                a.setNegativeButton( "取消",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                a.show();

            }
        } );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
