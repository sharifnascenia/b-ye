package com.nascenia.biyeta.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.nascenia.biyeta.R;

import com.nascenia.biyeta.fragment.Favourite;
import com.nascenia.biyeta.fragment.Inbox;
import com.nascenia.biyeta.fragment.Match;
import com.nascenia.biyeta.fragment.Search;

import java.util.ArrayList;


public class HomeScreen extends AppCompatActivity implements View.OnClickListener{


    static Context context;
    DrawerLayout drawerLayout;


    private View actionBarView;


    private ImageView searchImageView, matchImageView, fevImageView, inboxImageView, profileImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_drawer);
        context = this;


        initIdAndActionBar();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentParentViewGroup, new Search())
                .commit();
        searchImageView.setColorFilter(Color.WHITE);
        matchImageView.setColorFilter(Color.GRAY);
        inboxImageView.setColorFilter(Color.GRAY);
        fevImageView.setColorFilter(Color.GRAY);
        profileImageView.setColorFilter(Color.GRAY);





    }

    void initIdAndActionBar()
    {
        actionBarView = getLayoutInflater().inflate(R.layout.activity_main_actionbar_item, null);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(actionBarView);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        searchImageView = (ImageView) actionBarView.findViewById(R.id.search);
        matchImageView = (ImageView) actionBarView.findViewById(R.id.match);
        fevImageView = (ImageView) actionBarView.findViewById(R.id.favorite);
        inboxImageView = (ImageView) actionBarView.findViewById(R.id.inbox);
        profileImageView = (ImageView) actionBarView.findViewById(R.id.profile);

        searchImageView.setOnClickListener(this);
        matchImageView.setOnClickListener(this);
        fevImageView.setOnClickListener(this);
        inboxImageView.setOnClickListener(this);
        profileImageView.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {

        searchImageView.setColorFilter(Color.GRAY);
        matchImageView.setColorFilter(Color.GRAY);
        inboxImageView.setColorFilter(Color.GRAY);
        fevImageView.setColorFilter(Color.GRAY);
        profileImageView.setColorFilter(Color.GRAY);
        if (drawerLayout.isDrawerOpen(Gravity.RIGHT))
            drawerLayout.closeDrawer(Gravity.RIGHT);


        int id=view.getId();
        switch (id)
        {
            case R.id.search:
                searchImageView.setColorFilter(Color.WHITE);


                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentParentViewGroup, new Search())
                        .commit();
                break;
            case R.id.match:
                matchImageView.setColorFilter(Color.WHITE);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentParentViewGroup, new Match())
                        .commit();
                break;
            case R.id.favorite:

                fevImageView.setColorFilter(Color.WHITE);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentParentViewGroup, new Favourite())
                        .commit();
                break;
            case R.id.inbox:
                inboxImageView.setColorFilter(Color.WHITE);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentParentViewGroup, new Inbox())
                        .commit();
                break;
            case R.id.profile:
                profileImageView.setColorFilter(Color.WHITE);
                if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                    drawerLayout.closeDrawer(Gravity.RIGHT);
                } else {
                    drawerLayout.openDrawer(Gravity.RIGHT);
                }
                break;
        }

    }
}