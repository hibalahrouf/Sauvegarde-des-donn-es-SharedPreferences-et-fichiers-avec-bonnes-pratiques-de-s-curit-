package com.example.securestoragelabjava;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.example.securestoragelabjava.files.CacheFileManager;
import com.example.securestoragelabjava.files.ExternalFileManager;
import com.example.securestoragelabjava.files.LearnersJsonRepository;
import com.example.securestoragelabjava.files.PrivateTextFileManager;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "VaultLabJava";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textOutput = findViewById(R.id.textOutput);
        Button btnClear = findViewById(R.id.btnClear);
        StringBuilder output = new StringBuilder();

        // ---------- SharedPreferences ----------
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        prefs.edit()
                .putString("User", "Hiba")
                .putString("Lang", "fr")
                .putString("Theme", "dark")
                .apply();
        output.append("SharedPrefs -> User=").append(prefs.getString("User", ""))
                .append(", Lang=").append(prefs.getString("Lang", ""))
                .append(", Theme=").append(prefs.getString("Theme", ""))
                .append("\n");
        Log.d(TAG, output.toString());

        // ---------- EncryptedSharedPreferences ----------
        try {
            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
            SharedPreferences encryptedPrefs = EncryptedSharedPreferences.create(
                    "secure_prefs",
                    masterKeyAlias,
                    this,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );

            encryptedPrefs.edit().putString("Token", "1234567890123456789012").apply();
            String token = encryptedPrefs.getString("Token", "");
            output.append("EncryptedPrefs -> Token length=").append(token.length()).append("\n");
            Log.d(TAG, "Token stored securely (length=22)");

        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            output.append("EncryptedPrefs -> ERROR\n");
        }

        // ---------- Private Text File ----------
        PrivateTextFileManager fileManager = new PrivateTextFileManager(this);
        fileManager.writeSecureText("note.txt", "Hello Secure World");
        String fileContent = fileManager.readSecureText("note.txt");
        output.append("TextFile -> Content: ").append(fileContent).append("\n");
        Log.d(TAG, "Text stored securely: note.txt");

        // ---------- JSON Learners ----------
        LearnersJsonRepository jsonRepo = new LearnersJsonRepository(this);
        List<String> learners = new ArrayList<>();
        learners.add("Alice");
        learners.add("Bob");
        jsonRepo.saveLearners(learners);
        List<String> loadedLearners = jsonRepo.loadLearners();
        output.append("JSON Storage -> Learners: ").append(loadedLearners.toString()).append("\n");
        Log.d(TAG, "JSON Storage -> Loaded learners count: " + loadedLearners.size());

        // ---------- Cache Storage ----------
        CacheFileManager cacheManager = new CacheFileManager(this);
        cacheManager.writeCache("cache_note.txt", "Cached Hello World");
        String cacheContent = cacheManager.readCache("cache_note.txt");
        output.append("Cache -> Content: ").append(cacheContent).append("\n");

        // ---------- External App Files ----------
        ExternalFileManager extManager = new ExternalFileManager(this);
        extManager.writeExternalFile("external_note.txt", "External Secure World");
        String extContent = extManager.readExternalFile("external_note.txt");
        output.append("ExternalFile -> Content: ").append(extContent).append("\n");

        // ---------- Show everything on screen ----------
        textOutput.setText(output.toString());

        // ---------- Button to clear all data ----------
        btnClear.setOnClickListener(v -> {
            clearAllData();
            Log.d(TAG, "Button clicked -> All data cleared!");
            textOutput.setText("All data cleared!");
        });
    }

    private void clearAllData() {
        // ---------- SharedPreferences ----------
        getSharedPreferences("user_prefs", MODE_PRIVATE).edit().clear().apply();
        Log.d(TAG, "SharedPreferences cleared");

        // ---------- EncryptedSharedPreferences ----------
        try {
            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
            SharedPreferences encryptedPrefs = EncryptedSharedPreferences.create(
                    "secure_prefs",
                    masterKeyAlias,
                    this,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
            encryptedPrefs.edit().clear().apply();
            Log.d(TAG, "EncryptedSharedPreferences cleared");
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }

        // ---------- Private Text File ----------
        deleteFile("note.txt");
        Log.d(TAG, "Private text file cleared");

        // ---------- JSON File ----------
        deleteFile("learners.json");
        Log.d(TAG, "JSON file cleared");

        // ---------- Cache ----------
        CacheFileManager cacheManager = new CacheFileManager(this);
        cacheManager.clearCache("cache_note.txt");
        Log.d(TAG, "Cache cleared");

        // ---------- External Files ----------
        ExternalFileManager extManager = new ExternalFileManager(this);
        extManager.deleteExternalFile("external_note.txt");
        Log.d(TAG, "External file cleared");

        Log.d(TAG, "All data cleared successfully!");
    }
}