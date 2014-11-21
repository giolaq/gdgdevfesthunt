/*
 * Copyright 2013 Google Inc.
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

import com.google.android.gms.games.Games;
import com.google.android.gms.plus.PlusShare;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import android.view.View;
import android.view.View.OnClickListener;

public class VictoryActivity extends BaseActivity implements OnClickListener {

    public TextView mShareButton;

    public VictoryActivity() {
        super(CLIENT_GAMES | CLIENT_PLUS);
    }

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_victory);

        mShareButton = (TextView) findViewById(R.id.share_button);
        mShareButton.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        mShareButton.setEnabled(false);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Hunt hunt = Hunt.getHunt(getResources(), getApplicationContext());
        // In rare cases, we may reset the data, and we need to back out and
        // start the story over
        if (!hunt.hasSeenIntro()) {
            Intent intent = new Intent(this, ScreenSlidePagerActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onSignInSucceeded() {
        Hunt hunt = Hunt.getHunt(getResources(), getApplicationContext());

        Hunt.getHunt(getResources(), getApplicationContext()).soundManager
        .play(Hunt.getHunt(getResources(), getApplicationContext()).soundManager.fanfare,
            this);

        // Save the fact that we've already seen the victory screen and don't need to
        // play the sound again. XXX
        hunt.save(getResources(), this);

        TextView tv = (TextView) findViewById(R.id.nameView);
        tv.setText(Games.Players.getCurrentPlayer(getApiClient()).getDisplayName());

        mShareButton.setEnabled(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.share_button:
            PlusShare.Builder builder = new PlusShare.Builder(this);

            // Set call-to-action metadata.
            builder.addCallToAction("FIND", /** call-to-action button label */
            Uri.parse("http://www.droidcon.nl"), /**
             * call-to-action url
             * (for desktop use)
             */
            "/pages/create"/**
             * call to action deep-link ID (for mobile use),
             * 512 characters or fewer
             */
            );

            // Set the content url (for desktop use).
            builder.setContentUrl(Uri.parse("http://www.droidcon.nl"));

            // Set the target deep-link ID (for mobile use).
            builder.setContentDeepLinkId("http://www.droidcon.nl", null, null, null);

            // Set the share text.
            builder.setText("I win the Droidcon NL Treasure Hunt #droidconnl!");
//                builder.setContentUrl(uri)

            startActivityForResult(builder.getIntent(), 0);
            break;
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_victory;
    }

}
