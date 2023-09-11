package com.hum.chaterapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hum.chaterapp.R;
import com.hum.chaterapp.adapter.ChatsAdapter;
import com.hum.chaterapp.adapter.MessagesAdapter;
import com.hum.chaterapp.model.Message;
import com.hum.chaterapp.service.Firebase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MessagesActivity extends AppCompatActivity {

    private TextView txtUsername;
    private RecyclerView recMessages;
    private TextView txtMessage;
    private Button btnSendMessage;
    private ImageView actionBack;
    private ProgressBar loading;
    private TextView txtNoChats;

    private ArrayList<Message> messagesList;
    private String chatId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        txtUsername = findViewById(R.id.txt_username);
        recMessages = findViewById(R.id.rec_messages);
        txtMessage = findViewById(R.id.txt_message);
        btnSendMessage = findViewById(R.id.btn_send_message);
        actionBack = findViewById(R.id.action_back);
        loading = findViewById(R.id.loading);
        txtNoChats = findViewById(R.id.txt_no_chats);

        messagesList = new ArrayList<>();
        MessagesAdapter adapter = new MessagesAdapter(messagesList);
        recMessages.setLayoutManager(new LinearLayoutManager(this));
        recMessages.setAdapter(adapter);

        txtUsername.setText(getIntent().getStringExtra("name"));
        chatId = getIntent().getStringExtra("chatId");

        Firebase.use().getMessagesByChatId(chatId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot messagesSnapshot) {
                messagesList.clear();
                loading.setVisibility(View.GONE);
                for (DataSnapshot data : messagesSnapshot.getChildren()) {
                    messagesList.add(data.getValue(Message.class));
                }
                recMessages.getAdapter().notifyDataSetChanged();
                recMessages.scrollToPosition(messagesList.size() - 1);
                if (messagesList.size() > 0) {
                    txtNoChats.setVisibility(View.GONE);
                } else {
                    txtNoChats.setVisibility(View.VISIBLE);
                    txtNoChats.setText("No messages yet!");
                }
            }

            @Override
            public void onCancelled(DatabaseError e) {
                loading.setVisibility(View.GONE);
                txtNoChats.setVisibility(View.VISIBLE);
                txtNoChats.setText(e.getMessage());
                showMessage(e.getMessage());
            }
        });

        btnSendMessage.setOnClickListener(view -> {
            String msg = txtMessage.getText().toString().trim();
            if (msg.equals("")) return;

            Message message = new Message();
            message.setSenderId(Firebase.use().getUserId());
            message.setTimestamp(System.currentTimeMillis());
            message.setText(msg);
            Firebase.use().sendMessage(chatId, message);
            txtMessage.setText("");
        });

        actionBack.setOnClickListener(view -> finish());
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}