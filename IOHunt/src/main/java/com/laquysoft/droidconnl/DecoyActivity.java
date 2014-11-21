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

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

public class DecoyActivity extends BaseActivity {

    public DecoyActivity() {
        super(CLIENT_GAMES);
    }

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_decoy);

       // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
       //     getActionBar().setDisplayHomeAsUpEnabled(true);
       // }

        Hunt hunt = Hunt.getHunt(getResources(), this);

        hunt.soundManager.play(hunt.soundManager.rejected, this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected int getLayoutResource() {
        return R.layout.activity_decoy;
    }


}

