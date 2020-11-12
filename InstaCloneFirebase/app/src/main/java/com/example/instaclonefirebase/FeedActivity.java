package com.example.instaclonefirebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FeedActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    ArrayList<String> userEmailOfFb;
    ArrayList<String> userCommentOfFb;
    ArrayList<String> userImageOfFb;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Create the options menu and bind - Seçenekler menüsü oluşturma ve bağlama
        MenuInflater menuInflater = getMenuInflater(); // Use MenuInflater cause right-top menu  - MenuInflater sağ üstten gelen menünün kullanılması için
        menuInflater.inflate(R.menu.insta_options_menu, menu); // Binding with menu which is in Res - Res içerisinde oluşturduğumuz menünün bağlanması

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // When click the any items from menu - Menüden herhangi bir seçeneğe tıklandığında yapılacak işlemler

        if (item.getItemId() == R.id.add_post) {
            Intent intentToUpload = new Intent(FeedActivity.this, UploadActivity.class);
            startActivity(intentToUpload);
        } else if (item.getItemId() == R.id.signout) {

            // Signout process - Signout işlemleri
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

        userCommentOfFb = new ArrayList<>();
        userEmailOfFb = new ArrayList<>();
        userImageOfFb = new ArrayList<>();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        getData();
    }

    public void getData() {
        firebaseFirestore.collection("Posts").orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(FeedActivity.this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                } else {
                    if (value != null) {
                        for (DocumentSnapshot snapshot : value.getDocuments()) {
                            Map<String, Object> data = snapshot.getData();
                            String comment = (String) data.get("comment");
                            String userEmail = (String) data.get("userEmail");
                            String downloadURL = (String) data.get("downloadURL");

                            userEmailOfFb.add(userEmail);
                            userCommentOfFb.add(comment);
                            userImageOfFb.add(downloadURL);
                        }
                    }
                }
            }
        });
    }
}