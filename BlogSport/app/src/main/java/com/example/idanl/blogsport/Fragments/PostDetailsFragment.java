package com.example.idanl.blogsport.Fragments;
import static java.lang.Math.toIntExact;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.idanl.blogsport.Adapters.CommentAdapter;
import com.example.idanl.blogsport.Adapters.MyApplication;
import com.example.idanl.blogsport.Models.Comment;
import com.example.idanl.blogsport.Models.Entities.Post;
import com.example.idanl.blogsport.Models.ViewModel.PostDetailsViewModel;
import com.example.idanl.blogsport.Models.ViewModel.PostDetailsViewModelFactory;
import com.example.idanl.blogsport.Models.ViewModel.PostListViewModel;
import com.example.idanl.blogsport.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostDetailsFragment extends Fragment {

    boolean mProcessLike;
    PostDetailsViewModel mPostDetailsViewModel;
    TextView tv_Title,tv_SecondTitle,tv_Category,tv_Content,tv_Date_Writer, tv_Likes;
    ImageView image_Like_btn, image_post;
    CircleImageView  image_profile_comment, image_profile;
    Button btn_add_comment;
    EditText et_comment;
    RecyclerView commentRv ;
    ProgressBar progressBar, comment_ProgressBar;
    ConstraintLayout layout;
    CommentAdapter commentAdapter;
    List<Comment> comments;
    String postKey;
    Post p;
    public PostDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_post_details, container, false);
        tv_Title = v.findViewById(R.id.post_d_title);
        tv_SecondTitle = v.findViewById(R.id.post_d_sec_title);
        tv_Category = v.findViewById(R.id.post_d_category);
        tv_Content = v.findViewById(R.id.post_d_content);
        tv_Date_Writer = v.findViewById(R.id.post_d_writer_date);
        tv_Likes = v.findViewById(R.id.post_d_like_num);
        image_Like_btn = v.findViewById(R.id.post_d_like_btn);
        image_post = v.findViewById(R.id.post_d_image);
        image_profile_comment = v.findViewById(R.id.post_d_user_profile_comment);
        image_profile = v.findViewById(R.id.post_d_user_profile);
        btn_add_comment = v.findViewById(R.id.post_d_btn_add_comment);
        comment_ProgressBar = v.findViewById(R.id.comment_progressBar);
        et_comment = v.findViewById(R.id.post_d_comment_text);
        layout = v.findViewById(R.id.post_d_layout);
        progressBar = v.findViewById(R.id.post_d_progressBar);
        commentRv = v.findViewById(R.id.post_d_commet_RV);

        //ViewModel And LiveData Init
        assert getArguments() != null;
        postKey = PostDetailsFragmentArgs.fromBundle(getArguments()).getPostKey();
        Activity me = this.getActivity();
        PostDetailsViewModelFactory factory;
        if (me != null){
            factory = new PostDetailsViewModelFactory(this.getActivity().getApplication(),postKey);
            mPostDetailsViewModel = ViewModelProviders.of(this, factory).get(PostDetailsViewModel.class);
            //LiveData Observer Any change is updated
            mPostDetailsViewModel.getPost().observe(this, new Observer<Post>() {
                @Override
                public void onChanged(Post post) {
                    p = post;
                    populate();
                }
            });
        }

        layout.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        comment_ProgressBar.setVisibility(View.INVISIBLE);
        btn_add_comment.setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.floatingActionButton).setVisibility(View.INVISIBLE);



        /*image_Like_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProcessLike = true;
                databaseReferenceLikes.addValueEventListener(new ValueEventListener() {
                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(mProcessLike) {
                                if (dataSnapshot.child(p.getPostKey().toString()).hasChild(mAuth.getCurrentUser().getUid())) {
                                    databaseReferenceLikes.child(p.getPostKey().toString()).child(mAuth.getCurrentUser().getUid()).removeValue();
                                    image_Like_btn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ffffff")));
                                    mProcessLike = false;
                                } else {
                                    databaseReferenceLikes.child(p.getPostKey().toString()).child(mAuth.getCurrentUser().getUid()).setValue("Random_Value");
                                    image_Like_btn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ff0000")));
                                    mProcessLike = false;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

        });

                btn_add_comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        comment_ProgressBar.setVisibility(View.VISIBLE);
                        btn_add_comment.setVisibility(View.INVISIBLE);
                        DatabaseReference refComments = databaseReferenceComment.child(p.getPostKey()).push();
                        String comment_content = et_comment.getText().toString();
                        String uid = p.getUserId();
                        String uname = p.getUserName();
                        String uimg = p.getUserPhoto();
                        Comment c = new Comment(comment_content, uid, uimg, uname);

                        refComments.setValue(c).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                showMessage("comment added");
                                et_comment.setText("");
                                comment_ProgressBar.setVisibility(View.INVISIBLE);
                                btn_add_comment.setVisibility(View.VISIBLE);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                showMessage("Fail to add comment " + e.getMessage());
                                comment_ProgressBar.setVisibility(View.INVISIBLE);
                                btn_add_comment.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                });

        databaseReferenceLikes.addValueEventListener(new ValueEventListener() {
            @TargetApi(Build.VERSION_CODES.N)
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long num = dataSnapshot.child(p.getPostKey()).getChildrenCount();
                tv_Likes.setText(Long.toString(num));
                p.setLikes(toIntExact(num));
                postRef.child("likes").setValue(num);
                if (dataSnapshot.child(p.getPostKey().toString()).hasChild(mAuth.getCurrentUser().getUid())) {
                    image_Like_btn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ff0000")));
                } else {

                    image_Like_btn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ffffff")));
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
        initRVcomment();


        return v;
    }
    public void populate()
    {
        if (p!=null)
        {
            tv_Title.setText(p.getTitle());
            tv_SecondTitle.setText(p.getSecond_title());
            tv_Content.setText(p.getContent());
            tv_Category.setText(p.getCategory());
            tv_Likes.setText(Integer.toString(p.getLikes()));
            tv_Date_Writer.setText("Published at "+" By "+ p.getUserName());
            Glide.with(this).load(p.getUserPhoto()).into(image_profile);
            Glide.with(this).load(p.getUserPhoto()).into(image_profile_comment);
            Glide.with(this).load(p.getPicture()).into(image_post);


            layout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);

        }
    }
    @Override
    public void onStart() {
        super.onStart();



    }

    private void initRVcomment() {
        /*commentRv.setLayoutManager(new LinearLayoutManager(MyApplication.getContext()));
        commentRv.setAdapter(commentAdapter);
        DatabaseReference refComments = databaseReferenceComment.child(p.getPostKey());
        refComments.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                comments = new ArrayList<>();
                for (DataSnapshot snapshot: dataSnapshot.getChildren())
                {

                    Comment c = snapshot.getValue(Comment.class);
                    comments.add(c);
                }
                commentAdapter = new CommentAdapter(getContext(),comments);
                commentRv.setAdapter(commentAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().findViewById(R.id.floatingActionButton).setVisibility(View.VISIBLE);
    }
    @Override
    public void onStop() {
        super.onStop();
        getActivity().findViewById(R.id.floatingActionButton).setVisibility(View.VISIBLE);
    }

    private String timestamptoString(long timestamp)

    {
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(timestamp);
        String date = DateFormat.format("dd-MM-yyyy",calendar).toString();
        return date;

    }
    private void showMessage(String message) {
        Toast.makeText(getContext(),message,Toast.LENGTH_LONG).show();
    }
}
