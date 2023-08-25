package com.hum.chaterapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.hum.chaterapp.R;
import com.hum.chaterapp.model.Chat;
import com.hum.chaterapp.service.Firebase;

import java.util.HashMap;
import java.util.Map;

public class NewChatActivity extends AppCompatActivity {

    private TextInputEditText txtRecipientNumber;
    private Button btnStartChat;
    private ImageView actionBack;
    private String recipientNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_chat);

        txtRecipientNumber = findViewById(R.id.txt_recipient_number);
        btnStartChat = findViewById(R.id.btn_start_chat);
        actionBack = findViewById(R.id.action_back);

        btnStartChat.setOnClickListener(view -> {
            recipientNumber = "+88" + txtRecipientNumber.getText().toString();
            String[] participantIds = new String[]{
                    Firebase.use().getUserId(),
                    recipientNumber
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

        HashMap<String, Boolean> participants = new HashMap<>();
        for (String participantId : participantIds) {
            participants.put(participantId, true);
        }

        Chat chat = new Chat();
        chat.setChatId(chatId);
        chat.setType("private");
        chat.setParticipants(participants);

//        Map<String, Object> chatData = new HashMap<>();
//        chatData.put("chatId", chatId);
//        chatData.put("type", "private");
//        chatData.put("participants", participants);

        chatsRef.child(chatId).setValue(chat);

        Intent i = new Intent(NewChatActivity.this, MessagesActivity.class);
        i.putExtra("chatId", chatId);
        i.putExtra("name", recipientNumber);
        startActivity(i);
        finish();
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}