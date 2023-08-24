package com.hum.chaterapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.hum.chaterapp.activity.ChatsActivity;
import com.hum.chaterapp.activity.LoginActivity;
import com.hum.chaterapp.service.Firebase;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // when getting all users data then go next
        Firebase.use().initUsers(this::goNext);
    }

    private void goNext() {
        Intent i;
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            i = new Intent(MainActivity.this, LoginActivity.class);
        } else {
            i = new Intent(MainActivity.this, ChatsActivity.class);
        }
        startActivity(i);
        finish();
    }
}