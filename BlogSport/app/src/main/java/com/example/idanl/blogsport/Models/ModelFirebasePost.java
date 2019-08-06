package com.example.idanl.blogsport.Models;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.idanl.blogsport.Models.Entities.Post;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.AbstractExecutorService;

public class ModelFirebasePost extends ModelFirebase {
    final public static ModelFirebasePost instance = new ModelFirebasePost();

    public ListenerRegistration getGetAllPostsListener() {
        return getAllPostsListener;
    }

    public void setGetAllPostsListener(ListenerRegistration getAllPostsListener) {
        this.getAllPostsListener = getAllPostsListener;
    }

    private ListenerRegistration getAllPostsListener;
    public static ModelFirebasePost getInstance() {
        return instance;
    }

    private ModelFirebasePost() {
        super();


    }

    void activateGetAllPostsListener(final PostRepository.GetAllPostsListener listener) {

        this.setGetAllPostsListener(db.collection("Posts").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                Log.d("FIrebase", "onEvent: firebase getall posts");
                if (!isNetworkConnected())
                {
                    listener.onError();
                }
                else {
                    db.collection("Posts").whereEqualTo("deleted",false).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            final List<Post> data = new ArrayList<>();
                            if (!isNetworkConnected())
                            {

                                listener.onError();
                            }
                            else {
                                for (DocumentSnapshot doc : queryDocumentSnapshots
                                ) {
                                    Post p = doc.toObject(Post.class);
                                    data.add(p);
                                }

                                listener.onResponse(data);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            listener.onError();
                        }
                    });

                }


            }
        }));
    }
    void removeGetAllPostsListener()
    {
        this.getAllPostsListener.remove();
    }

    void addPost(Post p, final PostRepository.InsertPostListener listener) {
        if (isNetworkConnected())
        {

            final DocumentReference doc = db.collection("Posts").document();
            String key = doc.getId();
            p.setPostKey(key);
            doc.collection("Comments");
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


    void updatePost(Post p, final PostRepository.InsertPostListener listener)
    {
        if(!isNetworkConnected())
        {
            listener.onOffline();
        }
        else
        {
            db.collection("Posts").document(p.getPostKey()).set(p).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    listener.onComplete(true);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    listener.onError(e);
                }
            });
        }

    }



     void getPost(String id, final PostRepository.GetPostListener listener) {
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
