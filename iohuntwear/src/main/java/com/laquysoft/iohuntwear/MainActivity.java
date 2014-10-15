package com.laquysoft.iohuntwear;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends Activity {


    private static final String TAG = MainActivity.class.getName();

    private TextView mTextView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.text);
            }
        });

        Bundle b = this.getIntent().getExtras();
        if (null != b) {
            final String sessionId = b.getString(HomeListenerService.KEY_SESSION_NAME);
            Log.d(TAG, "Received session id in MainActivity: " + sessionId);
            mTextView.setText(sessionId);

        }
    }
}
