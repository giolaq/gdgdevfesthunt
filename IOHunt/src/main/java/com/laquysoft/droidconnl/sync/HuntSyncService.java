package com.laquysoft.droidconnl.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;


public class HuntSyncService extends Service {
    private static final Object sSyncAdapterLock = new Object();
    private static HuntSyncAdapter sHuntSyncAdapter = null;

    @Override
    public void onCreate() {
        Log.d("SunshineSyncService", "onCreate - SunshineSyncService");
        synchronized (sSyncAdapterLock) {
            if (sHuntSyncAdapter == null) {
                sHuntSyncAdapter = new HuntSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sHuntSyncAdapter.getSyncAdapterBinder();
    }
}