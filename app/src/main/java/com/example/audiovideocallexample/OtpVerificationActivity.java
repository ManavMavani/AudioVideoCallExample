package com.example.audiovideocallexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.audiovideocallexample.databinding.ActivityOtpVerificationBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class OtpVerificationActivity extends AppCompatActivity {

    private ActivityOtpVerificationBinding binding;
    private String phoneNumberText;
    private String userNameText;
    private FirebaseAuth auth;
    private String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOtpVerificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();

        phoneNumberText = getIntent().getStringExtra("phoneNumber");
        userNameText = getIntent().getStringExtra("userName");

        sendOtp(phoneNumberText);

        binding.submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.otpET.getText().toString().trim().isEmpty()) {
                    binding.otpET.setError("Please Enter OTP...");
                } else {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, binding.otpET.getText().toString().trim());
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });
    }

    private void sendOtp(String phoneNumberText) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(phoneNumberText)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                signInWithPhoneAuthCredential(phoneAuthCredential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(OtpVerificationActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                verificationId = s;
                                Toast.makeText(OtpVerificationActivity.this, "OTP send successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User user = new User(FirebaseAuth.getInstance().getUid(), userNameText, phoneNumberText);
                            FirebaseDatabase.getInstance()
                                    .getReference("users")
                                    .child(FirebaseAuth.getInstance().getUid())
                                    .setValue(user)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                startActivity(new Intent(OtpVerificationActivity.this, MainActivity.class));
                                                finish();
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(OtpVerificationActivity.this, task.getException().toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}