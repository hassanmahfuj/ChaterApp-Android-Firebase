package com.hum.chaterapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hum.chaterapp.R;
import com.hum.chaterapp.adapter.ChatsAdapter;
import com.hum.chaterapp.service.Firebase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ChatsActivity extends AppCompatActivity {

    private Button btnLogout;
    private TextView txtName;
    private TextView txtPhone;
    private RecyclerView recChats;
    private ArrayList<HashMap<String, Object>> chatsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);

        btnLogout = findViewById(R.id.btn_logout);
        txtName = findViewById(R.id.txt_name);
        txtPhone = findViewById(R.id.txt_phone);
        recChats = findViewById(R.id.rec_chats);

        btnLogout.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(ChatsActivity.this, LoginActivity.class));
            finish();
        });

        txtName.setText(FirebaseAuth.getInstance().getUid());
        txtPhone.setText(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());

        Firebase.get().saveUser(FirebaseAuth.getInstance().getUid(),
                FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());

        // TODO: update this every five second if activity is visible
        Firebase.get().updateLastSeen();

        chatsList = new ArrayList<>();
        ChatsAdapter adapter = new ChatsAdapter(chatsList);
        recChats.setLayoutManager(new LinearLayoutManager(this));
        recChats.setAdapter(adapter);

        fetchMessagesForUser(FirebaseAuth.getInstance().getUid());
    }

    public void fetchMessagesForUser(String userId) {
        DatabaseReference userChatsRef = Firebase.get().ref().child("chats");
        Query userChatQuery = userChatsRef.orderByChild("participants/" + userId).equalTo(true);
        userChatQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    HashMap<String, Object> map = data.getValue(_ind);
                    chatsList.add(map);
                }
                recChats.getAdapter().notifyDataSetChanged();
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