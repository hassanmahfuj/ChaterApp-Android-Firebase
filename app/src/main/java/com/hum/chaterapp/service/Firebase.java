package com.hum.chaterapp.service;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hum.chaterapp.model.Message;
import com.hum.chaterapp.model.User;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Firebase {
    public static final String USERS_NODE = "users";
    public static final String CHATS_NODE = "chats";
    public static final String MESSAGES_NODE = "messages";

    private static Firebase firebaseInstance;
    private HashMap<String, User> users;

    private Firebase() {
    }

    // singleton solution
    public static synchronized Firebase use() {
        if (firebaseInstance == null) {
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

    public DatabaseReference getMessagesByChatId(String chatId) {
        return ref().child(MESSAGES_NODE).child(chatId);
    }

    public void sendMessage(String chatId, Message message) {
        DatabaseReference messagesRef = ref().child(MESSAGES_NODE).child(chatId);
        String messageId = messagesRef.push().getKey();
        messagesRef.child(messageId).setValue(message);

        // set last message to chat node
        DatabaseReference chatsRef = ref().child(CHATS_NODE).child(chatId);
        chatsRef.child("lastMessage").setValue(message);
    }

    // get user details solution
    public void initUsers(Callback callback) {
        users = new HashMap<>();
        ref().child(USERS_NODE).addListenerForSingleValueEvent(new ValueEventListener() {
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

    public void updateUser(User user) {
        ref().child(USERS_NODE).child(user.getUserId()).setValue(user);
    }

    public User getUser() {
        return users.get(getUserId());
    }

    public User getUser(String userId) {
        return users.get(userId);
    }

    public boolean isUserRegistered(String userId) {
        return users.containsKey(userId);
    }

    public String getRecipientName(HashMap<String, Boolean> participants) {
        String name = "ChaterApp User";
        String[] ids = participants.keySet().toArray(new String[0]);
        for(String id : ids) {
            if(!Firebase.use().getUserId().equals(id)) {
                name = Firebase.use().getUser(id).getName();
            }
        }
        return name;
    }

    public void createNewUser(User user) {
        ref().child(USERS_NODE).child(user.getUserId()).setValue(user);
    }

    public interface Callback {
        void onComplete();
    }
}
