package com.philipstudio.thuctaptotnghiep1.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.bumptech.glide.Glide;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutionException;

public class ConvertUriToBitmap {

    public static Bitmap loadbitmap(String url) {
        Bitmap bitmap = null;
        InputStream inputStream = null;
        BufferedInputStream bis = null;

        try {
            URLConnection urlConnection = new URL(url).openConnection();
            urlConnection.connect();
            inputStream = urlConnection.getInputStream();
            bis = new BufferedInputStream(inputStream, 8192);
            bitmap = BitmapFactory.decodeStream(bis);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return bitmap;
    }

////    public Bitmap convetBitmap(final String uri, final Context context) {
////        final Bitmap[] bitmap = {null};
////        Thread task = new Thread() {
////            @Override
////            public void run() {
////                try {
////                    bitmap[0] = Glide.with(context).asBitmap().load(Uri.parse(uri));
////                } catch (InterruptedException | ExecutionException e) {
////                    e.printStackTrace();
////                }
////            }
////        };
////
////        task.start();
////
////        return bitmap[0];
//    }
}
