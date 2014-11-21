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

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TriviaQuestion {

    public String question;
    public String bitmapID;

    public ArrayList<String> answers;

    public int correctAnswer = -1;

    public String rightMessage;
    public String wrongMessage;

    public TriviaQuestion(JSONObject jsonobj) {
        try {
            question = jsonobj.getString("question");
            if (jsonobj.has("bitmap")) {
                bitmapID = jsonobj.getString("bitmap");
            }

            answers = new ArrayList<String>();

            JSONArray questionList = jsonobj.getJSONArray("answers");
            for (int i = 0; i < questionList.length(); i++) {
                String answerString = (String) questionList.get(i);
                answers.add(answerString);
            }

            correctAnswer = jsonobj.getInt("correctAnswer");
            rightMessage = jsonobj.getString("rightMessage");
            wrongMessage = jsonobj.getString("wrongMessage");
        } catch (JSONException e) {
            Log.e("AndroidHunt", "Error parsing TriviaQuestion "
                    + e.getStackTrace().toString());
        }
    }

}
