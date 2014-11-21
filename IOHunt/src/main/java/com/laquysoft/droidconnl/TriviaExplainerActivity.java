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
import android.view.View;
import android.widget.TextView;

public class TriviaExplainerActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_trivia_explainer);

        Hunt hunt = Hunt.getHunt(getResources(), getApplicationContext());

        // Show different text the second time through.
        if (hunt.hasAnsweredQuestion(hunt.getLastCompletedClue())) {
            ((TextView) findViewById(R.id.text_view_trivia_explainer))
                .setText(getResources().getString(R.string.between_clues_next_time));
        }
    }

    public void onStartClicked(View v) {
        Intent intent = new Intent(this, TriviaQuestionActivity.class);

        startActivity(intent);

        Hunt hunt = Hunt.getHunt(getResources(), getApplicationContext());

        hunt.setStartTime();
        hunt.save(getResources(), getApplicationContext());

        finish();
    }
}
