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
import com.laquysoft.gdghunt.rest.model.HuntModel;

import java.util.ArrayList;

import retrofit.ErrorHandler;
import retrofit.RetrofitError;

public class HuntListActivity extends BaseActivity implements ErrorHandler, HuntAdapter.OnNewHuntListener, Hunt.OnSuccessDownloadListener {
    private final String TAG = HuntListActivity.class.getCanonicalName();
    private HuntAdapter adapter;
    private ArrayList<HuntModel> huntlist = new ArrayList<>();

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

        setActionBarIcon(R.drawable.ic_launcher);

        adapter = new HuntAdapter(this, this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        new GetHuntsTask(this).execute();
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

    @Override
    public Throwable handleError(RetrofitError cause) {
        Toast.makeText(this, "Network Error!!!", Toast.LENGTH_LONG).show();

        return null;
    }

    @Override
    public void downloadHunt(int position) {
        Log.d(TAG, "downloadHunt");
        Log.i(TAG, "DOWNLOAD hunt POS: " + position);

        final Hunt hunt = Hunt.getHunt(getResources(), getApplicationContext());
        hunt.reset();
        hunt.reload(getApplicationContext(),this,position);

    }

    @Override
    public void startHunt(String huntid) {
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

    @Override
    public void onSuccessDownloadListener(int position) {
        huntlist.get(position).setDownloaded(true);
        adapter.notifyItemChanged(position);
    }

    class GetHuntsTask extends AsyncTask<Void, Void, ArrayList<HuntModel>> {
        private final String TAG = GetHuntsTask.class.getCanonicalName();

        private ErrorHandler errorHandler = null;

        public GetHuntsTask(ErrorHandler errorHandler) {
            super();

            this.errorHandler = errorHandler;
        }

        @Override
        protected ArrayList<HuntModel> doInBackground(Void... params) {
            Log.d(TAG, "doInBackground");

            huntlist = new RestClient(errorHandler).getHunts().getHunts();

            return huntlist;
        }

        @Override
        protected void onPostExecute(ArrayList<HuntModel> huntlist) {
            Log.d(TAG, "onPostExecute");

            adapter.clear();
            adapter.setModel(huntlist);
            adapter.notifyDataSetChanged();

            super.onPostExecute(huntlist);
        }
    }
}
