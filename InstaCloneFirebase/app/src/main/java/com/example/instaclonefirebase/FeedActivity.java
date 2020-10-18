package com.example.instaclonefirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

public class FeedActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Options menu oluşturma ve bağlama
        MenuInflater menuInflater = getMenuInflater(); // MenuInflater sağ üstten gelen menünün kullanılması için
        menuInflater.inflate(R.menu.insta_options_menu, menu); // Res içerisinde oluşturduğumuz menünün bağlanması

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Menüden herhangi bir seçeneğe tıklandığında yapılacak işlemler

        if (item.getItemId() == R.id.add_post) {
            Intent intentToUpload = new Intent(FeedActivity.this, UploadActivity.class);
            startActivity(intentToUpload);
        } else if (item.getItemId() == R.id.signout) {

            // Signout işlemleri
            firebaseAuth.signOut();
            Intent intentToAuth = new Intent(FeedActivity.this, AuthActivity.class);
            startActivity(intentToAuth);
            finish();
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        firebaseAuth = FirebaseAuth.getInstance();
    }
}