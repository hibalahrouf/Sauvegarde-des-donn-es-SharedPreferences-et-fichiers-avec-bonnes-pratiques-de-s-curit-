package com.example.securestoragelabjava.files;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class PrivateTextFileManager {
    private static final String TAG = "VaultLabJava";
    private Context context;

    public PrivateTextFileManager(Context context) {
        this.context = context;
    }

    public void writeSecureText(String filename, String content) {
        try (FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE)) {
            fos.write(content.getBytes());
            Log.d(TAG, "Text stored securely: " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readSecureText(String filename) {
        File file = new File(context.getFilesDir(), filename);
        if (!file.exists()) return "";

        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[(int) file.length()];
            int read = fis.read(buffer);
            if (read > 0) {
                String content = new String(buffer);
                Log.d(TAG, "TextFile -> Content: " + content);
                return content;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}