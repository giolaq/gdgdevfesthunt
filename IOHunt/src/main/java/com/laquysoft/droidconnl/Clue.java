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

import java.util.ArrayList;

public class Clue {

    public String id;
    public ArrayList<AHTag> tags;
    public String displayName;
    public String displayText;
    public String displayImage;
    public int shufflegroup;
    TriviaQuestion question;

    Clue(String id, String displayName, String displayText,
            String displayImage) {
        this.id = id;
        this.displayName = displayName;
        this.displayText = displayText;
        this.displayImage = displayImage;

        tags = new ArrayList<AHTag>();
    }

    int getCluesFound(Hunt hunt) {
        int count = 0;
        for (AHTag tag : tags) {
            if (hunt.isTagFound(tag.id)) {
                count++;
            }
        }

        return count;
    }

    String getStatus(Hunt hunt) {
        return getCluesFound(hunt) + "/" + tags.size();
    }

    void addTag(AHTag tag) {
        tags.add(tag);
    }
}
