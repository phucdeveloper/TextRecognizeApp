package com.philipstudio.thuctaptotnghiep1.util;

import android.app.Application;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainApplication extends Application {
    public static MainApplication instance = null;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        copyTessDataRecognize();
    }

    private void copyTessDataRecognize() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                AssetManager assetManager = MainApplication.instance.getAssets();
                OutputStream outputStream = null;
                try {
                    InputStream inputStream = assetManager.open("vie.traineddata");
                    String tessPath = instance.tessDataPath();
                    File folder = new File(tessPath);
                    if (!folder.exists()) {
                        folder.mkdir();
                        String tessData = tessPath + "/" + "vie.traineddata";
                        File file = new File(tessData);
                        if (!file.exists()) {
                            outputStream = new FileOutputStream(tessData);
                            byte[] bytes = new byte[1024];
                            int read = inputStream.read(bytes);
                            while (read != -1) {
                                outputStream.write(bytes, 0, read);
                                read = inputStream.read(bytes);
                            }
                            Log.d("MainApplication", "Copy file tess complete");
                        } else {
                            Log.d("MainApplication", "File exists");
                        }
                    }
                } catch (IOException e) {
                    Log.d("MainApplication", "couldn't copy with the following error : " + e.toString());
                } finally {
                    try {
                        if (outputStream != null)
                            outputStream.close();
                    } catch (Exception exx) {

                    }
                }
            }
        };
        new Thread(runnable).start();
    }

    private String tessDataPath() {
        return MainApplication.instance.getExternalFilesDir(null) + "/tessdata/";
    }

    public String getTessDataParentDirectory() {
        return MainApplication.instance.getExternalFilesDir(null).getAbsolutePath();
    }
}
