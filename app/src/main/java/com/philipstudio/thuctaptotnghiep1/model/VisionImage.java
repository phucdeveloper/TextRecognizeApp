package com.philipstudio.thuctaptotnghiep1.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.io.IOException;
import java.util.List;

public class VisionImage {

    public VisionImage() {
    }

    //Ham nhan dang van ban trong hinh anh
    public void detectTextFromImage(final TextView textView, Bitmap bitmap, final Context context){
        FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap(bitmap);
        FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance().getOnDeviceTextRecognizer();

        detector.processImage(firebaseVisionImage)
                .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                    @Override
                    public void onSuccess(FirebaseVisionText firebaseVisionText) {
                        String content = displayTextInImage(firebaseVisionText, context);
                        textView.setText(content);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showNotifications(context, e.getMessage());
            }
        });
    }

    //Ham nhan dang van ban trong hinh anh
    public void detectTextFromImage(final TextView textView, Uri uri, final Context context){
        FirebaseVisionImage firebaseVisionImage = null;
        try {
            firebaseVisionImage = FirebaseVisionImage.fromFilePath(context, uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance().getOnDeviceTextRecognizer();

        detector.processImage(firebaseVisionImage)
                .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                    @Override
                    public void onSuccess(FirebaseVisionText firebaseVisionText) {
                        String content = displayTextInImage(firebaseVisionText, context);
                        textView.setText(content);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showNotifications(context, e.getMessage());
                    }
                });
    }

    //Ham hien thi van ban tu ket qua cua qua trinh nhan dang van ban
    public String displayTextInImage(FirebaseVisionText firebaseVisionText, Context context) {
        String blockText = null;
        List<FirebaseVisionText.TextBlock> blocks = firebaseVisionText.getTextBlocks();
        if (blocks.size() == 0){
            showNotifications(context, "No Text Found In Image");
        }
        else{
            for (FirebaseVisionText.TextBlock block : blocks){
                blockText = block.getText();
            }
        }

        return blockText ;
    }

    public void showNotifications(Context context, String content){
        Toast.makeText(context, content, Toast.LENGTH_LONG).show();
    }
}
