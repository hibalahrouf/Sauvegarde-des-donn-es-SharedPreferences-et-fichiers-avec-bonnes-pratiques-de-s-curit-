package com.example.securestoragelabjava.files;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class LearnersJsonRepository {
    private static final String TAG = "VaultLabJava";
    private Context context;
    private Gson gson;

    public LearnersJsonRepository(Context context) {
        this.context = context;
        this.gson = new Gson();
    }

    public void saveLearners(List<String> learners) {
        String json = gson.toJson(learners);
        try (FileOutputStream fos = context.openFileOutput("learners.json", Context.MODE_PRIVATE)) {
            fos.write(json.getBytes());
            Log.d(TAG, "saveLearners time(ns): " + System.nanoTime());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> loadLearners() {
        File file = new File(context.getFilesDir(), "learners.json");
        if (!file.exists()) return new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[(int) file.length()];
            int read = fis.read(buffer);
            if (read > 0) {
                String json = new String(buffer);
                Type listType = new TypeToken<List<String>>(){}.getType();
                List<String> learners = gson.fromJson(json, listType);
                Log.d(TAG, "JSON Storage -> Loaded learners count: " + learners.size());
                return learners;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}