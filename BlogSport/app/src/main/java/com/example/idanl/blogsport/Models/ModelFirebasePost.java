package com.example.idanl.blogsport.Models;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

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
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.LinkedList;

public class ModelFirebasePost extends ModelFirebase {
    final public static ModelFirebasePost instance = new ModelFirebasePost();
    public void getAllPosts(final PostRepository.GetAllPostsListener listener) {

        db.collection("Posts").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                Log.d("FIrebase", "onEvent: firebase getall posts");
                final LinkedList<Post> data = new LinkedList<>();
                if (!isNetworkConnected())
                {
                    listener.onError();
                }
                for (DocumentSnapshot doc: queryDocumentSnapshots
                ) {
                    Post p = doc.toObject(Post.class);
                    data.add(p);
                }
                listener.onResponse(data);
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




    public void getPost(String id, final PostRepository.GetPostListener listener) {
        if (isNetworkConnected())
        {
            db.collection("Posts").document(id).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@javax.annotation.Nullable DocumentSnapshot doc, @javax.annotation.Nullable FirebaseFirestoreException e) {
                    assert doc != null;
                    final Post post = doc.toObject(Post.class);
                    assert post != null;
                    listener.onResponse(post);




                }
            });
        }
        else {
            listener.onError();



        }}



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



}
