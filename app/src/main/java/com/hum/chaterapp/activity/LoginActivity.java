package com.hum.chaterapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hum.chaterapp.R;
import com.hum.chaterapp.model.User;
import com.hum.chaterapp.service.Firebase;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private TextInputEditText txtPhone;
    private TextInputEditText txtOtp;
    private Button btnSendOtp;
    private Button btnLogin;
    private LinearLayout linPhoneNumber;
    private LinearLayout linOtp;
    private ProgressBar loader;

    FirebaseAuth mAuth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    String mVerificationId;
    String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtPhone = findViewById(R.id.txt_phone);
        txtOtp = findViewById(R.id.txt_otp);
        btnSendOtp = findViewById(R.id.btn_send_otp);
        btnLogin = findViewById(R.id.btn_login);
        linPhoneNumber = findViewById(R.id.lin_phone_number);
        linOtp = findViewById(R.id.lin_otp);
        loader = findViewById(R.id.loader);

        btnSendOtp.setOnClickListener(view -> sendVerificationCode());
        btnLogin.setOnClickListener(view -> verifyCode());

        mAuth = FirebaseAuth.getInstance();

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.w(TAG, "onVerificationFailed", e);
                showMessage(e.getMessage());
                loader.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                mVerificationId = verificationId;
                linOtp.setVisibility(View.VISIBLE);
                linPhoneNumber.setVisibility(View.GONE);
                loader.setVisibility(View.INVISIBLE);
                showMessage("Code Sent");
            }
        };
    }

    private void sendVerificationCode() {
        loader.setVisibility(View.VISIBLE);
        phoneNumber = "+88" + txtPhone.getText().toString();
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .requireSmsValidation(false)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        loader.setVisibility(View.VISIBLE);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        showMessage("Sign in successful");
                        if (task.getResult().getAdditionalUserInfo() != null) {
                            if (task.getResult().getAdditionalUserInfo().isNewUser()) {
                                User user = new User();
                                user.setLastSeen(System.currentTimeMillis());
                                user.setCreatedAt(System.currentTimeMillis());
                                user.setPhone(phoneNumber);
                                user.setUserId(phoneNumber);
                                user.setName(phoneNumber);
                                Firebase.use().createNewUser(user);
                            }
                        }
                        Firebase.use().initUsers(() -> {
                            startActivity(new Intent(LoginActivity.this, ChatsActivity.class));
                            finish();
                        });
                    } else {
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        showMessage("Sign in fail");
                    }
                });
    }

    private void verifyCode() {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, txtOtp.getText().toString());
        signInWithPhoneAuthCredential(credential);
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        Log.d("msg", message);
    }
}