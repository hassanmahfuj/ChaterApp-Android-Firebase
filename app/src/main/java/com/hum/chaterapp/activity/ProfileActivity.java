package com.hum.chaterapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.hum.chaterapp.R;
import com.hum.chaterapp.model.User;
import com.hum.chaterapp.service.Firebase;

public class ProfileActivity extends AppCompatActivity {

    private TextView txtName;
    private TextView txtPhone;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        txtName = findViewById(R.id.txt_name);
        txtPhone = findViewById(R.id.txt_phone);
        btnLogout = findViewById(R.id.btn_logout);

        btnLogout.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
            finishAffinity();
        });

        setCurrentUserDetails();
    }

    private void setCurrentUserDetails() {
        User u = Firebase.use().getUser();
        txtName.setText(u.getName());
        txtPhone.setText(u.getPhone());
    }
}