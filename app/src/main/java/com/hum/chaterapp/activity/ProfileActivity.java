package com.hum.chaterapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.hum.chaterapp.R;
import com.hum.chaterapp.model.User;
import com.hum.chaterapp.service.Firebase;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class ProfileActivity extends AppCompatActivity {

    private TextInputEditText txtUsername;
    private TextInputEditText txtPhoneNumber;
    private TextInputEditText txtMemberSince;
    private Button btnSave;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        txtUsername = findViewById(R.id.txt_username);
        txtPhoneNumber = findViewById(R.id.txt_phone_number);
        txtMemberSince = findViewById(R.id.txt_member_since);
        btnSave = findViewById(R.id.btn_save);
        btnLogout = findViewById(R.id.btn_logout);

        btnSave.setOnClickListener(view -> {
            User u = Firebase.use().getUser();
            u.setName(txtUsername.getText().toString());
            Firebase.use().updateUser(u);
        });

        btnLogout.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
            finishAffinity();
        });

        setCurrentUserDetails();
    }

    private void setCurrentUserDetails() {
        User u = Firebase.use().getUser();
        txtUsername.setText(u.getName());
        txtPhoneNumber.setText(u.getPhone());
        txtMemberSince.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(u.getCreatedAt()));
    }
}