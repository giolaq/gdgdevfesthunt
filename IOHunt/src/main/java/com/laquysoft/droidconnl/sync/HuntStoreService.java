package com.laquysoft.droidconnl.sync;


import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.laquysoft.droidconnl.rest.RestClient;
import com.laquysoft.droidconnl.rest.model.Hunt;
import com.laquysoft.droidconnl.rest.model.HuntModel;

import java.util.List;


public class HuntStoreService extends IntentService {

    private final String LOG_TAG = HuntStoreService.class.getSimpleName();

    public HuntStoreService() {
        super("HuntStoreService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d(LOG_TAG, "onHandleIntent");
        RestClient restClient = new RestClient();
        HuntModel huntModel = restClient.getApiService().getHunts();
        for ( Hunt h : huntModel.getHunts()) {
            Log.d(LOG_TAG, "hunt " + h.getDisplayName() );
            Log.d(LOG_TAG, "hunt " + h.getImageUrl() );

        }


    }





}
