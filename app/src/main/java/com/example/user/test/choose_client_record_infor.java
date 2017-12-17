package com.example.user.test;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class choose_client_record_infor extends choose_client_myfavorite {
    TextView MMMM ;
    String store,all;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_client_record_infor);

        MMMM = (TextView) findViewById( R.id.textmmmm );

        recycler = (RecyclerView) findViewById(R.id.recycler1);
        toolbar("紀錄資訊");
        RecyclerViewSetting(recycler);

        myRef = FirebaseDatabase.getInstance().getReference( "client_record" ).child( global.account ).child( global.date ).child( global.time ); //讀取資料
        myRef.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                store = dataSnapshot.child( "store" ).getValue(String.class);
                all = dataSnapshot.child( "all" ).getValue(String.class);
                MMMM.setText( all );


                myRef = FirebaseDatabase.getInstance().getReference( "client_record" ).child( global.account ).child(  store  ).child( global.date ).child( global.time );
                adapter = new FirebaseRecyclerAdapter<menu, ViewHolder>(menu.class, R.layout.three_row,ViewHolder.class, myRef) {
                    @Override
                    protected void populateViewHolder(ViewHolder viewHolder, menu menu, int position) {
                        viewHolder.setthreeinfor( menu );
                    }
                    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
                        final ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
                        viewHolder.setOnClickListener(new ViewHolder.ClickListener() {

                            @Override
                            public void onItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemLongClick(View view, int position) {

                            }
                        });
                        return viewHolder;
                    }
                };
                recycler.setAdapter( adapter );
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        } );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
