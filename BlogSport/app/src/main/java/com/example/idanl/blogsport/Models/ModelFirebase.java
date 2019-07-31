package com.example.idanl.blogsport.Models;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.idanl.blogsport.Adapters.MyApplication;
import com.example.idanl.blogsport.Models.Entities.Post;
import com.example.idanl.blogsport.Models.Entities.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.LinkedList;
import java.util.concurrent.Executor;

public class ModelFirebase {
    final public static ModelFirebase instance = new ModelFirebase();
    FirebaseFirestore db;
    FirebaseAuth mAuth;


    public ModelFirebase() {
        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false).build();

        db.setFirestoreSettings(settings);
        mAuth = FirebaseAuth.getInstance();
    }


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager)MyApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public void getAllPosts(final PostRepository.GetAllPostsListener listener) {

        db.collection("Posts").addSnapshotListener(new EventListener<QuerySnapshot>() {

            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                LinkedList<Post> data = new LinkedList<>();
                if (db.getFirestoreSettings().isPersistenceEnabled()){Log.d("Firebase","is 1" );}

                if (!isNetworkConnected())
                {
                    listener.onError();
                }
                else
                {
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Post post = doc.toObject(Post.class);
                        data.add(post);
                    }
                    listener.onResponse(data);
                }

            }
        });
    }

    public void addPost(Post p, final PostRepository.InsertPostListener listener) {
        if (isNetworkConnected())
        {
            final DocumentReference doc = db.collection("Posts").document();
            String key = doc.getId();
            p.setPostKey(key);
            doc.set(p).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    listener.onComplete(task.isSuccessful());
                }


            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    listener.onError(e);

                }
            });
        }
        else
        {
            listener.onOffline();
        }

    }

    public void signUp(String email, String password, final UserReposiroty.SignUpListener listener) {
        if (isNetworkConnected()) {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    listener.onSuccess();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    listener.onFailer(e);
                }
            });}
        else
        {
            listener.onOffiline();
        }
    }

    public void updateUserInfo(final String userName, final Uri pickerImgUri, final FirebaseUser currentUser, final UserReposiroty.UpdateUserInfoListener listener)
    {
        if (isNetworkConnected()) {
            saveUserImage(pickerImgUri, new PostRepository.SaveImageListener() {
                @Override
                public void onComplete(String uri) {
                    UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder().setDisplayName(userName).setPhotoUri(pickerImgUri).build();
                    currentUser.updateProfile(profileUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                listener.onSuccess();
                            }
                        }
                    });
                }

                @Override
                public void onOffline() {
                    listener.onOffiline();
                }

                @Override
                public void onError(Exception e) {
                    listener.onFailer(e);
                }
            });
        }
        else{
            listener.onOffiline();
        }
    }





    public void getPost(String id, final PostRepository.GetPostListener listener) {
        if (isNetworkConnected())
        {
            db.collection("Posts").document(id).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot snapshot = task.getResult();
                                if (snapshot != null)
                                {
                                    Post post = snapshot.toObject(Post.class);
                                    if (post!=null)
                                    {
                                        listener.onResponse(post);
                                    }
                                    else
                                    {
                                        listener.onError();
                                    }
                                }


                            }

                        }
                    });
        }
        else {
            listener.onError();
        }


    }



    public void saveBlogImage(Uri pickerImgUri, final PostRepository.SaveImageListener listener) {
        if (isNetworkConnected())
        {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("blog_images");
            final StorageReference imageFilePath = storageReference.child(pickerImgUri.getLastPathSegment());
            imageFilePath.putFile(pickerImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            listener.onComplete(uri.toString());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            listener.onError(e);
                        }
                    });
                }
            });
        }
        else
        {
            listener.onOffline();
        }
    }

    public void saveUserImage(Uri pickerImgUri, final PostRepository.SaveImageListener listener) {
        if (isNetworkConnected())
        {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("users_images");
            final StorageReference imageFilePath = storageReference.child(pickerImgUri.getLastPathSegment());
            imageFilePath.putFile(pickerImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            listener.onComplete(uri.toString());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            listener.onError(e);
                        }
                    });
                }
            });
        }
        else
        {
            listener.onOffline();
        }
    }

    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    public void signIn(String email, String password, final UserReposiroty.SignInListener listener) {
        if (isNetworkConnected()) {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful())
                        listener.onSuccess();
                    else
                    {
                        listener.onFailer(task.getException());
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    listener.onFailer(e);
                }
            });
        }
        else
        {
            listener.onOffiline();
        }
    }

    public boolean isSigned() {
        if(mAuth.getCurrentUser() != null)
        {
            return true;
        }
        return false;
    }

    public Uri getUserImageUrl() {
        if(getCurrentUser()!=null)
        {
            return getCurrentUser().getPhotoUrl();
        }
        return null;
    }

    public String getUid() {
        if(getCurrentUser()!=null)
        {
            return getCurrentUser().getUid();
        }
        return null;
    }

    public String getDisplayName() {
        if(getCurrentUser()!=null)
        {
            return getCurrentUser().getDisplayName();
        }
        return null;
    }
}
