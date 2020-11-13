package com.example.instaclonefirebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    FeedRecyclerAdapter feedRecyclerAdapter; // RecycleView adapter which we will use - Kullanacağımız recyclerView adapter
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

        // RecyclerView

        RecyclerView recyclerView = findViewById(R.id.postsList); // Get the recyclerView - RecyclerView içeri dahil et
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // Set a layout with this activity - Buradaki aktiviteyi içerik olarak arayüz atama

        feedRecyclerAdapter = new FeedRecyclerAdapter(userEmailOfFb, userCommentOfFb, userImageOfFb); // The data send to the constructor - Verileri yapıcı metoda gönder

        recyclerView.setAdapter(feedRecyclerAdapter); // Bind the adapter to the recyclerView - Adaptörü recyclerView bağla
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

                            feedRecyclerAdapter.notifyDataSetChanged(); // Notify the adapter - Adaptörü yeni veri geldiğine karşın uyarı
                        }
                    }
                }
            }
        });
    }
}