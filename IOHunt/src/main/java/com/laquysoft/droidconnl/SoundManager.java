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
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

public class SoundManager {
    public SoundPool soundPool;

    public int foundItAll;
    public int repeat;
    public int rejected;
    public int foundIt;
    public int fanfare;

    SoundManager(Context context) {
        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);

        foundItAll = soundPool.load(context, R.raw.founditall, 1);
        repeat = soundPool.load(context, R.raw.repeat, 1);
        rejected = soundPool.load(context, R.raw.rejected, 1);
        foundIt = soundPool.load(context, R.raw.coin, 1);
        fanfare = soundPool.load(context, R.raw.fanfare, 1);
    }

    public void play(int soundID, Activity activity) {
        // Is the sound loaded already?
        if (soundID != 0) {
            soundPool.play(soundID, 1, 1, 1, 0, 1f);
            Log.e("AndroidHunt", "Played sound " + soundID);
        }
    }
}
