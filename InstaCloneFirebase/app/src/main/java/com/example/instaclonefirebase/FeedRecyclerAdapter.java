package com.example.instaclonefirebase;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class FeedRecyclerAdapter extends RecyclerView.Adapter<FeedRecyclerAdapter.PostHolder> {

    // Use the data in recyclerView - RecyclerView içerisinde kullancağımız veriler
    private ArrayList<String> userEmailList;
    private ArrayList<String> postCommentList;
    private ArrayList<String> postImageList;

    public FeedRecyclerAdapter(ArrayList<String> userEmailList, ArrayList<String> postCommentList, ArrayList<String> postImageList) {
        // Get the data on constructor - Verileri yapıcı metod içerisinde ata
        this.userEmailList = userEmailList;
        this.postCommentList = postCommentList;
        this.postImageList = postImageList;
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Binding

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_row, parent, false);

        return new PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {
        holder.userEmail.setText(userEmailList.get(position));
        holder.commentText.setText(postCommentList.get(position));
    }

    @Override
    public int getItemCount() {
        // How many rows - Kaç adet satır olacak
        return userEmailList.size();
    }

    class PostHolder extends RecyclerView.ViewHolder {
        ImageView postImage;
        TextView userEmail;
        TextView commentText;

        public PostHolder(@NonNull View itemView) {
            super(itemView);

            postImage = itemView.findViewById(R.id.imgPostImage);
            userEmail = itemView.findViewById(R.id.txtUserEmail);
            commentText = itemView.findViewById(R.id.txtComment);
        }
    }
}
