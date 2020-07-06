package com.philipstudio.thuctaptotnghiep1.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.document.FirebaseVisionDocumentText;
import com.google.firebase.ml.vision.document.FirebaseVisionDocumentTextRecognizer;
import com.philipstudio.thuctaptotnghiep1.util.Constant;
import com.philipstudio.thuctaptotnghiep1.R;

import java.util.List;

public class TextRecognizeActivity extends AppCompatActivity {

    ImageView imgImage;
    Button btnTakePhoto, btnSelectPhoto, btnExtract;
    TextView txtDisplay;

    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        btnTakePhoto.setOnClickListener(listener);

        btnSelectPhoto.setOnClickListener(listener);

        btnExtract.setOnClickListener(listener);

    }

    private void initView(){
        imgImage = findViewById(R.id.imageview_image);
        btnSelectPhoto = findViewById(R.id.button_select_photo);
        btnExtract = findViewById(R.id.button_extract_text);
        txtDisplay = findViewById(R.id.textview_display);
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.button_take_photo:
                    dispatchTakePictureIntent();
                case R.id.button_select_photo:
                    openGallery();
                case R.id.button_extract_text:
                    detectTextFromImage(bitmap);
            }
        }
    };

    //Ham mo thu vien anh trong device
    private void openGallery(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, Constant.REQUEST_IMAGE_CAPTURE);
    }

    //Ham cap quyen va chup anh tu camera
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, Constant.REQUEST_IMAGE_CAPTURE);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == Constant.REQUEST_IMAGE_CAPTURE && data != null){
            Bundle bundle = data.getExtras();
            if(bundle != null){
                bitmap = (Bitmap) bundle.get("data");
                imgImage.setImageBitmap(bitmap);
            }
        }
    }

    //Ham nhan dang van ban trong hinh anh
    private void detectTextFromImage(Bitmap bitmap){
        FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap(bitmap);
        FirebaseVisionDocumentTextRecognizer detector = FirebaseVision.getInstance().getCloudDocumentTextRecognizer();

        detector.processImage(firebaseVisionImage).addOnSuccessListener(new OnSuccessListener<FirebaseVisionDocumentText>() {
            @Override
            public void onSuccess(FirebaseVisionDocumentText firebaseVisionDocumentText) {
                displayTextInImage(firebaseVisionDocumentText);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(TextRecognizeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Ham hien thi van ban tu ket qua cua qua trinh nhan dang van ban
    private void displayTextInImage(FirebaseVisionDocumentText firebaseVisionDocumentText) {
        List<FirebaseVisionDocumentText.Block> blockList = firebaseVisionDocumentText.getBlocks();

        if (blockList.size() == 0){
            Toast.makeText(TextRecognizeActivity.this, "No text found in image", Toast.LENGTH_SHORT).show();
        }
        else{
            for (FirebaseVisionDocumentText.Block block : blockList){
                String text = block.getText();
                txtDisplay.setText(text);
            }
        }
    }
}