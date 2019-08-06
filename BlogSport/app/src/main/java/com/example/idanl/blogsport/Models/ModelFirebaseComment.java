package com.example.idanl.blogsport.Models;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.idanl.blogsport.Models.Entities.Comment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.LinkedList;
import java.util.List;

public class ModelFirebaseComment extends ModelFirebase {
    final public static ModelFirebaseComment instance = new ModelFirebaseComment();


    public static ModelFirebaseComment getInstance() {
        return instance;
    }

    private ModelFirebaseComment() {
        super();
        
        
    }
    
    void GetAllComments(final CommentRepository.GetAllCommentsListener listener, String postKey) {
    if (isNetworkConnected())
    {
        Log.d("Id", "GetAllComments: "+postKey);
        DocumentReference doc = db.collection("Posts").document(postKey);

        db.collection("Posts").document(postKey).collection("Comments").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                final List<Comment> commentList = new LinkedList<>();
                for (DocumentSnapshot doc: queryDocumentSnapshots
                ) {
                    Comment comment = doc.toObject(Comment.class);
                    commentList.add(comment);

                }
                listener.onResponse(commentList);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onError();
            }
        });
    }

    }

    void addComment(Comment p, final CommentRepository.InsertCommentListener listener) {
        if (isNetworkConnected())
        {
            String postKey = p.getPostId();
            Log.d("PostKey", "addComment: "+postKey);
            final DocumentReference doc = db.collection("Posts").document(postKey).collection("Comments").document();
            String key = doc.getId();
            p.setCommentKey(key);
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

    void removeComment(String commentId, String postId)
    {
        if(isNetworkConnected())
        {db.collection("Posts").document(postId).collection("Comments").document(commentId).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });}
        else
        {

        }
    }


}
