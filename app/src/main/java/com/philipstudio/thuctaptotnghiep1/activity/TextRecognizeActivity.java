package com.philipstudio.thuctaptotnghiep1.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//import com.philipstudio.thuctaptotnghiep1.model.VisionImage;
import com.philipstudio.thuctaptotnghiep1.util.Constant;
import com.philipstudio.thuctaptotnghiep1.R;
import com.skyhope.textrecognizerlibrary.TextScanner;
import com.skyhope.textrecognizerlibrary.callback.TextExtractCallback;

import java.util.List;


public class TextRecognizeActivity extends AppCompatActivity {

    ImageView imgImage;
    Button btnTakePhoto, btnSelectPhoto;
    TextView txtDisplay;

   // VisionImage visionImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        btnTakePhoto.setOnClickListener(listener);

        btnSelectPhoto.setOnClickListener(listener);

    }

    private void initView() {
        imgImage = findViewById(R.id.imageview_image);
        btnSelectPhoto = findViewById(R.id.button_select_photo);
        txtDisplay = findViewById(R.id.textview_display);
        btnTakePhoto = findViewById(R.id.button_take_photo);

    //    visionImage = new VisionImage();
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.button_take_photo:
                    dispatchTakePictureIntent();
                case R.id.button_select_photo:
                    openGallery();
            }
        }
    };

    //Ham mo thu vien anh trong device
    private void openGallery() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, Constant.REQUEST_SELECT_PHOTO);
    }

    //Ham cap quyen va chup anh tu camera
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, Constant.REQUEST_TAKE_IMAGE_CAPTURE);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == Constant.REQUEST_TAKE_IMAGE_CAPTURE && data != null) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    Bitmap bitmap = (Bitmap) bundle.get("data");
                    imgImage.setImageBitmap(bitmap);
            //        visionImage.detectTextFromImage(txtDisplay, bitmap, TextRecognizeActivity.this);
                }
            } else if (requestCode == Constant.REQUEST_SELECT_PHOTO && data != null) {
                Uri uri = data.getData();
                imgImage.setImageURI(uri);
                detectTextInImage(uri, txtDisplay);
            }
        }
    }

    private void detectTextInImage(Uri uri, final TextView textView){
        TextScanner.getInstance(TextRecognizeActivity.this)
                .init()
                .load(uri)
                .getCallback(new TextExtractCallback() {
                    @Override
                    public void onGetExtractText(List<String> list) {
                        final StringBuilder builder = new StringBuilder();
                        for (String str : list){
                            builder.append(str).append("\n");
                        }

                        Toast.makeText(TextRecognizeActivity.this, builder.toString(), Toast.LENGTH_LONG).show();
                        textView.post(new Runnable() {
                            @Override
                            public void run() {
                                textView.setText(builder.toString());

                            }
                        });
                    }
                });

    }
}