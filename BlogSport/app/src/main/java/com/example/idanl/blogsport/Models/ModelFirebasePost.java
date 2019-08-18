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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Comparator;
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

        this.setGetAllPostsListener(db.collectionGroup("Posts").orderBy("timestamp", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e!=null){
                    Log.d("Firestore","Error:"+e.getMessage());
                }
                else
                {
                    Log.d("FIrebase", "onEvent: firebase getall posts");
                    if (!isNetworkConnected())
                    {
                        listener.onError();
                    }
                    else {
                        final LinkedList<Post> data = new LinkedList<>();
                        for (DocumentSnapshot doc : queryDocumentSnapshots
                        ) {
                            Post p = doc.toObject(Post.class);
                            if (!p.isDeleted())
                            {
                                data.add(p);
                            }

                        }
                        listener.onResponse(data);

                    }
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
            String userId = p.getUserId();
            final DocumentReference doc = db.collection("Users").document(userId).collection("Posts").document();
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
            String userId = p.getUserId();
            db.collection("Users").document(userId).collection("Posts").document(p.getPostKey()).set(p).addOnSuccessListener(new OnSuccessListener<Void>() {
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



     void getPost(String postKey, final PostRepository.GetPostListener listener) {
        if (isNetworkConnected())
        {
            db.collectionGroup("Posts").whereEqualTo("postKey", postKey).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                    if (e!=null){
                        Log.d("Firestore","Error:"+e.getMessage());
                    }
                    else
                    {
                       DocumentSnapshot snapshot = queryDocumentSnapshots.getDocuments().get(0);
                       final Post p = snapshot.toObject(Post.class);
                       listener.onResponse(p);

                    }




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


    public void isPostExist(String postKey, final PostRepository.ExistPostListener listener) {
        if (isNetworkConnected())
        {
            db.collectionGroup("Posts").whereEqualTo("deleted",false).whereEqualTo("postKey",postKey).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if(queryDocumentSnapshots.size() == 0)
                    {
                        listener.onNotExist();
                    }
                    else{
                        listener.onExist();
                    }
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
}
