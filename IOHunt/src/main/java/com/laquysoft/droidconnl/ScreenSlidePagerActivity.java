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
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.common.SignInButton;
import com.google.example.games.basegameutils.BaseGameActivity;

public class ScreenSlidePagerActivity extends BaseGameActivity {

    /**
     * The number of miliseconds to wait after logging in.
     */
    private static final int WAIT_TIME = 2800;

    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 4;

    /**
     * The pager widget, which handles animation and allows swiping horizontally
     * to access previous and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

    private SignInButton mSignInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide_pager);

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        mPagerAdapter = new ScreenSlidePagerAdapter(fm);
        mPager.setAdapter(mPagerAdapter);

        getGameHelper().setMaxAutoSignInAttempts(0);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.limited, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_about:
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_reload:
                intent = new Intent(this, TagsActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the
            // system to handle the
            // Back button. This calls finish() on this activity and pops the
            // back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    public void onBack(View view) {
        if (mPager.getCurrentItem() == 0) {
            return;
        }

        onBackPressed();
    }

    public void onNext(View view) {
        mPager.setCurrentItem(mPager.getCurrentItem() + 1);
    }

    /**
     * A simple pager adapter that represents 4 ScreenSlidePageFragment objects,
     * in sequence, and then allows you to log in at the end.
     */
    private class ScreenSlidePagerAdapter extends FragmentPagerAdapter {
        public ScreenSlidePagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public void finishUpdate(ViewGroup container) {
            super.finishUpdate(container);

            Button nextButton = (Button) findViewById(R.id.next_button);
            Button prevButton = (Button) findViewById(R.id.prev_button);

            // We play around with the button labels as you page.
            if (mPager.getCurrentItem() == 0) {
                prevButton.setText("");
                prevButton.setEnabled(false);
                nextButton.setText("NEXT");
                nextButton.setEnabled(true);
            } else if (mPager.getCurrentItem() == NUM_PAGES - 2) {
                prevButton.setText("BACK");
                prevButton.setEnabled(true);
                nextButton.setText("LET'S GO!");
                nextButton.setEnabled(true);
            } else if (mPager.getCurrentItem() == NUM_PAGES - 1) {
                prevButton.setText("BACK");
                prevButton.setEnabled(true);
                nextButton.setText("");
                nextButton.setEnabled(false);

                mSignInButton = (SignInButton) findViewById(R.id.sign_in_button);

                mSignInButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mSignInButton != null && mSignInButton.isEnabled()) {
                            beginUserInitiatedSignIn();
                            mSignInButton.setEnabled(false);
                        }
                    }
                });
            } else {
                prevButton.setText("BACK");
                prevButton.setEnabled(true);
                nextButton.setText("NEXT");
                nextButton.setEnabled(true);
            }
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            ScreenSlidePageFragment ssl = new ScreenSlidePageFragment();

            switch (position) {
                case 0:
                    ssl.layoutId = R.layout.story_frag_1;
                    break;
                case 1:
                    ssl.layoutId = R.layout.story_frag_1a;
                    break;
                case 2:
                    ssl.layoutId = R.layout.story_frag_2;
                    break;
                case 3:
                    ssl.layoutId = R.layout.story_frag_3;
                    break;
            }

            return ssl;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

    @Override
    public void onSignInFailed() {
        Log.e("AndroidHunt", "Connection failed.");

        if (mSignInButton != null) {
            mSignInButton.setEnabled(true);
        }
    }

    private Handler mHandler;

    @Override
    public void onSignInSucceeded() {
        Log.e("AndroidHunt", "Connection succeeded.");

        mHandler = new Handler();

        final Activity bind = this;

        // If you log in on one activity but switch out before the popup appears,
        // the popup is just lost.  So, we hang around for a few seconds because
        // we're giving time for the Play Games services login dialog to appear.
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent newIntent = new Intent(bind, ClueActivity.class);
                startActivity(newIntent);
                Hunt hunt = Hunt.getHunt(getResources(), getApplicationContext());
                hunt.setIntroSeen(true);
                hunt.save(getResources(), getApplicationContext());

                bind.finish();
            }
        }, WAIT_TIME);

        return;
    }


}
