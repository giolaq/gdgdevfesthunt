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

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.os.SystemClock;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.text.format.Time;
import android.util.Log;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

public class Hunt {

    private static final String TAG = "Hunt";

    public static final int QUESTION_STATE_NONE = 0;
    public static final int QUESTION_STATE_INTRO = 1;
    public static final int QUESTION_STATE_QUESTIONING = 2;
    public static final String QUESTION_STATE_KEY = "QUESTION_STATE_KEY";
    public static final String FINISH_TIME_KEY = "FINISH_TIME_KEY";

    static Hunt theHunt;
    static HuntResourceManager hrm;

    public Boolean isShuffled = false;
    public int questionState = 0;
    public long finishTime;

    public SoundManager soundManager;
    public AchievementManager achievementManager;

    HashMap<String, Boolean> tagsFound;

    HashMap<String, AHTag> tags;
    HashMap<String, Clue> clues;
    ArrayList<Clue> clueList;
    ArrayList<AHTag> tagList;

    private boolean hasSeenIntro = false;

    static final String HAS_SEEN_INTRO_KEY = "HAS_SEEN_INTRO_KEY";

    static final String WRONG_CLUE = "WRONG CLUE";
    static final String ACK = "ACK";
    static final String CLUE_COMPLETE = "CLUE COMPLETE";
    static final String ALREADY_FOUND = "ALREADY FOUND";
    static final String DECOY = "DECOY";
    // The actual text for the DECOY clue.
    static final String DECOY_ID = "decoy";


    private DownloadManager downloadManager;
    private long downloadReference;


    /** Returns the singleton hunt object, and initializes it if it's not ready. */
    public static Hunt getHunt(Resources res, Context context) {

        if (theHunt == null) {

            hrm = new HuntResourceManager();
            hrm.unzipFile(res);

            theHunt = new Hunt(hrm.huntJSON, res, context);
            String android_id = Secure.getString(context.getContentResolver(),
                    Secure.ANDROID_ID);

            if (android_id == null) {
                // Fall back on devices where ANDROID_ID is not reliable.
                theHunt.shuffle(Integer.parseInt(Settings.Secure.ANDROID_ID, 0));
            } else {
                BigInteger bi = new BigInteger(android_id, 16);
                System.out.println(bi);

                theHunt.shuffle(bi.shortValue());
            }
        }

        return theHunt;
    }

    /** Shuffles the clues.  Note that each clue is marked with
     *  a difficulty group, so that, say, a hard clue can't preceed
     *  an easier clue.
     * @param seed The random number seed.
     */
    public void shuffle(int seed) {
        if (isShuffled) {
            return;
        }
        // Divide into shuffle groups
        ArrayList<ArrayList<Clue>> groups = new ArrayList<ArrayList<Clue>>(10);

        for (int i = 0; i < 10; i++) {
            groups.add(null);
        }

        for (int i = 0; i < clueList.size(); i++) {
            Clue c = clueList.get(i);

            if (groups.get(c.shufflegroup) == null) {
                groups.set(c.shufflegroup, new ArrayList<Clue>());
            }

            groups.get(c.shufflegroup).add(c);
        }

        clueList = new ArrayList<Clue>();

        Random r = new Random(seed);

        for (int i = 0; i < 10; i++) {
            ArrayList<Clue> cl = groups.get(i);

            if (cl == null)
                continue;

            Collections.shuffle(cl, r);

            clueList.addAll(cl);
        }

        isShuffled = true;
    }

    /**
     * Saves the player's progress.
     *
     * @param res
     * @param context
     */
    public void save(Resources res, Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                res.getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);

        Editor editor = sharedPref.edit();

        // XXX encrypt this?
        for (String key : tagsFound.keySet()) {
            editor.putBoolean(key, tagsFound.get(key));
        }

        editor.putBoolean(HAS_SEEN_INTRO_KEY, hasSeenIntro);
        editor.putInt(QUESTION_STATE_KEY, questionState);
        editor.putLong(FINISH_TIME_KEY, finishTime);

        editor.commit();

    }

    /** Loads player progress. */
    public void restore(Resources res, Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                res.getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);

        for (String tag : tags.keySet()) {
            Boolean val = sharedPref.getBoolean(tag, false);
            tagsFound.put(tag, val);
        }

        hasSeenIntro = sharedPref.getBoolean(HAS_SEEN_INTRO_KEY, false);
        questionState = sharedPref.getInt(QUESTION_STATE_KEY,
                QUESTION_STATE_NONE);
        finishTime = sharedPref.getLong(FINISH_TIME_KEY, 0);
    }

    /** Returns whether or not we're in a question so we can restore itself. */
    public int getQuestionState() {
        return questionState;
    }

    /** Generates the entire hunt structure from JSON */
    Hunt(String jsonString, Resources res, Context context) {

        //set filter to only when download is complete and register broadcast receiver
        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        context.registerReceiver(downloadReceiver, filter);
        soundManager = new SoundManager(context);
        achievementManager = new AchievementManager(res);

        try {
            clues = new HashMap<String, Clue>();
            clueList = new ArrayList<Clue>();
            tags = new HashMap<String, AHTag>();

            JSONObject huntObject = new JSONObject(jsonString);
            JSONArray clueObjList = huntObject.getJSONArray("clues");
            tagList = new ArrayList<AHTag>();

            int length = clueObjList.length();
            for (int i = 0; i < length; i++) {
                JSONObject clueObj = clueObjList.getJSONObject(i);

                Clue clue = new Clue(clueObj.getString("id"),
                        clueObj.getString("displayName"),
                        clueObj.getString("displayText"),
                        clueObj.getString("displayImage"));

                clue.shufflegroup = clueObj.getInt("shufflegroup");

                JSONArray tagObjList = clueObj.getJSONArray("tags");

                int tagLength = tagObjList.length();

                clueList.add(clue);

                for (int j = 0; j < tagLength; j++) {
                    JSONObject tagObj = tagObjList.getJSONObject(j);
                    AHTag tag = new AHTag(tagObj.getString("id"));
                    tag.clueId = clue.id;
                    clue.addTag(tag);
                    tags.put(tag.id, tag);
                    tagList.add(tag);
                }

                if (clueObj.has("question")) {
                    clue.question = new TriviaQuestion(
                            clueObj.getJSONObject("question"));
                }
            }
        } catch (Exception e) {
            if (e != null)
                Log.e("JSON Parser", "Error parsing Hunt data " + e.toString());
        }

        reset();
        restore(res, context);
    }

    /** Deletes all player progress.*/
    public void reset() {
        tagsFound = new HashMap<String, Boolean>();
        for (AHTag tag : tagList) {
            tagsFound.put(tag.id, false);
        }

        // I'm not currently asking a question
        questionState = QUESTION_STATE_NONE;

        hasSeenIntro = false;
    }

    /** Gets active clue.
     *
     * One objection to this is that we are computing progress rather than remembering.
     *
     * This is deliberate; if we cache progress, we are likely
     * to get be wrong, especially during state transitions.  The number of
     * operations here is very small and it is called infrequently, so this saves us the
     * trouble of maintaining and persisting progress.
     *
     * If this were a performance issue (called in an inner loop, for example), of course
     * we would cache this.
     * @return The current clue, or else null if you are finished.
     */
    Clue getCurrentClue() {
        int length = clueList.size();
        for (int i = 0; i < length; i++) {
            Clue clue = clueList.get(i);

            if (isClueFinished(clue) && questionState != QUESTION_STATE_NONE) {
                // We're still asking a question
                return clue;
            }

            if (!isClueFinished(clue)) {
                return clue;
            }
        }

        // The hunt is complete!
        return null;
    }

    /** What clue have I *just* completed? */
    public Clue getLastCompletedClue() {
        int length = clueList.size();
        Clue lastBestClue = null;
        for (int i = 0; i < length; i++) {
            Clue clue = clueList.get(i);

            if (!isClueFinished(clue)) {
                return lastBestClue;
            }

            lastBestClue = clue;
        }

        // The hunt is complete.
        return lastBestClue;
    }


    public Boolean isTagFound(String id) {
        if (!tagsFound.containsKey(id)) {
            return false;
        }

        return tagsFound.get(id);
    }

    /**
     * Called when a tag is scanned.  Checks the hunt
     *
     * @param tagId the short string that represents the tag found.
     * @return
     */
    String findTag(String tagId) {

        if (tagId.equals(DECOY_ID)) {
            return DECOY;
        }
        // See if this tag is part of this clue
        Clue clue = getCurrentClue();

        AHTag tag = tags.get(tagId);

        if (tag == null) {
            return WRONG_CLUE;
        }

        if (clue.id.equals(tag.clueId)) {
            if (isTagFound(tagId)) {
                return ALREADY_FOUND;
            }

            tagsFound.put(tag.id, true);

            if (isClueFinished(clue)) {
                return CLUE_COMPLETE;
            }

            return ACK;
        }

        return WRONG_CLUE;
    }

    /** Have we found all the clues?  Does not check question completeness. */
    Boolean isClueFinished(Clue clue) {
        for (AHTag tag : clue.tags) {
            if (!isTagFound(tag.id)) {
                return false;
            }
        }

        return true;
    }

    public int getTotalClues() {
        return clueList.size();
    }

    /** Count from 1. */
    public int getClueDisplayNumber(Clue clue) {
        return clueList.indexOf(clue) + 1;
    }

    /** Count from 0. */
    public int getClueIndex(Clue clue) {
        return clueList.indexOf(clue);
    }

    /** Return value: Whether or not it needs to load from web. */
    public void setClueImage(Resources res, ImageView imgView) {
        final Clue clue = getCurrentClue();

        if (hrm.drawables.get(clue.displayImage) == null) {
            imgView.setImageDrawable(res.getDrawable(R.drawable.ab_icon));
        } else {
            imgView.setImageDrawable(hrm.drawables.get(clue.displayImage));
        }
    }

    public boolean hasSeenIntro() {
        return hasSeenIntro;
    }

    public void setIntroSeen(Boolean val) {
        hasSeenIntro = val;
    }

    public void setQuestionState(int state) {
        questionState = state;
    }

    public void setStartTime() {
        finishTime = SystemClock.uptimeMillis() + 15000;
    }

    public Time getFinishTime() {
        Time t = new Time();

        t.set(finishTime);
        return t;
    }

    public int getSecondsLeft() {
        long t = SystemClock.uptimeMillis();

        return (int) Math.ceil((finishTime - t) / 1000.0);
    }

    public boolean isComplete() {
        return (getCurrentClue() == null);
    }

    public boolean hasAnsweredQuestion(Clue c) {
        int currentClueNum = getClueIndex(c);

        // Find the first question in the list and see if we're past it.
        int totalClues = clueList.size();
        for (int i = 0; i < totalClues; i++) {
            if (clueList.get(i).question != null) {
                return currentClueNum > i;
            }
        }

        return false;
    }

    public void reload(Context context) {
        reloadFromRemote(context);
    }


    private BroadcastReceiver downloadReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            //check if the broadcast message is for our Enqueued download
            long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (downloadReference == referenceId) {

                ParcelFileDescriptor file;
                try {
                    file = downloadManager.openDownloadedFile(downloadReference);
                    //Log.d(TAG,"Downloaded " + downloadManager.getUriForDownloadedFile(downloadReference));
                    hrm = new HuntResourceManager();
                    hrm.unzipDownloadedFile(file);

                    theHunt = new Hunt(hrm.huntJSON, context.getResources(), context);
                    String android_id = Secure.getString(context.getContentResolver(),
                            Secure.ANDROID_ID);

                    if (android_id == null) {
                        // Fall back on devices where ANDROID_ID is not reliable.
                        theHunt.shuffle(Integer.parseInt(Settings.Secure.ANDROID_ID, 0));
                    } else {
                        BigInteger bi = new BigInteger(android_id, 16);
                        System.out.println(bi);

                        theHunt.shuffle(bi.shortValue());
                    }


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }
    };



    public void reloadFromRemote(Context context) {
        downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri Download_Uri = Uri.parse("http://162.248.167.159:8080/zip/hunt.zip");
        DownloadManager.Request request = new DownloadManager.Request(Download_Uri);

        request.setAllowedNetworkTypes(
                DownloadManager.Request.NETWORK_WIFI
                        | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false).setTitle("NFC/IO Hunt")
                .setDescription("Downloading Hunt data");

        //Enqueue a new download and same the referenceId
        downloadReference = downloadManager.enqueue(request);

    }
}
