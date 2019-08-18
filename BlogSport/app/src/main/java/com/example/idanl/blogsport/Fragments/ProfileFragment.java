package com.example.idanl.blogsport.Fragments;


import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.idanl.blogsport.Activities.LoginActivity;
import com.example.idanl.blogsport.Models.ViewModel.UserProfileViewModel;
import com.example.idanl.blogsport.Models.ViewModel.UserProfileViewModelFactory;
import com.example.idanl.blogsport.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    Button signOutButton;
    Button editProfileButton;
    TextView nameTextView, emailTextView;
    CircleImageView userImageProfile;
    RecyclerView postsRecyclerView;
    UserProfileViewModel userProfileViewModel;
    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View  v = inflater.inflate(R.layout.fragment_profile, container, false);
        signOutButton = v.findViewById(R.id.user_profile_signout_button);
        editProfileButton= v.findViewById(R.id.user_profile_edit_button);
        nameTextView = v.findViewById(R.id.user_profile_name_tv);
        emailTextView = v.findViewById(R.id.user_profile_email_tv);
        userImageProfile = v.findViewById(R.id.user_profile_image);
        postsRecyclerView = v.findViewById(R.id.profile_user_post_list);
        UserProfileViewModelFactory factory = new UserProfileViewModelFactory(getActivity().getApplication(), )
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                Intent loginActivity = new Intent(getActivity(), LoginActivity.class);
                startActivity(loginActivity);
                getActivity().finish();
            }
        });

        return v;
    }

}
