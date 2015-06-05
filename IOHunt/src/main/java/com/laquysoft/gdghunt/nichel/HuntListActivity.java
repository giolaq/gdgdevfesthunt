package com.laquysoft.gdghunt.nichel;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.laquysoft.gdghunt.BaseActivity;
import com.laquysoft.gdghunt.ClueActivity;
import com.laquysoft.gdghunt.Hunt;
import com.laquysoft.gdghunt.R;
import com.laquysoft.gdghunt.ReauthActivity;
import com.laquysoft.gdghunt.ScreenSlidePagerActivity;
import com.laquysoft.gdghunt.rest.RestClient;
import com.laquysoft.gdghunt.rest.model.HuntListModel;
import com.laquysoft.gdghunt.rest.model.HuntModel;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.ErrorHandler;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class HuntListActivity extends BaseActivity implements HuntAdapter.OnNewHuntListener, Hunt.OnSuccessDownloadListener {
    private final String TAG = HuntListActivity.class.getCanonicalName();
    private HuntAdapter adapter;

    public HuntListActivity() {
        super(CLIENT_GAMES);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.a_huntlist;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");

        super.onCreate(savedInstanceState);

        setActionBarIcon(R.mipmap.ic_launcher);

        adapter = new HuntAdapter(this, this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        new RestClient().get().getHunts(new Callback<HuntListModel>() {
            @Override
            public void success(HuntListModel huntListModel, Response response) {
                adapter.clear();
                adapter.setModel(huntListModel.getHunts());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                showToast();

                adapter.clear();
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean flag = super.onCreateOptionsMenu(menu);

        menu.findItem(R.id.menu_reset).setVisible(false);

        return flag;
    }

    @Override
    public void onSignInFailed() {
        Log.d(TAG, "onSignInFailed");

        startActivity(new Intent(this, ReauthActivity.class));
    }

    @Override
    public void onSignInSucceeded() {
        Log.d(TAG, "onSignInSucceeded");

        //Hunt hunt = Hunt.getHunt(getResources(), this);
        //hunt.achievementManager.processBacklog(getApiClient());
    }

    public void showToast() {
        Toast.makeText(this, "Check your network!!!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void downloadHunt(String huntid) {
        Log.d(TAG, "downloadHunt");
        Log.i(TAG, "DOWNLOAD hunt POS: " + huntid);

        final Hunt hunt = Hunt.getHunt(getResources(), getApplicationContext());
        hunt.reset();
        hunt.reload(getApplicationContext(),this,huntid);
    }

    @Override
    public void onSuccessDownloadListener(String huntid) {
        Log.d(TAG, "startNewHunt");
        Log.i(TAG, "PLAY hunt ID: " + huntid);

        final Hunt hunt = Hunt.getHunt(getResources(), getApplicationContext());

        boolean hasSeenIntro = hunt.hasSeenIntro();
        Log.i(TAG, "intro: " + hasSeenIntro);

        if (hunt.hasSeenIntro()) {
            startActivity(new Intent(this, ClueActivity.class));
            finish();
        } else {
            startActivity(new Intent(this, ScreenSlidePagerActivity.class));
            finish();
        }
    }
}
