package com.laquysoft.droidconnl;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.text.format.Time;


public class AchievementManager {

    public static String ID_FIRST_CLUE;
    public static String ID_HALFWAY;
    public static String ID_SPEEDRUN;
    public static String ID_TEACHERS_PET;
    public static String ID_5_TRIVIA_CORRECTLY;
    public static String ID_DOUBLE_SCAN;
    public static String ID_FOUND_ALEX;
	public static String ID_LEADERBOARD;

    static final String FOUND_ACHIEVEMENTS_KEY = "FOUND_ACHIEVEMENTS_KEY";

    public static final int PAR_QUESTIONS = 3;
    public static final int TOTAL_QUESTIONS = 5;

    public ArrayList<Long> tagScannedTimes = new ArrayList<Long>();
    
    
    private int points = 0;

    public AchievementManager(Resources res) {
        ID_FIRST_CLUE = res.getString(R.string.achievement_first_clue);
        ID_HALFWAY = res.getString(R.string.achievement_halfway_there);
        ID_SPEEDRUN = res.getString(R.string.achievement_speedrun);
        ID_TEACHERS_PET = res.getString(R.string.achievement_teachers_pet);
        ID_5_TRIVIA_CORRECTLY = res.getString(R.string.achievement_pursuing_trivia);
        ID_DOUBLE_SCAN = res.getString(R.string.achievement_double_scan);
        ID_FOUND_ALEX = res.getString(R.string.achievement_alexander_the_great);
        ID_LEADERBOARD = res.getString(R.string.leaderboard_id);
    }

    /** Each function returns whether or not we have have already
     * seen this pop.
     * If the player were playing on a separate device or the achievement
     * were already won, it may not pop. */

    public void onNewTagScanned(int clueNum, GoogleApiClient ga, Context ctx) {
        Time t = new Time();
        t.setToNow();
        tagScannedTimes.add(t.toMillis(false));

        if (tagScannedTimes.size() >= 3 && clueNum > 0) {
            if (tagScannedTimes.get(tagScannedTimes.size() - 1)
                    - tagScannedTimes.get(tagScannedTimes.size() - 3) < 60000) {
                winAchievement(ctx, ID_SPEEDRUN, ga);
            }
        }
    }

    public void onOldTagScanned(GoogleApiClient ga, Context ctx) {
        winAchievement(ctx, ID_DOUBLE_SCAN, ga);
    }

    public void onQuestionAnswered(GoogleApiClient ga, Boolean underPar, Context ctx) {
        if (underPar) {
            incrementAchievement(ID_TEACHERS_PET, ga);
            points += 20;
           // Games.Leaderboards.submitScore(ga, ID_LEADERBOARD, points);

        }

        incrementAchievement(ID_5_TRIVIA_CORRECTLY, ga);
    }

    public void onCompletedClue(Clue clue, ClueActivity ca, GoogleApiClient ga) {

        if (clue.id.equals("starterMat")
                || ca.getHunt().getClueIndex(clue) == 0) {
            winAchievement(ca, ID_FIRST_CLUE, ga);
        }

        if (1.0 * (ca.getHunt().getClueIndex(clue)+1)
                / ca.getHunt().getTotalClues() >= 0.5) {
            winAchievement(ca, ID_HALFWAY, ga);
        }
        

    }

    public void onVictory(GoogleApiClient ga, Context ctx) {
        winAchievement(ctx, ID_FOUND_ALEX, ga);
    }

    ArrayList<String> storedAchievements = new ArrayList<String>();
    ArrayList<String> storedIncrements = new ArrayList<String>();

    public void storeAchievement(String id) {
        storedAchievements.add(id);
    }

    public void storeIncrement(String id) {
        storedIncrements.add(id);
    }

    public void processBacklog(GoogleApiClient ga) {
        // Still not time yet?
        if (!ga.isConnected()) {
            return;
        }
        for (String id : storedAchievements) {
            Games.Achievements.unlock(ga, id);
        }
        storedAchievements.clear();
        for (String id : storedIncrements) {
            Games.Achievements.increment(ga, id, 1);
        }
        Games.Leaderboards.submitScore(ga, ID_LEADERBOARD, points);
        storedIncrements.clear();
    }

    public void winAchievement(Context ctx, String id, GoogleApiClient ga) {
        if (!ga.isConnected()) {
            // Hmm, can't get here, so we'll wait.
            storeAchievement(id);
            return;
        }
        Games.Achievements.unlock(ga, id);
    }

    public void incrementAchievement(String id, GoogleApiClient ga) {
        if (!ga.isConnected()) {
            // Hmm, can't get here, so we'll wait.
            storeIncrement(id);
            return;
        }

        Games.Achievements.increment(ga, id, 1);
    }

	public void onDecoy(Clue clue, ClueActivity clueActivity,
                        GoogleApiClient gamesClient) {
		  
        points -= 10;
       // Games.Leaderboards.submitScore(gamesClient, ID_LEADERBOARD, points);
	}
}
