/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.laquysoft.droidconnl;

import com.google.android.gms.common.SignInButton;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class ReauthActivity extends BaseActivity implements OnClickListener {

    ProgressDialog mProgressDialog;

    public ReauthActivity() {
        super(CLIENT_GAMES | CLIENT_PLUS);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reauth);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setTitle("Signing in...");

        SignInButton sb = (SignInButton) findViewById(R.id.sign_in_button_reauth);
        sb.setOnClickListener(this);
    }

    private Handler mHandler;

    @Override
    public void onSignInSucceeded() {
        mProgressDialog.dismiss();

        mHandler = new Handler();

        final Activity bind = this;

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent newIntent = new Intent(bind, ClueActivity.class);
                startActivity(newIntent);
                Hunt.getHunt(getResources(), getApplicationContext())
                        .setIntroSeen(true);
                bind.finish();

            }
        }, 3000);

    }

    @Override
    public void onSignInFailed() {
        mProgressDialog.dismiss();
        Toast.makeText(this, "Not signed in", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        beginUserInitiatedSignIn();
        mProgressDialog.show();

    }

}
