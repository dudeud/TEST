package com.example.user.test;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.valueOf;

public class choose_client_myfavorite extends AppCompatActivity  {

    ArrayAdapter<String> areasAdapter;
    Toolbar toolbar;
    DatabaseReference myRef;
    RecyclerView recycler;
    FirebaseRecyclerAdapter<menu, ViewHolder> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_client_myfavorite);
        recycler = (RecyclerView) findViewById(R.id.recycler1);
        toolbar("我的最愛");
        RecyclerViewSetting(recycler);
        myRef = FirebaseDatabase.getInstance().getReference( "mylike" ).child( global.account ); //讀取 已加入我的最愛的店家
        adapter = new FirebaseRecyclerAdapter<menu, ViewHolder>( menu.class, R.layout.type_row, ViewHolder.class, myRef ) {
            @Override
            protected void populateViewHolder(ViewHolder viewHolder, menu menu, int position) {

                viewHolder.setStore(menu);
            }

            public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
                final ViewHolder viewHolder = super.onCreateViewHolder( parent, viewType );
                viewHolder.setOnClickListener( new ViewHolder.ClickListener() { //進入店家資訊
                    @Override
                    public void onItemClick(View view, int position) {
                        global.key = getRef( position ).getKey();
                        startActivity(new Intent(choose_client_myfavorite.this, choose_client_store_2.class));

                    }

                    @Override
                    public void onItemLongClick(View view, int position) { //長點可以移除

                        final String KEY = getRef( position ).getKey();
                        myRef = FirebaseDatabase.getInstance().getReference( "mylike" ).child( global.account ).child( KEY );
                        final String[] a = {"至我的最愛移除"};
                        AlertDialog.Builder dialog_list = new AlertDialog.Builder( choose_client_myfavorite.this );
                        dialog_list.setItems( a, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                myRef.removeValue();
                            }
                        } );
                        dialog_list.show();
                    }
                } );
                return viewHolder;
            }
        };
        recycler.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu2, menu);
        return true;
    }

    public void RecyclerViewSetting(RecyclerView recycler) {
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    public void toolbar(String a) {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(a);
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        setSupportActionBar(toolbar);
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView row;
        final TextView food;
        final TextView cost;

        final TextView textViewfoodname;
        final TextView textViewcost;
        final TextView textcount;

        public ViewHolder(final View itemView) {
            super( itemView );

            row = (TextView) itemView.findViewById( R.id.row );
            food = (TextView) itemView.findViewById( R.id.textView_food);
            cost = (TextView) itemView.findViewById( R.id.textView_cost);

            textcount = (TextView) itemView.findViewById( R.id.T_count );
            textViewfoodname = (TextView) itemView.findViewById( R.id.T_food );
            textViewcost = (TextView) itemView.findViewById( R.id.T_cost );

            itemView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onItemClick( v, getAdapterPosition() );
                }
            } );
            itemView.setOnLongClickListener( new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mClickListener.onItemLongClick( v, getAdapterPosition() );
                    return true;
                }
            } );
        }

        private ViewHolder.ClickListener mClickListener;

        public interface ClickListener {
            public void onItemClick(View view, int position);

            public void onItemLongClick(View view, int position);
        }

        public void setOnClickListener(ViewHolder.ClickListener clickListener) {
            mClickListener = clickListener;
        }


        public void setStore(menu menu) {

            row.setText(menu.getstore());
        }

        public void settype(menu menu) {

            row.setText(menu.gettype());
        }

        public void setValues(menu menu) {

            food.setText(menu.getfood());
            cost.setText( valueOf(menu.getcost()));
        }

        public void setdate(menu menu) {

            row.setText(menu.getdate());
        }

        public void setsnt(menu menu) {
            food.setText( menu.gettime() );
            cost.setText( menu.getstore() );
        }

        public void setthreeinfor(menu menu) {
            textViewfoodname.setText( menu.getfood() );
            textViewcost.setText( valueOf( menu.getcost() ) );
            textcount.setText( valueOf( menu.getcount() ) );
        }

        public void settna(menu menu){
            food.setText( menu.gettime() );
            cost.setText( menu.getaccount() );
        }

    }

    public int setallmoney(menu menu) {
        int a = menu.getcost();
        int b = menu.getcount();
        int c = a * b;
        return c;
    }


    public void spinner(DatabaseReference myRef, final Spinner spinner, final Context a) {
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<String> areas = new ArrayList<String>();

                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                    String areaName = areaSnapshot.child("store").getValue(String.class);
                    areas.add(areaName);
                }

                areasAdapter = new ArrayAdapter<String>(a, android.R.layout.simple_spinner_item, areas);
                areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(areasAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }  //getstore

}
