package com.hum.chaterapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.hum.chaterapp.R;
import com.hum.chaterapp.model.Chat;
import com.hum.chaterapp.service.Firebase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NewChatActivity extends AppCompatActivity {

    private TextInputEditText txtRecipientNumber;
    private Button btnAdd;
    private Button btnStartChat;
    private ImageView actionBack;
    private ArrayList<String> participantIds;
    private LinearLayout linGroupChat;
    private EditText txtGroupChatName;
    private TextView txtParticipantsIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_chat);

        txtRecipientNumber = findViewById(R.id.txt_recipient_number);
        btnAdd = findViewById(R.id.btn_add);
        btnStartChat = findViewById(R.id.btn_start_chat);
        actionBack = findViewById(R.id.action_back);
        linGroupChat = findViewById(R.id.lin_group_chat);
        txtGroupChatName = findViewById(R.id.txt_group_chat_name);
        txtParticipantsIds = findViewById(R.id.txt_participants_ids);

        participantIds = new ArrayList<>();

        btnAdd.setOnClickListener(view -> {
            String recipientNumber = "+88" + txtRecipientNumber.getText().toString();
            if (!Firebase.use().isUserRegistered(recipientNumber)) {
                showMessage(recipientNumber + " is not registered!");
                return;
            }
            participantIds.add(recipientNumber);
            txtParticipantsIds.setText(txtParticipantsIds.getText().toString().concat(recipientNumber).concat("\n"));
            txtRecipientNumber.setText("");

            linGroupChat.setVisibility(View.VISIBLE);
        });

        btnStartChat.setOnClickListener(view -> {
            if (participantIds.size() > 0) {
                String groupChatName = txtGroupChatName.getText().toString().trim();
                if (groupChatName.equals("")) {
                    showMessage("Enter a group name");
                    return;
                }
                participantIds.add(Firebase.use().getUserId());
                createNewChat(groupChatName, "group", participantIds);
            } else {
                String recipientNumber = "+88" + txtRecipientNumber.getText().toString();
                if (!Firebase.use().isUserRegistered(recipientNumber)) {
                    showMessage(recipientNumber + " is not registered!");
                    return;
                }
                participantIds.add(recipientNumber);
                participantIds.add(Firebase.use().getUserId());

                createNewChat(Firebase.use().getUser(recipientNumber).getName(), "private", participantIds);
            }
        });

        actionBack.setOnClickListener(view -> finish());
    }

    private void createNewChat(String chatName, String chatType, ArrayList<String> participantIds) {
        DatabaseReference chatsRef = Firebase.use().ref().child("chats");
        String chatId = chatsRef.push().getKey();

        HashMap<String, Boolean> participants = new HashMap<>();
        for (String participantId : participantIds) {
            participants.put(participantId, true);
        }

        Chat chat = new Chat();
        chat.setChatId(chatId);
        chat.setName(chatName);
        chat.setType(chatType);
        chat.setParticipants(participants);

        chatsRef.child(chatId).setValue(chat);

        Intent i = new Intent(NewChatActivity.this, MessagesActivity.class);
        i.putExtra("chatId", chatId);
        i.putExtra("name", chatName);
        startActivity(i);
        finish();
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}