package com.hum.chaterapp.service;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hum.chaterapp.model.User;

import java.util.Date;
import java.util.HashMap;

public class Firebase {
    public static final String USERS_NODE = "users";
    public static final String CHATS_NODE = "chats";

    private static Firebase firebaseInstance;
    private HashMap<String, User> users;

    private Firebase() {}

    // singleton solution
    public static synchronized Firebase use() {
        if(firebaseInstance == null) {
            firebaseInstance = new Firebase();
        }
        return firebaseInstance;
    }

    public DatabaseReference ref() {
        return FirebaseDatabase.getInstance().getReference();
    }

    public String getUserId() {
        return FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
    }

    public void saveUser(String userId, String phone) {
        HashMap<String, Object> m = new HashMap<>();
        m.put("userId", userId);
        m.put("phone", phone);
        ref().child(USERS_NODE).child(userId).updateChildren(m);
    }

    public void updateLastSeen() {
        HashMap<String, Object> m = new HashMap<>();
        m.put("lastSeen", new Date().getTime());
        ref().child(USERS_NODE).child(getUserId()).updateChildren(m);
    }

    public void getChatsByUserId() {

    }

    public void getMessagesByUserIdAndChatId() {

    }


    // get user details solution
    public void initUsers(Callback callback) {
        users = new HashMap<>();
        ref().child(USERS_NODE).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    users.put(data.getKey(), data.getValue(User.class));
                }
                callback.onComplete();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public User getUser() {
        return users.get(getUserId());
    }

    public User getUser(String userId) {
        return users.get(userId);
    }

    public boolean isCurrentUser(String userId) {
        return users.containsKey(userId);
    }

    public interface Callback {
        void onComplete();
    }
}
