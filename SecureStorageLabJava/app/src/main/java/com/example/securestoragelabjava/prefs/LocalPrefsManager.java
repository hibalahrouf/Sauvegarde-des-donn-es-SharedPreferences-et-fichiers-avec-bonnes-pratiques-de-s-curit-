package com.example.securestoragelabjava.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public final class LocalPrefsManager {

    private static final String LOG_TAG = "VaultLabJava";

    private static final String STORE_IDENTIFIER = "user_store";
    private static final String FIELD_USERNAME = "field_username";
    private static final String FIELD_LANGUAGE = "field_language";
    private static final String FIELD_THEME_MODE = "field_theme_mode";

    private LocalPrefsManager() {}

    public static boolean persistUserProfile(Context ctx, String username, String language, String themeMode, boolean forceSync) {

        long startTime = System.nanoTime();

        SharedPreferences prefs = ctx.getSharedPreferences(STORE_IDENTIFIER, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit()
                .putString(FIELD_USERNAME, username)
                .putString(FIELD_LANGUAGE, language)
                .putString(FIELD_THEME_MODE, themeMode);

        boolean result;

        if (forceSync) {
            result = editor.commit();
        } else {
            editor.apply();
            result = true;
        }

        long endTime = System.nanoTime();
        Log.d(LOG_TAG, "persistUserProfile time(ns): " + (endTime - startTime));

        return result;
    }

    public static UserProfileSnapshot retrieveUserProfile(Context ctx) {

        long startTime = System.nanoTime();

        SharedPreferences prefs = ctx.getSharedPreferences(STORE_IDENTIFIER, Context.MODE_PRIVATE);

        String username = prefs.getString(FIELD_USERNAME, "");
        String language = prefs.getString(FIELD_LANGUAGE, "fr");
        String themeMode = prefs.getString(FIELD_THEME_MODE, "system");

        long endTime = System.nanoTime();
        Log.d(LOG_TAG, "retrieveUserProfile time(ns): " + (endTime - startTime));

        return new UserProfileSnapshot(username, language, themeMode);
    }

    public static void wipePreferences(Context ctx) {

        long startTime = System.nanoTime();

        SharedPreferences prefs = ctx.getSharedPreferences(STORE_IDENTIFIER, Context.MODE_PRIVATE);
        prefs.edit().clear().apply();

        long endTime = System.nanoTime();
        Log.d(LOG_TAG, "wipePreferences time(ns): " + (endTime - startTime));
    }

    public static final class UserProfileSnapshot {
        public final String username;
        public final String language;
        public final String themeMode;

        public UserProfileSnapshot(String username, String language, String themeMode) {
            this.username = username;
            this.language = language;
            this.themeMode = themeMode;
        }
    }
}