package com.ichi2.anki.ankigame.util;

import org.json.JSONException;
import org.json.JSONObject;

import timber.log.Timber;

public class DeckInfoHandler {

    public static String getValidDeckInfo(JSONObject jsonDeck) {
        String sDeckInfo = "";
        String[] fields = {"name", "newToday", "revToday", "lrnToday", "timeToday", "extendNew", "extendRev"};

        for(String field : fields) {
            try {
                sDeckInfo += jsonDeck.getString(field) + ",";
            } catch (JSONException e) {
                Timber.e("Failed to get from deck: " + field);
                continue;
            }
        }

        // Remove last comma
        if (sDeckInfo.length() > 0 && sDeckInfo.charAt(sDeckInfo.length() - 1) == ',') {
            sDeckInfo = sDeckInfo.substring(0, sDeckInfo.length() - 1);
        }

        return sDeckInfo;
    }

    public static String filterDeckInfo(String deckInfo) {
        try {
            JSONObject json = new JSONObject(deckInfo);
            return getValidDeckInfo(json);
        } catch(JSONException e) {
            Timber.e("Impossible to filter deck info");
            return deckInfo;
        }
    }
}
