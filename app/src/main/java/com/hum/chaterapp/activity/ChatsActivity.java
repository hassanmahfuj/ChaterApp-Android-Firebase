package com.hum.chaterapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hum.chaterapp.R;
import com.hum.chaterapp.adapter.ChatsAdapter;
import com.hum.chaterapp.model.Chat;
import com.hum.chaterapp.model.User;
import com.hum.chaterapp.service.Firebase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ChatsActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private RecyclerView recChats;
    private ArrayList<Chat> chatsList;
    private ProgressBar loading;
    private TextView txtNoChats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);

        fab = findViewById(R.id.fab);
        recChats = findViewById(R.id.rec_chats);
        loading = findViewById(R.id.loading);
        txtNoChats = findViewById(R.id.txt_no_chats);

        MaterialToolbar topAppBar = findViewById(R.id.topAppBar);
        topAppBar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_profile) {
                startActivity(new Intent(ChatsActivity.this, ProfileActivity.class));
                return true;
            }
            return false;
        });

        fab.setOnClickListener(view -> {
            startActivity(new Intent(ChatsActivity.this, NewChatActivity.class));
        });

        chatsList = new ArrayList<>();
        ChatsAdapter adapter = new ChatsAdapter(chatsList);
        recChats.setLayoutManager(new LinearLayoutManager(this));
        recChats.setAdapter(adapter);

        fetchMessagesForUser(Firebase.use().getUserId());
    }

    public void fetchMessagesForUser(String userId) {
        DatabaseReference userChatsRef = Firebase.use().ref().child("chats");
        Query userChatQuery = userChatsRef.orderByChild("participants/" + userId).equalTo(true);
        userChatQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                chatsList.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    chatsList.add(data.getValue(Chat.class));
                }
                loading.setVisibility(View.GONE);
                recChats.getAdapter().notifyDataSetChanged();
                if (chatsList.size() > 0) {
                    txtNoChats.setVisibility(View.GONE);
                } else {
                    txtNoChats.setVisibility(View.VISIBLE);
                    txtNoChats.setText("No chats yet!");
                }
            }

            @Override
            public void onCancelled(DatabaseError e) {
                showMessage(e.getMessage());
                txtNoChats.setVisibility(View.VISIBLE);
                txtNoChats.setText(e.getMessage());
                loading.setVisibility(View.GONE);
            }
        });
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}