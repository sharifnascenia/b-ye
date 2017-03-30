package com.nascenia.biyeta.fragment;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.nascenia.biyeta.activity.InboxListView;
import com.nascenia.biyeta.activity.RequestSentFromMe;
import com.nascenia.biyeta.activity.Search_Filter;
import com.nascenia.biyeta.activity.UserProfileActivity;
import com.nascenia.biyeta.adapter.BiodataProfileAdapter;
import com.nascenia.biyeta.adapter.CommunicationAdapter;

import com.nascenia.biyeta.adapter.Match_Adapter;
import com.nascenia.biyeta.R;
import com.nascenia.biyeta.appdata.SharePref;
import com.nascenia.biyeta.constant.Constant;
import com.nascenia.biyeta.model.InboxAllThreads.Example;
import com.nascenia.biyeta.model.SearchProfileModel;
import com.nascenia.biyeta.model.biodata.profile.*;
import com.nascenia.biyeta.model.biodata.profile.Profile;
import com.nascenia.biyeta.model.communication.profile.CommunicationProfile;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by user on 1/5/2017.
 */

public class Match extends Fragment implements View.OnClickListener {


    private final OkHttpClient client = new OkHttpClient();
    TextView biodata;
    TextView connection;
    RecyclerView recyclerView;
    ProgressBar progressBar;

    //for paging in Biodata
    int biodataPageTrack = 1;
    int toalBiodataPage;

    //for paging in Connection

    int connectionPageTrack = 1;
    int toalConnectionPage;

    List<com.nascenia.biyeta.model.communication.profile.Profile> profileCommunicationList;

    BiodataProfileAdapter biodataListAdapter;
    BiodataProfile biodataResponse;

    List<Profile> profileArrayList;
    Response response;
    int position;

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
        View v = inflater.inflate(R.layout.match, null);


        biodata = (TextView) v.findViewById(R.id.biodata);
        biodata.setOnClickListener(this);
        biodata.setBackgroundResource(R.color.colorAccent);

        connection = (TextView) v.findViewById(R.id.connection);
        connection.setOnClickListener(this);

        progressBar = (ProgressBar) v.findViewById(R.id.simpleProgressBar);

        recyclerView = (RecyclerView) v.findViewById(R.id.communication_profile_list);

        new LoadBiodataConnection().execute("http://test.biyeta.com/api/v1/profile_requests");

        return v;

    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        switch (id) {
            case R.id.biodata:
                biodataPageTrack=1;
                progressBar.setVisibility(View.VISIBLE);
                biodata.setTextColor(Color.WHITE);
                biodata.setBackgroundResource(R.color.colorAccent);

                connection.setTextColor(Color.BLACK);
                connection.setBackgroundColor(Color.GRAY);
                recyclerView.setAdapter(null);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                new LoadBiodataConnection().execute("http://test.biyeta.com/api/v1/profile_requests");

                break;

            case R.id.connection:
                connectionPageTrack=1;

                progressBar.setVisibility(View.VISIBLE);
                connection.setTextColor(Color.WHITE);
                connection.setBackgroundResource(R.color.colorAccent);

                biodata.setTextColor(Color.BLACK);
                biodata.setBackgroundColor(Color.GRAY);
                new LoadConnection().execute(" http://test.biyeta.com/api/v1/communication_requests");

                break;
        }

    }

    CommunicationAdapter communicationAdapter;
    class LoadConnection extends AsyncTask<String, String, String> {

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.GONE);


            Gson gson = new Gson();
            InputStream is = new ByteArrayInputStream(s.getBytes());
            InputStreamReader isr = new InputStreamReader(is);
            CommunicationProfile response = gson.fromJson(isr, CommunicationProfile.class);
            toalConnectionPage=response.getTotalPage();

            if (connectionPageTrack == 1 ) {
                toalConnectionPage = response.getTotalPage();
                profileCommunicationList = response.getProfiles();


                communicationAdapter = new CommunicationAdapter(profileCommunicationList, R.layout.common_user_profile_item, response.getCurrentUserSignedIn()) {
                    @Override
                    public void LoadData() {
                        connectionPageTrack++;
                        if(connectionPageTrack>1 && connectionPageTrack<= toalConnectionPage)
                        {
                            new LoadConnection().execute("http://test.biyeta.com/api/v1/communication_requests?page=" + connectionPageTrack);
                            snackbar = Snackbar
                                    .make(recyclerView, "Loading..", Snackbar.LENGTH_INDEFINITE);
                            Snackbar.SnackbarLayout snack_view = (Snackbar.SnackbarLayout) snackbar.getView();
                            snack_view.addView(new ProgressBar(getContext()));
                            snackbar.show();
                        }

                    }
                };
                recyclerView.setAdapter(communicationAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
            }

            else if (connectionPageTrack >1 && connectionPageTrack <= toalConnectionPage )
            {
                for (int i=0 ; i<response.getProfiles().size(); i++)
                {
                    profileCommunicationList.add(response.getProfiles().get(i));
                    communicationAdapter.notifyDataSetChanged();
                }
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            Response response;
            SharePref sharePref = new SharePref(getContext());
            String token = sharePref.get_data("token");
            Request request = null;

            request = new Request.Builder()
                    .url(strings[0])
                    .addHeader("Authorization", "Token token=" + token)
                    .build();
            try {
                response = client.newCall(request).execute();
                String jsonData = response.body().string();
                return jsonData;

            } catch (Exception e) {
            }

            return null;
        }
    }
    Snackbar snackbar;

    class LoadBiodataConnection extends AsyncTask<String, String, String> {

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("BiodataResponse",s);

            progressBar.setVisibility(View.GONE);
            Gson gson = new Gson();
            InputStream is = new ByteArrayInputStream(s.getBytes());
            InputStreamReader isr = new InputStreamReader(is);
            biodataResponse = gson.fromJson(isr, BiodataProfile.class);

            toalBiodataPage = biodataResponse.getTotalPage();

            if (biodataPageTrack > 1 && biodataPageTrack <= toalBiodataPage) {
                snackbar.dismiss();
                Log.e("testOvi","show more data"+profileArrayList.size());

               for (int i=0;i<biodataResponse.getProfiles().size();i++)
                   profileArrayList.add(biodataResponse.getProfiles().get(i));
                   biodataListAdapter.notifyDataSetChanged();


            }
            else if (biodataPageTrack == 1) {
                profileArrayList=biodataResponse.getProfiles();

                biodataListAdapter = new BiodataProfileAdapter(profileArrayList, R.layout.biodata_layout_item) {
                    @Override
                    public void setConnectionRequest(int id, int position) {

                        new SendConnectionRequest().execute("http://test.biyeta.com/api/v1/communication_requests", id + "", position + "");

                    }

                    @Override
                    public void LoadData() {
                        biodataPageTrack++;
                        if (biodataPageTrack <= toalBiodataPage) {
                            new LoadBiodataConnection().execute("http://test.biyeta.com/api/v1/profile_requests?page=" + biodataPageTrack);
                            snackbar = Snackbar
                                    .make(recyclerView, "Loading..", Snackbar.LENGTH_INDEFINITE);
                            Snackbar.SnackbarLayout snack_view = (Snackbar.SnackbarLayout) snackbar.getView();
                            snack_view.addView(new ProgressBar(getContext()));
                            snackbar.show();
                        }

                    }
                };
                recyclerView.setAdapter(biodataListAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            Response response;
            SharePref sharePref = new SharePref(getContext());
            String token = sharePref.get_data("token");
            Request request = null;

            request = new Request.Builder()
                    .url(strings[0])
                    .addHeader("Authorization", "Token token=" + token)
                    .build();
            try {
                response = client.newCall(request).execute();
                String jsonData = response.body().string();
                return jsonData;

            } catch (Exception e) {
            }

            return null;
        }
    }

    class SendConnectionRequest extends AsyncTask<String, String, String> {
        Integer listposition;

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.has("message")) {
                    String mes = jsonObject.getJSONArray("message").getJSONObject(0).getString("detail");
                    Toast.makeText(getContext(), mes, Toast.LENGTH_SHORT).show();
                    profileArrayList.get(listposition).getRequestStatus().setMessage("আপনি যোগাযোগের  অনুরোধ  করেছেন");
                    biodataListAdapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        protected String doInBackground(String... strings) {

            Integer id = Integer.parseInt(strings[1]);
            listposition= Integer.parseInt(strings[2]);
            RequestBody requestBody = new FormEncodingBuilder()
                    .add("profile_id", id + "")
                    .build();


            Response response;
            SharePref sharePref = new SharePref(getContext());
            String token = sharePref.get_data("token");

            Request request = new Request.Builder()
                    .url(strings[0])
                    .addHeader("Authorization", "Token token=" + token)
                    .post(requestBody)
                    .build();
            try {
                response = client.newCall(request).execute();
                String jsonData = response.body().string();
                Log.e("fuck", jsonData);
                return jsonData;

            } catch (Exception e) {
                Log.e("Fuck", e.toString());
                return null;

            }


        }
    }

}
