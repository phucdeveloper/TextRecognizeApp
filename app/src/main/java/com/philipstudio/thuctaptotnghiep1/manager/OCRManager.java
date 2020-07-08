package com.philipstudio.thuctaptotnghiep1.manager;

import android.graphics.Bitmap;

import com.googlecode.tesseract.android.TessBaseAPI;
import com.philipstudio.thuctaptotnghiep1.util.MainApplication;

public class OCRManager {
    TessBaseAPI baseAPI = null;
    public void initApi(){
        baseAPI = new TessBaseAPI();
        String dataPath = MainApplication.instance.getTessDataParentDirectory();
        baseAPI.init(dataPath, "vie");
    }

    public String startRecognize(Bitmap bitmap){
        if (baseAPI == null){
            initApi();
        }
        else{
            baseAPI.setImage(bitmap);
        }
        return baseAPI.getUTF8Text();
    }
}
