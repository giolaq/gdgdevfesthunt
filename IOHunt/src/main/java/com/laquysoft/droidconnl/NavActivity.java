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

import com.laquysoft.droidconnl.nichel.HuntListActivity;
import com.laquysoft.droidconnl.sync.HuntStoreService;

public class NavActivity extends Activity {
    public final static String LOG = NavActivity.class.getSimpleName();
    public final static String EXTRA_MESSAGE = "com.google.wolff.androidhunt.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.w(LOG, "onCreate");
        super.onCreate(savedInstanceState);

        Intent sendIntent = new Intent(getApplicationContext(), HuntStoreService.class);
        getApplicationContext().startService(sendIntent);

        startActivity(new Intent(this, HuntListActivity.class));
        finish();
    }
}
