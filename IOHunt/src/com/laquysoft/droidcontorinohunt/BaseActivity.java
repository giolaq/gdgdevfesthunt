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

package com.laquysoft.droidcontorinohunt;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.example.games.basegameutils.BaseGameActivity;
import com.laquysoft.droidcontorinohunt.R;

/**
 * The base inheritor for all activities where you can
 * be signed in.  It manages the menus.
 *
 * If you want or need any
 *
 * @author wolff
 *
 */
public class BaseActivity extends BaseGameActivity {

    public static final int REQUEST_ACHIEVEMENTS = 1001;
	public static final int REQUEST_LEADERBOARD = 1002;

    public BaseActivity() {
        super(0);
    }

    public BaseActivity(int client) {
        super(client);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.menu_reset:
            Intent intent = new Intent(this, TagsActivity.class);
            startActivity(intent);
            return true;
        case R.id.menu_achievements:
            if (isSignedIn()) {
                startActivityForResult(getGamesClient().getAchievementsIntent(),
                        REQUEST_ACHIEVEMENTS);
            } else {
                Toast.makeText(this, "Achievements are unavailable at this time.",
                        Toast.LENGTH_SHORT).show();
            }
            return true;
        case R.id.menu_about:
            intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;
        case R.id.menu_leaderboard:
        	startActivityForResult(getGamesClient().getLeaderboardIntent(AchievementManager.ID_LEADERBOARD), REQUEST_LEADERBOARD );

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSignInFailed() {
    }

    @Override
    public void onSignInSucceeded() {

    }
 }
