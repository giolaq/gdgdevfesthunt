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

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class HuntLite {

    static HuntLite theHunt;
    static HuntResourceManager hrm;

    HashMap<String, AHTag> tags;
    ArrayList<AHTag> tagList;

    /** Returns the singleton hunt object, and initializes it if it's not ready. */
    public static HuntLite getHunt(Resources res, Context context) {

        if (theHunt == null) {
            hrm = new HuntResourceManager();
            hrm.unzipFile(res);

            theHunt = new HuntLite(hrm.huntJSON, res, context);
        }

        return theHunt;
    }


    /** Generates the entire hunt structure from JSON */
    HuntLite(String jsonString, Resources res, Context context) {

        try {
            tags = new HashMap<String, AHTag>();

            JSONObject huntObject = new JSONObject(jsonString);
            JSONArray clueObjList = huntObject.getJSONArray("clues");
            tagList = new ArrayList<AHTag>();

            int length = clueObjList.length();
            for (int i = 0; i < length; i++) {
                JSONObject clueObj = clueObjList.getJSONObject(i);

                JSONArray tagObjList = clueObj.getJSONArray("tags");

                int tagLength = tagObjList.length();

                for (int j = 0; j < tagLength; j++) {
                    JSONObject tagObj = tagObjList.getJSONObject(j);
                    AHTag tag = new AHTag(tagObj.getString("id"));
                    tags.put(tag.id, tag);
                    tagList.add(tag);
                }
            }
        } catch (Exception e) {
            if (e != null)
                Log.e("JSON Parser", "Error parsing Hunt data " + e.toString());
        }

    }
}
