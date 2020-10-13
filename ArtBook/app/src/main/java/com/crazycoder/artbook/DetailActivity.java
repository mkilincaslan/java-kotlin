package com.crazycoder.artbook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class DetailActivity extends AppCompatActivity {

    Bitmap selectedImage;
    ImageView artImage;
    EditText txtArtNameText, txtArtPainterText, txtArtYearText;
    Button saveButton;
    SQLiteDatabase DB;
    int artId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        artImage = findViewById(R.id.imageView);
        txtArtNameText = findViewById(R.id.txtArtName);
        txtArtPainterText = findViewById(R.id.txtPainter);
        txtArtYearText = findViewById(R.id.txtYear);
        saveButton = findViewById(R.id.btnSave);

        try {
            DB = this.openOrCreateDatabase("Arts", MODE_PRIVATE, null);
            DB.execSQL("Create Table if not exists arts (id INTEGER PRIMARY KEY, artName VARCHAR, painterName VARCHAR, year VARCHAR, image BLOB)");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        Intent intent = getIntent();
        int activityType = intent.getIntExtra("activityType", 1);

        if (activityType == 2) {
            artId = intent.getIntExtra("artId", 1);
            Cursor cursor = DB.rawQuery("Select * from arts where id = ?", new String[] {String.valueOf(artId)});

            int artNameIx = cursor.getColumnIndex("artName");
            int painterNameIx = cursor.getColumnIndex("painterName");
            int yearIx = cursor.getColumnIndex("year");
            int imageIx = cursor.getColumnIndex("image");

            while (cursor.moveToNext()) {
                txtArtNameText.setText(cursor.getString(artNameIx));
                txtArtPainterText.setText(cursor.getString(painterNameIx));
                txtArtYearText.setText(cursor.getString(yearIx));
                byte[] bytes = cursor.getBlob(imageIx);
                selectedImage = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                artImage.setImageBitmap(selectedImage);
            }

            cursor.close();
            saveButton.setVisibility(View.INVISIBLE);
        } else {
            txtArtNameText.setText("");
            txtArtPainterText.setText("");
            txtArtYearText.setText("");
            selectedImage = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.addphoto);
            artImage.setImageBitmap(selectedImage);
        }
    }

    public void selectImage(View view) {

        // Context compat uses permission for api 23 before
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Permission request
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        } else {
            Intent gallery_intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(gallery_intent, 2);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 1) {
            // Read external storage permission
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentToGallery, 2);
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();

            try {

                if (Build.VERSION.SDK_INT >= 28) {
                    ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), imageUri);
                    selectedImage = ImageDecoder.decodeBitmap(source);
                    artImage.setImageBitmap(selectedImage);
                } else {
                    selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    artImage.setImageBitmap(selectedImage);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void save(View view) {

        String art_name = txtArtNameText.getText().toString();
        String art_painter = txtArtPainterText.getText().toString();
        String art_year = txtArtYearText.getText().toString();

        Bitmap smallerImage = imageCompress(selectedImage, 300);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        smallerImage.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream);
        byte[] image_data = byteArrayOutputStream.toByteArray();

        try {
            String execSql = "Insert into arts (artName, painterName, year, image) VALUES (?, ?, ?, ?)";

            SQLiteStatement sqLiteStatement = DB.compileStatement(execSql);
            sqLiteStatement.bindString(1, art_name);
            sqLiteStatement.bindString(2, art_painter);
            sqLiteStatement.bindString(3, art_year);
            sqLiteStatement.bindBlob(4, image_data);

            sqLiteStatement.execute();
        } catch (Exception ex) {

        }

        Intent intentToMain = new Intent(DetailActivity.this, MainActivity.class);
        intentToMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intentToMain);

//        finish();
    }

    public Bitmap imageCompress(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float)height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int)(width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int)(height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }
}