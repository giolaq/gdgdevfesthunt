
package com.google.wolff.androidhunt;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

public class VictoryActivity extends BaseActivity {

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_victory);
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
}
