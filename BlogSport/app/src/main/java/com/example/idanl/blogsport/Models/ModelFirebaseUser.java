package com.example.idanl.blogsport.Models;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.idanl.blogsport.Models.Entities.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import javax.annotation.Nullable;

public class ModelFirebaseUser extends ModelFirebase {
    final public static ModelFirebaseUser instance = new ModelFirebaseUser();

    public void saveUserImage(Uri pickerImgUri, final UserRepository.SaveImageListener listener) {
        if (isNetworkConnected())
        {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("user_images");
            final StorageReference imageFilePath = storageReference.child(pickerImgUri.getLastPathSegment());
            imageFilePath.putFile(pickerImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            listener.onComplete(uri);
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

    public ModelFirebaseUser() {
        this.UsersChangeListener();
    }

    public void getCurrentUser(UserRepository.GetUserListener listener) {
        getUser(mAuth.getCurrentUser().getUid(),listener);
    }

    public void signIn(String email, String password, final UserRepository.SignInListener listener) {
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
    public void signUp(String email, String password, final UserRepository.SignUpListener listener) {
        if (isNetworkConnected())
            mAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    if (authResult != null) {
                        listener.onSuccess();


                    }


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    listener.onFailer(e);
                }
            });
        else
        {
            listener.onOffiline();
        }
    }

    public void updateUserInfo(final String userName, final Uri pickerImgUri, final FirebaseUser currentUser, final UserRepository.UpdateUserInfoListener listener)
    {
        if (isNetworkConnected()) {
            saveUserImage(pickerImgUri, new UserRepository.SaveImageListener() {
                @Override
                public void onComplete(final Uri uri) {

                    UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder().setDisplayName(userName).setPhotoUri(uri).build();
                    Log.d("Register image user", "onComplete: Update user "+ uri.toString());
                    currentUser.updateProfile(profileUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                String email = currentUser.getEmail();
                                if (email!=null)
                                {
                                    assert currentUser.getPhotoUrl() != null;
                                    User user = new User(currentUser.getUid(), email, currentUser.getDisplayName(),uri.toString());
                                    db.collection("Users").document(currentUser.getUid()).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            listener.onSuccess(uri.toString());
                                        }

                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            listener.onFailer(e);
                                        }
                                    });


                                }

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
    private void updateUserInfoPosts(User user)
    {
        final HashMap<String,Object> m = new HashMap<>();
        m.put("userName",user.getName());
        m.put("userImage",user.getUserImage());
        db.collection("Posts").whereEqualTo("userId",user.getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                WriteBatch batch = db.batch();
                for (QueryDocumentSnapshot doc: queryDocumentSnapshots
                ) {
                    batch.update(doc.getReference(),m);
                }
                batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });
            }
        });
    }
    private void UsersChangeListener()
    {
        db.collection("Users").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                assert queryDocumentSnapshots!=null;
                for (DocumentChange doc: queryDocumentSnapshots.getDocumentChanges()) {
                    if(doc.getType() == DocumentChange.Type.MODIFIED)
                    {
                        User u = doc.getDocument().toObject(User.class);
                        updateUserInfoPosts(u);
                    }
                }
            }
        });
    }

    public void getUser(String id, final UserRepository.GetUserListener listener) {
        if (isNetworkConnected())
        {
            db.collection("Users").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot snapshot = task.getResult();
                        if (snapshot != null)
                        {
                            User user = snapshot.toObject(User.class);
                            if (user!=null)
                            {
                                listener.onResponse(user);
                            }
                        }}}}).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    listener.onError();
                }
            });
        }



    }


}