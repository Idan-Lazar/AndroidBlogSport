package com.example.idanl.blogsport.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.idanl.blogsport.Fragments.HomeFragmentDirections;
import com.example.idanl.blogsport.Models.Comment;
import com.example.idanl.blogsport.Models.Post;
import com.example.idanl.blogsport.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {


        Context mContext;
        List<Comment> mData;

        public CommentAdapter(Context mContext, List<Comment> mData) {
            this.mContext = mContext;
            this.mData = mData;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View row = LayoutInflater.from(mContext).inflate(R.layout.row_comment, parent, false);

            return new MyViewHolder(row);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


            Comment selected = mData.get(position);

            holder.tvContent.setText(selected.getContent());
            holder.tvWriterName.setText(selected.getUname());
            Glide.with(mContext).load(selected.getUimg()).into(holder.imgCommentUserProfile);



        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        public class MyViewHolder extends  RecyclerView.ViewHolder{

            TextView tvContent,tvWriterName;
            ImageView imgCommentUserProfile;
            public MyViewHolder(@NonNull View itemView) {
                super(itemView);


                tvContent = itemView.findViewById(R.id.comment_item_content);
                tvWriterName = itemView.findViewById(R.id.comment_item_user);
                imgCommentUserProfile = itemView.findViewById(R.id.comment_item_writer_image);


            }
        }


}
