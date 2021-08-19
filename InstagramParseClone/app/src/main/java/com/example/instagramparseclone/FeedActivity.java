package com.example.instagramparseclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.LogOutCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FeedActivity extends AppCompatActivity {
    
    ListView postList;
    ArrayList<String> usernameList;
    ArrayList<String> userCommentList;
    ArrayList<Bitmap> postImageList;
    ArrayList<Date> postDateList;
    PostClass postClassAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.add_post) {
            Intent intent = new Intent(getApplicationContext(), PostActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.logout) {
            ParseUser.logOutInBackground(new LogOutCallback() {
                @Override
                public void done(ParseException e) {
                    if (e != null) {
                        Toast.makeText(FeedActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                        startActivity(intent);
                    }
                }
            });
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        postList = findViewById(R.id.postListView);

        usernameList = new ArrayList<>();
        userCommentList = new ArrayList<>();
        postImageList = new ArrayList<>();
        postDateList = new ArrayList<>();

        postClassAdapter = new PostClass(usernameList, userCommentList, postImageList, postDateList, this);

        postList.setAdapter(postClassAdapter);

        getData();
    }

    public void getData() {
        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("Posts");
        parseQuery.findInBackground((objects, e) -> {
            if (e != null) {
                Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            } else {
                if (objects.size() > 0) {
                    for (final ParseObject object : objects) {
                        ParseFile file = (ParseFile) object.get("image");
                        System.out.println("file " + file);
                        if (file != null) {
                            file.getDataInBackground((byte[] data, ParseException e1) -> {
                                if (e1 == null && data != null) {
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

                                    postImageList.add(bitmap);
                                    usernameList.add(object.getString("username"));
                                    userCommentList.add(object.getString("comment"));
                                    postDateList.add(object.getCreatedAt());

                                    postClassAdapter.notifyDataSetChanged();
                                } else {
                                    Toast.makeText(getApplicationContext(), e1.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                }
            }
        });
    }
}