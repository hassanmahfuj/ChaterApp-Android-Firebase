package com.hum.chaterapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.hum.chaterapp.R;
import com.hum.chaterapp.adapter.MessagesAdapter;
import com.hum.chaterapp.model.Message;
import com.hum.chaterapp.service.Firebase;

import java.util.ArrayList;

public class MessagesActivity extends AppCompatActivity {

    private TextView txtUsername;
    private RecyclerView recMessages;
    private TextView txtMessage;
    private Button btnSendMessage;
    private ImageView actionBack;
    private ProgressBar loading;
    private TextView txtNoChats;
    private ImageButton btnGetLocation;

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
        btnGetLocation = findViewById(R.id.btn_get_location);

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

        btnGetLocation.setOnClickListener(view -> {
            checkPermission();
        });

        actionBack.setOnClickListener(view -> finish());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 6060) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkLocationEnabled();
            } else if (!ActivityCompat.shouldShowRequestPermissionRationale(MessagesActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(MessagesActivity.this)
                        .setMessage("You have permanently denied this permission, goto settings to allow the permission.")
                        .setPositiveButton("Goto Settings", (dialogInterface, i) -> {
                            startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getApplicationContext().getPackageName())));
                            finish();
                        })
                        .setNegativeButton("Exit App", (dialogInterface, i) -> finish())
                        .setCancelable(false)
                        .show();
            } else {
                ActivityCompat.requestPermissions(MessagesActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        6060);
            }
        }
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(
                MessagesActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            checkLocationEnabled();
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(MessagesActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            new AlertDialog.Builder(MessagesActivity.this)
                    .setMessage("We need your permission to access device's location to get current location.")
                    .setPositiveButton("OK", (dialogInterface, i) -> ActivityCompat.requestPermissions(MessagesActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            6060))
                    .show();
        } else {
            ActivityCompat.requestPermissions(MessagesActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    6060);
        }
    }

    private void checkLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            new AlertDialog.Builder(MessagesActivity.this)
                    .setTitle("GPS Not Found")
                    .setMessage("Please enable GPS.")
                    .setPositiveButton("Yes", (dialogInterface, i) -> {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    })
                    .setNegativeButton("No", (dialogInterface, i) -> {
                    })
                    .show();
        } else {
            getLastLocation();
        }
    }

    private void getLastLocation() {
        FusedLocationProviderClient locationProvider = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationProvider.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                String googleMapsLink = "https://www.google.com/maps?q=" + latitude + "," + longitude;
                txtMessage.setText(googleMapsLink);
                showMessage("Location Found");
            } else {
                showMessage("null");
            }
        });
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}