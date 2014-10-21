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

package com.laquysoft.droidconuk;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * This shows the debugging information.
 *
 * @author wolff
 */
public class TagsActivity extends BaseActivity {


    public TagsActivity() {
        super(CLIENT_PLUS);
    }

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_tags);

        // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
        //    getActionBar().setDisplayHomeAsUpEnabled(true);
        // }


    }


    @Override
    protected void onResume() {
        super.onResume();
        Hunt hunt = Hunt.getHunt(getResources(), getApplicationContext());

        ListView lv = (ListView) findViewById(R.id.tagsList);
        ArrayAdapter<AHTag> adapter = new TagListAdapter(
                getApplicationContext(), hunt);

        lv.setAdapter(adapter);
    }



    // Buttons

    public void onResetClicked(View view) {
        final Hunt hunt = Hunt.getHunt(getResources(), getApplicationContext());


        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        // Yes button clicked
                        hunt.reset();

                        hunt.reload(getApplicationContext());
                        signOut();
                        hunt.save(getResources(), getApplicationContext());
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        // No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_tags;
    }


}
