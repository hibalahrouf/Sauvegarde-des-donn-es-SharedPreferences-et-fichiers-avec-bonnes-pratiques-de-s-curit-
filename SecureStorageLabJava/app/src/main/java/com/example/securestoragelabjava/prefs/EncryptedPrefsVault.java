package com.example.securestoragelabjava.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

public final class EncryptedPrefsVault {

    private static final String LOG_TAG = "VaultLabJava";

    private static final String VAULT_IDENTIFIER = "encrypted_vault";
    private static final String FIELD_SECRET_TOKEN = "field_secret_token";

    private EncryptedPrefsVault() {}

    private static SharedPreferences buildSecurePreferences(Context ctx) throws Exception {

        long startTime = System.nanoTime();

        MasterKey masterKey = new MasterKey.Builder(ctx)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build();

        SharedPreferences securePrefs = EncryptedSharedPreferences.create(
                ctx,
                VAULT_IDENTIFIER,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        );

        long endTime = System.nanoTime();
        Log.d(LOG_TAG, "buildSecurePreferences time(ns): " + (endTime - startTime));

        return securePrefs;
    }

    public static void storeEncryptedToken(Context ctx, String token) {

        long startTime = System.nanoTime();

        try {
            buildSecurePreferences(ctx)
                    .edit()
                    .putString(FIELD_SECRET_TOKEN, token)
                    .apply();

            Log.d(LOG_TAG, "Token stored securely (length=" + token.length() + ")");

        } catch (Exception e) {
            Log.e(LOG_TAG, "Error storing token: " + e.getMessage());
        }

        long endTime = System.nanoTime();
        Log.d(LOG_TAG, "storeEncryptedToken time(ns): " + (endTime - startTime));
    }

    public static String retrieveEncryptedToken(Context ctx) {

        long startTime = System.nanoTime();

        try {
            String token = buildSecurePreferences(ctx)
                    .getString(FIELD_SECRET_TOKEN, "");

            Log.d(LOG_TAG, "Token retrieved (length=" + token.length() + ")");

            long endTime = System.nanoTime();
            Log.d(LOG_TAG, "retrieveEncryptedToken time(ns): " + (endTime - startTime));

            return token;

        } catch (Exception e) {
            Log.e(LOG_TAG, "Error retrieving token: " + e.getMessage());
            return "";
        }
    }

    public static void wipeEncryptedVault(Context ctx) {

        long startTime = System.nanoTime();

        try {
            buildSecurePreferences(ctx)
                    .edit()
                    .clear()
                    .apply();

            Log.d(LOG_TAG, "Encrypted vault cleared");

        } catch (Exception e) {
            Log.e(LOG_TAG, "Error clearing vault: " + e.getMessage());
        }

        long endTime = System.nanoTime();
        Log.d(LOG_TAG, "wipeEncryptedVault time(ns): " + (endTime - startTime));
    }
}