package com.example.user.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class choose_store_order extends choose_client_myfavorite {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_client_recycler_one_row);
        toolbar("訂單列表");
        recycler = (RecyclerView) findViewById(R.id.recycler1);
        RecyclerViewSetting(recycler);
        myRef = FirebaseDatabase.getInstance().getReference( "store_information" ).child( global.account ); //連接資料庫讀訂單資料
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                global.store = dataSnapshot.child("store").getValue().toString();

                myRef = FirebaseDatabase.getInstance().getReference( "store_date_record" ).child( global.store );
                adapter = new FirebaseRecyclerAdapter<menu, ViewHolder>(menu.class, R.layout.type_row,ViewHolder.class, myRef) {


                    @Override
                    public menu getItem(int position) { //最新的資料擺在上方
                        return super.getItem(getItemCount() - 1 - position);
                    }
                    @Override
                    protected void populateViewHolder(ViewHolder viewHolder, menu menu, int position) {
                        viewHolder.setdate( menu );
                    }

                    @Override
                    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
                        final ViewHolder viewHolder = super.onCreateViewHolder( parent, viewType );
                        viewHolder.setOnClickListener( new ViewHolder.ClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                global.date = getRef( getItemCount() - position-1 ).getKey();
                                startActivity(new Intent(choose_store_order.this,choose_store_order_in.class));
                            }

                            @Override
                            public void onItemLongClick(View view, int position) {

                            }
                        } );
                        return viewHolder;
                    }
                };
                recycler.setAdapter( adapter);
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
}
