package com.example.instagramparseclone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class PostActivity extends AppCompatActivity {

    EditText commentEditText;
    ImageView postImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        commentEditText = findViewById(R.id.txtComment);
        postImageView = findViewById(R.id.postImg);
    }

    public void post(View view) {

    }
}