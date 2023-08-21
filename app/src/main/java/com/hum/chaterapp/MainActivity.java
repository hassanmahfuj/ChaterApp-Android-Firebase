package com.hum.chaterapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.hum.chaterapp.activity.ChatsActivity;
import com.hum.chaterapp.activity.LoginActivity;

public class MainActivity extends AppCompatActivity {

    private boolean isLoggedIn = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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