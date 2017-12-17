package com.example.user.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.FirebaseDatabase;

public class choose_client_record_in extends choose_client_myfavorite {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_client_myfavorite);

        recycler = (RecyclerView) findViewById(R.id.recycler1);
        toolbar("列表");
        RecyclerViewSetting(recycler);

        myRef = FirebaseDatabase.getInstance().getReference( "client_record" ).child( global.account ).child( global.date ); //這邊KEY=DATE
        adapter = new FirebaseRecyclerAdapter<menu, ViewHolder>(menu.class, R.layout.row,ViewHolder.class, myRef) {
            @Override
            public menu getItem(int position) { //拿取資料 最新資料擺上方
                return super.getItem(getItemCount() - 1 - position);
            }

            @Override
            protected void populateViewHolder(ViewHolder viewHolder, menu menu, int position) {

                viewHolder.setsnt( menu );
            }

            @Override
            public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
                final ViewHolder viewHolder = super.onCreateViewHolder( parent, viewType );
                viewHolder.setOnClickListener( new ViewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        global.time = getRef(  getItemCount() - position-1 ).getKey();              //key2=time

                        Intent intent = new Intent();
                        intent.setClass(choose_client_record_in.this ,choose_client_record_infor.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                } );
                return viewHolder;
            }
        };
        recycler.setAdapter( adapter );
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
