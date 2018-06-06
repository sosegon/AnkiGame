package com.ichi2.anki.ankigame.util;

import org.json.JSONException;
import org.json.JSONObject;

import timber.log.Timber;

public class InfoHandler {

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

    public static String getValidCardInfo(String fullString) {
        String sCardInfo = "";
        String[] fields = {"mElapsedTime", "mQueue", "mWasNew"};

        String[] keys = fullString.split(",");

        for(String field : fields) {
            for(String key : keys) {
                if(key.contains(field)){
                    try {
                        String value = key.split(":")[1];
                        sCardInfo += value + ",";
                    } catch (IndexOutOfBoundsException e) {
                        Timber.e("Failed to get from card: " + field);
                        continue;
                    }
                }
            }
        }

        // Remove last comma
        if (sCardInfo.length() > 0 && sCardInfo.charAt(sCardInfo.length() - 1) == ',') {
            sCardInfo = sCardInfo.substring(0, sCardInfo.length() - 1);
        }

        return sCardInfo;
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

    public static String filterCardInfo(String cardInfo) {
        return getValidCardInfo(cardInfo);
    }
}
