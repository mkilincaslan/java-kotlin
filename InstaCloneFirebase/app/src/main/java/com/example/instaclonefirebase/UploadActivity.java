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
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;

public class UploadActivity extends AppCompatActivity {

    ImageView postImage;
    EditText postComment;
    Bitmap bitmap;
    Uri imageURI;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        postImage = findViewById(R.id.imageView);
        postComment = findViewById(R.id.txtComment);
        storage = FirebaseStorage.getInstance(); // Get instance from Firebase Storage
        storageReference = storage.getReference();
        firebaseFirestore = FirebaseFirestore.getInstance(); // Get instance from Firebase FireStore
        firebaseAuth = FirebaseAuth.getInstance(); // Get instance from Firebase Auth
    }

    public void upload (View view) {
        // Upload data to server
        final String comment = postComment.getText().toString();

        if (imageURI != null /* && !comment.matches("") */) {
            final String[] imageName = imageURI.toString().split("/");
            storageReference
                    .child("images") // main folder - ana dosya ismi
                    .child("posts") // child folder - alt dosya ismi
                    .child("post-" + imageName[imageName.length - 1]) // image name - fotoğraf ismi
                    .putFile(imageURI)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            // Get download URL from firebase storage - Firebase depoya kaydettiğimiz fotoğrafın URL alacağız

                            StorageReference imageReference = FirebaseStorage.getInstance().getReference("images/posts/post-" + imageName[imageName.length - 1]); // Create a firebase storage reference - Bunun için bir depo referansı kuruyoruz
                            imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // Get url success
                                    String downloadURL = uri.toString();
                                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser(); // Get current user - Aktif kullanıcıyı al
                                    String userEmail = firebaseUser.getEmail();

                                    HashMap<String, Object> postData = new HashMap<>(); // Firebase FireStore gets a hashmap object - Firebase FireStore HashMap halinde objeler kabul eder
                                    // Create the data for send to firestore - firestore'a gönderilecek veri kümesini hazırlıyoruz
                                    postData.put("userEmail", userEmail);
                                    postData.put("downloadURL", downloadURL);
                                    postData.put("comment", comment);
                                    postData.put("date", FieldValue.serverTimestamp());

                                    // Write the collection name which we will send the data - Verileri göndereceğimiz veritabanı döküman yığınının adını yazıyoruz
                                    firebaseFirestore.collection("Posts").add(postData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            // Success Status
                                            Intent feedIntent = new Intent(UploadActivity.this, FeedActivity.class);
                                            feedIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Block the get back - Geri dönülmesini engellemek
                                            startActivity(feedIntent);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Failure Status
                                            Toast.makeText(UploadActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UploadActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        }
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

            imageURI = data.getData();
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