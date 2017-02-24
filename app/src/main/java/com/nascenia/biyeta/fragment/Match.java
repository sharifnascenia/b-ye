package com.nascenia.biyeta.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.nascenia.biyeta.adapter.Match_Adapter;
import com.nascenia.biyeta.adapter.Profile_Adapter;
import com.nascenia.biyeta.R;

/**
 * Created by user on 1/5/2017.
 */

public class Match extends Fragment {


    RecyclerView recyclerView;
    Match_Adapter mMatch_adapter;
    RelativeLayout relativeLayout;
    private List<com.nascenia.biyeta.model.Profile> movieList = new ArrayList<>();

    public Match() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("come", "Match");
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.match, null);
        recyclerView = (RecyclerView) v.findViewById(R.id.match_list);
        mMatch_adapter = new Match_Adapter(movieList);
        relativeLayout = (RelativeLayout) v.findViewById(R.id.RelativeLayoutLeftButton);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mMatch_adapter);


        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // TODO Handle item click
                        showDialog(getContext(), movieList.get(position).getDisplay_name(), movieList.get(position).getLocation());

                    }
                })
        );


        new Get_Data().execute();

        //  prepareMovieData();
        return v;

    }

    public void showDialog(Context activity, String display_name, String location) {
        final Dialog dialog = new Dialog(activity);

        dialog.setContentView(R.layout.custom_dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        TextView display = (TextView) dialog.findViewById(R.id.display_name);
        TextView loc = (TextView) dialog.findViewById(R.id.details);
        display.setText(display_name);
        loc.setText(location);
        dialog.setCancelable(false);


        dialog.show();

    }

    class Get_Data extends AsyncTask<String, String, String> {
        @Override
        protected void onPostExecute(String res) {
            super.onPostExecute(res);


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            relativeLayout.setVisibility(View.GONE);


        }

        @Override
        protected String doInBackground(String... url) {
            return null;

        }
    }
}