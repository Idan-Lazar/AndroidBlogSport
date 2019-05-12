package com.example.idanl.blogsport.Activities;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.idanl.blogsport.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity{

    CircleImageView ImgUserPhoto = null;
    static int PReqCode = 1;
    static int REQUESTCODE = 1;
    Uri pickerImgUri = null;
    private FirebaseAuth mAuth;

    private EditText userEmail,userName,userPassword,userPassword2;
    private ProgressBar loadingProgress;
    private Button btn;
    private TextView signin_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registar);

        //initals
        userEmail = findViewById(R.id.regMail);
        userPassword = findViewById(R.id.regPassword);
        signin_btn = findViewById(R.id.reg_signin_btn);
        userPassword2 = findViewById(R.id.regPassword2);
        userName = findViewById(R.id.regName);
        loadingProgress = findViewById(R.id.regPogressBar);
        btn = findViewById(R.id.regBtn);
        loadingProgress.setVisibility(View.INVISIBLE);
        mAuth = FirebaseAuth.getInstance();
        signin_btn.setPaintFlags(signin_btn.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        signin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signinActivity = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(signinActivity);
                finish();
            }
        });
        
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn.setVisibility(View.INVISIBLE);
                loadingProgress.setVisibility(View.VISIBLE);
                final String email = userEmail.getText().toString();
                final String name = userName.getText().toString();
                final String password = userPassword.getText().toString();
                final String password2 = userPassword2.getText().toString();
                
                
                ///Something went worng
                if(email.isEmpty() || name.isEmpty() || password.isEmpty() || password2.isEmpty() || !password.equals(password2) || pickerImgUri==null)
                {
                    if(pickerImgUri==null)
                    {
                        showMessage("Please add picture and verify all fields");
                    }
                    else
                    {
                        showMessage("Please Verify all fields");
                    }

                    btn.setVisibility(View.VISIBLE);
                    loadingProgress.setVisibility(View.INVISIBLE);
                }
                else{
                    CreateUserAccount(email,name,password);
                }
            }
        });

        ImgUserPhoto = findViewById(R.id.regUserPhoto);
        ImgUserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >=22){
                    checkandrequestforpermission();

                }
                else{
                    openGallery();

                }
            }
        });
    }

    private void CreateUserAccount(String email, final String name, String password) {
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    showMessage("Account Created");
                    UpdateUserInfo(name,pickerImgUri,mAuth.getCurrentUser());
                }
                else{
                    showMessage("Account Creation Failed " + task.getException());
                    btn.setVisibility(View.VISIBLE);
                    loadingProgress.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void UpdateUserInfo(final String userName, Uri pickerImgUri, final FirebaseUser currentUser) {

            StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("users_photos");
            final StorageReference imageFilePath = mStorage.child(pickerImgUri.getLastPathSegment());
            imageFilePath.putFile(pickerImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    // image uploaded succesfully
                    // now we can get the image url
                    imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            // uri contain user image url
                            UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder().setDisplayName(userName).setPhotoUri(uri).build();
                            currentUser.updateProfile(profileUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        // user info updated succesfully
                                        showMessage("Register Complete");
                                        updateUI();
                                    }
                                }
                            });
                        }
                    });
                }
            });


    }

    private void updateUI() {
        Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(mainActivity);
        finish();
    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == REQUESTCODE && data!= null)
        {
            pickerImgUri = data.getData();
            ImgUserPhoto.setImageURI(pickerImgUri);
        }
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,REQUESTCODE);
    }

    private void checkandrequestforpermission() {
        if(ContextCompat.checkSelfPermission(RegisterActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {

           ActivityCompat.requestPermissions(RegisterActivity.this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},PReqCode);

        }
        else{
            openGallery();
        }
    }
}
