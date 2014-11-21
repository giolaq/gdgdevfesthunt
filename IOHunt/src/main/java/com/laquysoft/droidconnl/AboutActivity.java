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

import android.app.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

public class AboutActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    public void onNoticesClicked(View v) {
        createNotices(this);
    }

    public void onTermsClicked(View v) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://m.google.com/utos"));
        startActivity(browserIntent);
    }

    public void onPrivacyClicked(View v) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.google.com/policies/privacy/"));
        startActivity(browserIntent);
    }

    public static void createNotices(Activity act) {
        LayoutInflater inflater = LayoutInflater.from(act);
        View addView = inflater.inflate(R.layout.dialog_license, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                act);
        alertDialogBuilder.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog,int id) {
                // if this button is clicked, close
                // current activity
                dialog.dismiss();
            }
          }).setView(addView).create().show();
    }
}
