package com.hum.chaterapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
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
import com.hum.chaterapp.service.Firebase;

import java.util.ArrayList;
import java.util.HashMap;

public class MessagesActivity extends AppCompatActivity {

    private TextView txtUsername;
    private RecyclerView recMessages;
    private TextView txtMessage;
    private Button btnSendMessage;
    private ArrayList<HashMap<String, Object>> messagesList;
    private String chatId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        txtUsername = findViewById(R.id.txt_username);
        recMessages = findViewById(R.id.rec_messages);
        txtMessage = findViewById(R.id.txt_message);
        btnSendMessage = findViewById(R.id.btn_send_message);

        messagesList = new ArrayList<>();
        MessagesAdapter adapter = new MessagesAdapter(messagesList);
        recMessages.setLayoutManager(new LinearLayoutManager(this));
        recMessages.setAdapter(adapter);

        chatId = getIntent().getStringExtra("chatId");

        DatabaseReference messagesRef = Firebase.get().ref().child("chats").child(chatId).child("messages");
        messagesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot messagesSnapshot) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                for (DataSnapshot data : messagesSnapshot.getChildren()) {
                    HashMap<String, Object> map = data.getValue(_ind);
                    messagesList.add(map);
                }
                recMessages.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError e) {
                showMessage(e.getMessage());
            }
        });
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}