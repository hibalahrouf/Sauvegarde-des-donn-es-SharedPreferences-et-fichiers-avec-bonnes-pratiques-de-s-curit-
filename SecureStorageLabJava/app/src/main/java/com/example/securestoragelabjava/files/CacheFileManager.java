package com.example.securestoragelabjava.files;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class CacheFileManager {

    private static final String TAG = "VaultLabJava";
    private Context context;

    public CacheFileManager(Context context) {
        this.context = context;
    }

    public void writeCache(String filename, String content) {
        long startTime = System.nanoTime();
        File file = new File(context.getCacheDir(), filename);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(content.getBytes());
            Log.d(TAG, "Cache stored: " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }

        long endTime = System.nanoTime();
        Log.d(TAG, "writeCache time(ns): " + (endTime - startTime));
    }

    public String readCache(String filename) {
        long startTime = System.nanoTime();
        File file = new File(context.getCacheDir(), filename);
        if (!file.exists()) return "";

        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[(int) file.length()];
            int read = fis.read(buffer);
            if (read > 0) {
                String content = new String(buffer);
                Log.d(TAG, "Cache read: " + content);
                long endTime = System.nanoTime();
                Log.d(TAG, "readCache time(ns): " + (endTime - startTime));
                return content;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        long endTime = System.nanoTime();
        Log.d(TAG, "readCache time(ns): " + (endTime - startTime));
        return "";
    }

    public void clearCache(String filename) {
        long startTime = System.nanoTime();
        File file = new File(context.getCacheDir(), filename);
        if (file.exists() && file.delete()) {
            Log.d(TAG, "Cache cleared: " + filename);
        }
        long endTime = System.nanoTime();
        Log.d(TAG, "clearCache time(ns): " + (endTime - startTime));
    }
}