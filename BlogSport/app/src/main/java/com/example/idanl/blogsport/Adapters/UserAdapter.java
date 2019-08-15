package com.example.idanl.blogsport.Adapters;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.idanl.blogsport.Fragments.HomeFragmentDirections;
import com.example.idanl.blogsport.Models.Entities.User;
import com.example.idanl.blogsport.R;

import java.util.LinkedList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {

    private LinkedList<User> mUsers ;

    public UserAdapter() {
        this.mUsers = new LinkedList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_user_item, parent,false);
        
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }


    public void setUsers(LinkedList<User> users) {
        mUsers = users;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    

    @Override
    public void onBindViewHolder(@NonNull final  MyViewHolder holder, int position) {

        holder.layout.setVisibility(View.INVISIBLE);
        final User user = mUsers.get(position);
        holder.tvWriterName.setText(user.getName());
        Glide.with(MyApplication.getContext()).load(user.getUserImage()).into(holder.imgUserProfile);
        holder.tvTitle.setText(user.getTitle());
        holder.tvSecondTitle.setText(user.getSecond_title());
        holder.tvCategory.setText(user.getCategory());
        Glide.with(MyApplication.getContext()).load(user.getPicture()).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                holder.progressBar.setVisibility(View.INVISIBLE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                holder.progressBar.setVisibility(View.INVISIBLE);
                holder.layout.setVisibility(View.VISIBLE);
                return false;
            }
        }).into(holder.imgUser);

    }



    public class MyViewHolder extends  RecyclerView.ViewHolder{

        TextView tvTitle, tvSecondTitle, tvCategory, tvWriterName;
        ImageView imgUser, imgUserProfile;
        ProgressBar progressBar;
        ConstraintLayout layout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.layout_user_item);
            progressBar = itemView.findViewById(R.id.user_item_progress);
            tvTitle = itemView.findViewById(R.id.user_item_title);
            tvSecondTitle = itemView.findViewById(R.id.user_item_sec_title);
            tvCategory = itemView.findViewById(R.id.user_item_category);
            tvWriterName = itemView.findViewById(R.id.user_item_writer_name);
            imgUser = itemView.findViewById(R.id.user_item_image);
            imgUserProfile = itemView.findViewById(R.id.item_user_image_card);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    HomeFragmentDirections.ActionHomeFragmentToUserDetailsFragment action = HomeFragmentDirections.actionHomeFragmentToUserDetailsFragment(mUsers.get(position).getUserKey());
                    Navigation.findNavController(v)
                            .navigate(action);
                }
            });
        }
    }
}
