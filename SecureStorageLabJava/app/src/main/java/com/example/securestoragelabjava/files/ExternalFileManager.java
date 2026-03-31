package com.example.securestoragelabjava.files;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExternalFileManager {

    private static final String TAG = "VaultLabJava";
    private Context context;

    public ExternalFileManager(Context context) {
        this.context = context;
    }

    public void writeExternalFile(String filename, String content) {
        long startTime = System.nanoTime();
        File file = new File(context.getExternalFilesDir(null), filename);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(content.getBytes());
            Log.d(TAG, "External file stored: " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }

        long endTime = System.nanoTime();
        Log.d(TAG, "writeExternalFile time(ns): " + (endTime - startTime));
    }

    public String readExternalFile(String filename) {
        long startTime = System.nanoTime();
        File file = new File(context.getExternalFilesDir(null), filename);
        if (!file.exists()) return "";

        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[(int) file.length()];
            int read = fis.read(buffer);
            if (read > 0) {
                String content = new String(buffer);
                Log.d(TAG, "External file read: " + content);
                long endTime = System.nanoTime();
                Log.d(TAG, "readExternalFile time(ns): " + (endTime - startTime));
                return content;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        long endTime = System.nanoTime();
        Log.d(TAG, "readExternalFile time(ns): " + (endTime - startTime));
        return "";
    }

    public void deleteExternalFile(String filename) {
        long startTime = System.nanoTime();
        File file = new File(context.getExternalFilesDir(null), filename);
        if (file.exists() && file.delete()) {
            Log.d(TAG, "External file deleted: " + filename);
        }
        long endTime = System.nanoTime();
        Log.d(TAG, "deleteExternalFile time(ns): " + (endTime - startTime));
    }
}