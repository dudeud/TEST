package com.example.user.test;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.FirebaseDatabase;

public class choose_store_order_in extends choose_client_myfavorite {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_client_recycler_one_row);

        toolbar("訂單列表");
        recycler = (RecyclerView) findViewById(R.id.recycler1);
        RecyclerViewSetting(recycler);

        myRef = FirebaseDatabase.getInstance().getReference( "store_record" ).child( global.date );
        adapter = new FirebaseRecyclerAdapter<menu, ViewHolder>(menu.class,R.layout.row, ViewHolder.class,myRef){
            @Override
            public menu getItem(int position) {
                return super.getItem(getItemCount() - 1 - position);
            }

            @Override
            protected void populateViewHolder(ViewHolder viewHolder, menu menu, int position) {
                viewHolder.settna( menu );
            }

            @Override
            public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
                final ViewHolder viewHolder = super.onCreateViewHolder( parent, viewType );
                viewHolder.setOnClickListener( new ViewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        global.key = getRef( getItemCount() - position-1 ).getKey();  //store_record key

                        view.setBackgroundColor( Color.parseColor( "#DDDDDD" ) );
                       startActivity(new Intent(choose_store_order_in.this,choose_store_order_infor.class));


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
}
