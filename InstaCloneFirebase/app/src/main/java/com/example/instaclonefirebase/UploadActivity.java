package com.example.instaclonefirebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.MailTo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.IOException;
import java.net.URI;

public class UploadActivity extends AppCompatActivity {

    ImageView postImage;
    EditText postComment;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        postImage = findViewById(R.id.imageView);
        postComment = findViewById(R.id.txtComment);
    }

    public void upload (View view) {
        // Upload data to server

    }

    public void selectImage (View view) {
        // selectImage function that defined to ImageView click action
        // ImageView üzerine tıklanmasına tanımladığımız fonksiyon

        // Check permissions for Read to Gallery
        // Galeri okuma izni var mı kontrolü

        // If it's allow go to Gallery, otherwise request permission
        // Eğer izin varsa galeriye git aksi taktirde izin sor
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        } else {
            Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intentToGallery, 2);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // Request Permission Result for Gallery
        // İzin sorulduktan sonra çalışan fonksiyon
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intentToGallery, 2);
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // The function which works after go to the gallery with data and result code
        // Galeriye gidildikten sonra çalışan fonksiyon, sonuç kodu ve veri ile döner

        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            // Create a image uri variable
            // Gelen data içerisinden fotoğrafın kaynağını aldığımız değişken

            Uri imageURI = data.getData();
            try {
                // Check the SDK version for decode image
                // SDK versiyon kontrolü ile fotoğrafı decode ettiğimiz kısım

                if (Build.VERSION.SDK_INT >= 28) {
                    ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), imageURI);
                    bitmap = ImageDecoder.decodeBitmap(source);
                    postImage.setImageBitmap(bitmap);
                } else {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageURI);
                    postImage.setImageBitmap(bitmap);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}