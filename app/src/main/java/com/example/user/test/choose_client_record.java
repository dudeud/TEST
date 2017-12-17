package com.example.user.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.FirebaseDatabase;

public class choose_client_record extends choose_client_myfavorite {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_client_myfavorite);

        recycler = (RecyclerView) findViewById(R.id.recycler1);
        toolbar("我的紀錄");
        RecyclerViewSetting(recycler);

        myRef = FirebaseDatabase.getInstance().getReference( "client_date_record" ).child( global.account );
        adapter = new FirebaseRecyclerAdapter<menu, ViewHolder>(menu.class, R.layout.type_row,ViewHolder.class, myRef) { //拿取資料 最新資料擺上方
            @Override
            public menu getItem(int position) {
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
                        startActivity(new Intent(choose_client_record.this,choose_client_record_in.class));

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
    protected void onDestroy() {
        super.onDestroy();
    }
}
