package com.example.user.test;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class choose_client_store_1 extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recycler;
    DatabaseReference myRef;
    ArrayList availableLocales = new ArrayList<>();
    LanguageAdapter ad;
    String a, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_client_recycler_one_row);

        toolbar("商店搜尋");

        recycler = (RecyclerView) findViewById(R.id.recycler1);
        RecyclerViewSetting(recycler);

        myRef = FirebaseDatabase.getInstance().getReference("store_information"); //從資料庫讀取店家列表
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String st = ds.child("store").getValue().toString();
                    availableLocales.add(st);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        ad = new LanguageAdapter(availableLocales);



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem menuSearchItem = menu.findItem(R.id.search);


        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menuSearchItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (newText.length() != 0) {
                    recycler.setAdapter(new LanguageAdapter(OnFilter(availableLocales, newText)));
                } else if (newText.isEmpty()) {
                    recycler.setAdapter(null);
                }
                return false;
            }
        });


        return true;
    }


    private List<String> OnFilter(List<String> filterLocales, String text) {
        String search = text.toLowerCase();

        final List<String> filtered = new ArrayList<>();

        for (String info : filterLocales) {
            final String localeName = info.toLowerCase();
            if (localeName.contains(search)) {
                filtered.add(localeName);
            }
        }
        return filtered;
    }

    private class LanguageAdapter extends RecyclerView.Adapter<ViewHolder> {

        private List<String> mLocales;

        private LanguageAdapter(List<String> mLocales) {
            this.mLocales = mLocales;
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
            TextView tv = (TextView) LayoutInflater.from(parent.getContext())
                    .inflate(android.R.layout.simple_list_item_1, parent, false);
            ViewHolder vh = new ViewHolder(tv);

            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.mTextView.setText(mLocales.get(position));
            holder.setOnClickListener(new ViewHolder.ClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    a = mLocales.get(position);

                    myRef = FirebaseDatabase.getInstance().getReference("store_information");
                    myRef.orderByChild("store").equalTo(a).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                global.key = ds.getKey();
                            }
                            startActivity(new Intent(choose_client_store_1.this, choose_client_store_2.class));
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }

                @Override
                public void onItemLongClick(View view, int position) {

                    a = mLocales.get(position);
                    myRef = FirebaseDatabase.getInstance().getReference("store_information");
                    myRef.orderByChild("store").equalTo(a).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                phone = ds.getKey();
                            }

                            myRef = FirebaseDatabase.getInstance().getReference("mylike").child(global.account);
                            final String[] like = {"加入我的最愛"};
                            AlertDialog.Builder dialog_list = new AlertDialog.Builder(choose_client_store_1.this);
                            dialog_list.setItems(like, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    myRef.child(phone).child("store").setValue(a);
                                    Toast.makeText(getApplicationContext(), "已加入我的最愛", Toast.LENGTH_SHORT).show();
                                }
                            });
                            dialog_list.show();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            });
        }

        @Override
        public int getItemCount() {
            return mLocales.size();
        }


    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextView;
        public ViewHolder(final View itemView) {
            super( itemView );

            mTextView = (TextView) itemView;

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
    }

}

