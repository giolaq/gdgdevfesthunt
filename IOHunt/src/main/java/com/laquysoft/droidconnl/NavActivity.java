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

/**
 * This is a shim launcher activity that redirects incoming
 * intents to the right place.
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class NavActivity extends Activity {

    public final static String EXTRA_MESSAGE = "com.google.wolff.androidhunt.MESSAGE";
    public final static String STORY_NEXT = "com.google.wolff.androidhunt.STORY_NEXT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.w("AndroidHunt", "Nav: OnCreate");
        super.onCreate(savedInstanceState);

        Intent intent;
        Hunt hunt = Hunt.getHunt(getResources(), getApplicationContext());
        if (hunt.hasSeenIntro()) {
            intent = new Intent(this, ClueActivity.class);
        } else {
            intent = new Intent(this, ScreenSlidePagerActivity.class);
        }

        startActivity(intent);
        finish();
    }

    public String lastClue = null;

    @Override
    public void onStart() {
        super.onStart();
        Log.w("AndroidHunt", "Nav: OnStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onNewIntent(Intent intent) {
        Log.w("AndroidHunt", "New intent");
        Log.w("AndroidHunt", "Nav: Intent gotten: " + intent.getAction());
    }
}
