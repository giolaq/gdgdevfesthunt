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

package com.google.wolff.androidhunt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.support.v4.app.FragmentActivity;

public class ScreenSlidePagerActivity extends FragmentActivity {

    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 3;

    /**
     * The pager widget, which handles animation and allows swiping horizontally
     * to access previous and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide_pager);

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        mPagerAdapter = new ScreenSlidePagerAdapter(fm);
        mPager.setAdapter(mPagerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.menu_about:
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;
        case R.id.reset_this:
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

        if (mPager.getCurrentItem() == NUM_PAGES - 1) {
            startHunt();
            return;
        }
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
            } else if (mPager.getCurrentItem() == NUM_PAGES - 1) {
                prevButton.setText("BACK");
                prevButton.setEnabled(true);
                nextButton.setText("Let's Go!");
                prevButton.setEnabled(true);
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
           }

            return ssl;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

    public void startHunt() {
        Log.e("AndroidHunt", "Connection succeeded.");

        Intent newIntent = new Intent(this, ClueActivity.class);
        startActivity(newIntent);
        Hunt hunt = Hunt.getHunt(getResources(), getApplicationContext());
        hunt.setIntroSeen(true);
        hunt.save(getResources(), getApplicationContext());

        this.finish();
    }

}
