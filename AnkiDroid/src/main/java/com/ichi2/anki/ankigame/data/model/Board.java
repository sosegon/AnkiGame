package com.ichi2.anki.ankigame.data.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import timber.log.Timber;

public class Board {

    private static final String LOG_TAG = Board.class.getSimpleName();
    private static final String KEY_SCORE = "score";
    private static final String KEY_VALUES = "values";
    private static final String KEY_BEST_SCORE = "bestScore";
    private static final String KEY_HAS_LOST = "hasLost";
    private static final String KEY_USED_TRICKS = "usedTricks";

    private int[] boardValues;
    private int score;
    private int bestScore;
    private boolean hasLost;
    private String[] usedTricks;

    public static Board parseJSON(String jsonString) {
        Board board = new Board();

        try {
            JSONObject obj = new JSONObject(jsonString);
            int score = obj.getInt(KEY_SCORE);
            int bestScore = obj.getInt(KEY_BEST_SCORE);
            boolean hasLost = obj.getBoolean(KEY_HAS_LOST);

            JSONArray values = obj.getJSONArray(KEY_VALUES);
            int[] tiles = new int[values.length()];
            for(int i = 0; i < values.length(); i++) {
                tiles[i] = values.getInt(i);
            }

            values = obj.getJSONArray(KEY_USED_TRICKS);
            String[] usedTricks = new String[values.length()];
            for(int i = 0; i < values.length(); i++) {
                usedTricks[i] = values.getString(i);
            }

            board.boardValues = tiles;
            board.score = score;
            board.bestScore = bestScore;
            board.hasLost = hasLost;
            board.usedTricks = usedTricks;

            return board;

        } catch (JSONException e) {
            Timber.e(LOG_TAG);
        }

        return board;
    }

    public Board() {

    }

    public int[] getBoardValues() {
        return boardValues;
    }

    public int getScore() {
        return score;
    }

    public int getBestScore() {
        return bestScore;
    }

    public boolean isHasLost() {
        return hasLost;
    }

    public String[] getUsedTricks() {
        return usedTricks;
    }

    public String getUsedTricksAsString() {
        if(usedTricks.length == 0) {
            return "";
        }

        StringBuilder sUsedTricks = new StringBuilder();
        for(int i = 0; i < usedTricks.length; i++) {
            sUsedTricks.append(usedTricks[i] + ",");
        }
        return sUsedTricks.substring(0, sUsedTricks.length() - 1);
    }

    public String getBoardValuesAsString() {
        StringBuilder sBoardValues = new StringBuilder();
        for(int i = 0; i < boardValues.length; i++) {
            sBoardValues.append(boardValues[i] + ",");
        }
        return sBoardValues.substring(0, sBoardValues.length() - 1);
    }
}
