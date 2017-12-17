package com.example.user.test;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class choose extends AppCompatActivity {

    private ViewPager mViewPager;
    private List<PageView> pageList;
    TextView store, client;
    TextView choose_client_menu,choose_client_record,choose_client_allstore,choose_client_favorite,choose_client_shoppingcar;
    TextView choose_store_menu,choose_store_order,choose_store_set;
    DatabaseReference myRef;
    String store_;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        store = (TextView) findViewById(R.id.textView1);
        client = (TextView) findViewById(R.id.textView2);

        initData();
        initView();
        pagechangelistener();

        click(client, 1, store, "#AAAAAA"); //顧客店家點擊變色
        click(store, 0, client, "#AAAAAA");

    }
    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(new SamplePagerAdapter());
    }
    private void initData() { //加入子畫面
        pageList = new ArrayList<>();
        pageList.add(new PageOneView(choose.this));
        pageList.add(new PageTwoView(choose.this));
    }
    private class SamplePagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return pageList.size();
        }
        @Override
        public boolean isViewFromObject(View view, Object o) {
            return o == view;
        }
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            container.addView(pageList.get(position));
            return pageList.get(position);
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
    public abstract class PageView extends RelativeLayout {
        public PageView(Context context) {
            super(context);
        }
        public abstract void refreshView();
    }
    public class PageOneView extends PageView {  //子畫面 店家與顧客
        public PageOneView(Context context) {
            super(context);
            View view = LayoutInflater.from(context).inflate(R.layout.activity_choose_store, null);
            choose_store_menu = (TextView) view.findViewById(R.id.choose_store_menu);
            choose_store_order = (TextView) view.findViewById(R.id.choose_store_order);
            choose_store_set = (TextView) view.findViewById(R.id.choose_store_set);

            TextviewClickstore(choose_store_menu,choose_store_menu.class);
            TextviewClickstore(choose_store_order,choose_store_order.class);
            TextviewClick(choose_store_set,choose_store_set.class);

            addView(view);
        }
        @Override
        public void refreshView() {
        }
    }
    public class PageTwoView extends PageView  {
        public PageTwoView(Context context) {
            super(context);
            View view = LayoutInflater.from(context).inflate(R.layout.activity_choose_client, null);

            choose_client_menu = (TextView) view.findViewById(R.id.choose_client_menu);
            choose_client_record = (TextView) view.findViewById(R.id.choose_client_record);
            choose_client_allstore = (TextView) view.findViewById(R.id.choose_client_allstore);
            choose_client_favorite = (TextView) view.findViewById(R.id.choose_client_favorite);
            choose_client_shoppingcar = (TextView) view.findViewById(R.id.choose_client_shoppingcar);

            TextviewClick(choose_client_menu,choose_client_menu.class);
            TextviewClick(choose_client_allstore,choose_client_store_1.class);
            TextviewClick(choose_client_favorite,choose_client_myfavorite.class);
            TextviewClick(choose_client_record,choose_client_record.class);
            TextviewClick(choose_client_shoppingcar,choose_client_shoppingcar.class);
            addView(view);
        }

        @Override
        public void refreshView() {
        }


    }
    public void click(TextView textviewclick, final int position, final TextView textview, final String color) {
        textviewclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(position);
                textview.setTextColor(Color.parseColor(color));
            }
        });
    }


    public void pagechangelistener() { //抓取位置 0為店家 1為顧客 點擊0店家變黑 顧客變白反之同理

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                switch (position) {

                    case 0:
                        store.setTextColor(Color.parseColor("#000000"));
                        client.setTextColor(Color.parseColor("#AAAAAA"));
                        break;
                    case 1:
                        client.setTextColor(Color.parseColor("#000000"));
                        store.setTextColor(Color.parseColor("#AAAAAA"));
                        break;

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void TextviewClick(TextView textView, final Class a ){
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(),a));
            }
        });

    }  //新頁面

    public void TextviewClickstore(TextView textView, final Class a ){
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef = FirebaseDatabase.getInstance().getReference( "store_information" ).child( global.account );
                myRef.addListenerForSingleValueEvent( new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        store_ = dataSnapshot.child( "store" ).getValue(String.class);

                        if(store_ == null)
                        {
                            Toast.makeText(getApplicationContext(), "請先登記商店資訊", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {

                            startActivity(new Intent(getApplicationContext(), a));

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                } );            }
        });

    }  //菜單跟訂單條件



}
