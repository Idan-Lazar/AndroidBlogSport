package com.example.idanl.blogsport.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.idanl.blogsport.Models.ViewModel.UserViewModel;
import com.example.idanl.blogsport.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditUserFragment extends Fragment {

    Button updateButton, changeButton, cancleButton;
    UserViewModel userViewModel;
    TextView nameTextView , emailTextView;
    ProgressBar progressBar;

    public EditUserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_user, container, false);
        updateButton = view.findViewById(R.id.user_edit_send_button);
        changeButton = view.findViewById(R.id.user_change_pass_button);
        cancleButton = view.findViewById(R.id.user_disable_acount_button);



        return view;
    }

}
