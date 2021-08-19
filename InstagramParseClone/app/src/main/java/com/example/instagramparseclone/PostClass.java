package com.example.instagramparseclone;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Date;

public class PostClass extends ArrayAdapter<String> {

    private final ArrayList<String> username;
    private final ArrayList<String> userComment;
    private final ArrayList<Bitmap> postImages;
    private final ArrayList<Date> postDateList;
    private final Activity context;

    public PostClass(ArrayList<String> username, ArrayList<String> userComments, ArrayList<Bitmap> postImages, ArrayList<Date> postDates, Activity context) {
        super(context, R.layout.post_view, username);
        this.username = username;
        this.userComment = userComments;
        this.postImages = postImages;
        this.postDateList = postDates;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // Modify postView.xml to object here
        // That means we can use and contact with postView.xml with this way
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View postView = layoutInflater.inflate(R.layout.post_view, null, true);
        TextView usernameText = postView.findViewById(R.id.txtUsername_postView);
        TextView commentText = postView.findViewById(R.id.txtComment_postView);
        TextView dateText = postView.findViewById(R.id.dateText);
        ImageView imageView = postView.findViewById(R.id.postImage_postView);

        usernameText.setText(username.get(position));
        commentText.setText(userComment.get(position));
        dateText.setText(postDateList.get(position).toLocaleString());
        imageView.setImageBitmap(postImages.get(position));

        return postView;
    }
}
