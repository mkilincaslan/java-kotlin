package com.example.instaclonefirebase;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FeedRecyclerAdapter extends RecyclerView.Adapter<FeedRecyclerAdapter.PostHolder> {

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
        holder.userEmail.setText("");
        holder.commentText.setText("");
    }

    @Override
    public int getItemCount() {
        // How many rows - Kaç adet satır olacak
        return 0;
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
