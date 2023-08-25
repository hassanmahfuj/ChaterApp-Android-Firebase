package com.hum.chaterapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.hum.chaterapp.R;
import com.hum.chaterapp.service.Firebase;

import java.util.HashMap;
import java.util.Map;

public class NewChatActivity extends AppCompatActivity {

    private EditText txtRecipientNumber;
    private Button btnStartChat;
    private ImageView actionBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_chat);

        txtRecipientNumber = findViewById(R.id.txt_recipient_number);
        btnStartChat = findViewById(R.id.btn_start_chat);
        actionBack = findViewById(R.id.action_back);

        btnStartChat.setOnClickListener(view -> {
            String[] participantIds = new String[]{
                    Firebase.use().getUserId(),
                    txtRecipientNumber.getText().toString()
            };
            for (String id : participantIds) {
                if (!Firebase.use().isUserRegistered(id)) {
                    showMessage(id + " is not registered!");
                    return;
                }
            }
            createNewChat("", participantIds);
        });

        actionBack.setOnClickListener(view -> finish());
    }

    private void createNewChat(String chatName, String[] participantIds) {
        DatabaseReference chatsRef = Firebase.use().ref().child("chats");

        String chatId = chatsRef.push().getKey();

        Map<String, Boolean> participants = new HashMap<>();
        for (String participantId : participantIds) {
            participants.put(participantId, true);
        }

        Map<String, Object> chatData = new HashMap<>();
        chatData.put("chatId", chatId);
        chatData.put("type", "private");
        chatData.put("participants", participants);

        chatsRef.child(chatId).setValue(chatData);

        Intent i = new Intent(NewChatActivity.this, MessagesActivity.class);
        i.putExtra("chatId", chatId);
        i.putExtra("name", txtRecipientNumber.getText().toString());
        startActivity(i);
        finish();
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}