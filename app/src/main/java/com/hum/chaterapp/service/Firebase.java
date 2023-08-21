package com.hum.chaterapp.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hum.chaterapp.model.User;

import java.util.Date;
import java.util.HashMap;

public class Firebase {
    public static final String USERS_NODE = "users";
    public static final String CHATS_NODE = "chats";

    private DatabaseReference databaseReference;
    private String userId;
    private static Firebase firebaseInstance;

    private Firebase() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        userId = FirebaseAuth.getInstance().getUid();
    }

    public static Firebase get() {
        if(firebaseInstance == null) {
            firebaseInstance = new Firebase();
        }
        return firebaseInstance;
    }

    public DatabaseReference ref() {
        return databaseReference;
    }

    public void saveUser(String userId, String phone) {
        HashMap<String, Object> m = new HashMap<>();
        m.put("userId", userId);
        m.put("phone", phone);
        databaseReference.child(USERS_NODE).child(userId).updateChildren(m);
    }

    public void updateLastSeen() {
        HashMap<String, Object> m = new HashMap<>();
        m.put("lastSeen", new Date().getTime());
        databaseReference.child(USERS_NODE).child(userId).updateChildren(m);
    }

    public void getChatsByUserId() {

    }

    public void getMessagesByUserIdAndChatId() {

    }
}
