package com.philipstudio.thuctaptotnghiep1.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.philipstudio.thuctaptotnghiep1.manager.OCRManager;
import com.philipstudio.thuctaptotnghiep1.util.Constant;
import com.philipstudio.thuctaptotnghiep1.R;

import java.io.IOException;
import java.util.List;


public class TextRecognizeActivity extends AppCompatActivity {

    ImageView imgImage;
    Button btnTakePhoto, btnSelectPhoto;
    TextView txtDisplay;

    OCRManager ocrManager;
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

        ocrManager = new OCRManager();
        ocrManager.initApi();
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.button_take_photo:
                    dispatchTakePictureIntent();
                case R.id.button_select_photo:
                    Dexter.withContext(getApplicationContext())
                            .withPermissions(
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.INTERNET
                            ).withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                            if (multiplePermissionsReport.areAllPermissionsGranted()){
                                openGallery();
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        }
                    }).check();
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
                }
            } else if (requestCode == Constant.REQUEST_SELECT_PHOTO && data != null) {
                Uri uri = data.getData();
                Bitmap bitmapImage = getBitmapFromUri(uri);
                imgImage.setImageURI(uri);
          //      ocrManager.startRecognize(bitmapImage);
            }
        }
    }

    private Bitmap getBitmapFromUri(Uri uri){
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }
}