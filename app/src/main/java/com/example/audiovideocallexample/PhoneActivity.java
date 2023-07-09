package com.example.audiovideocallexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.audiovideocallexample.databinding.ActivityPhoneBinding;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneActivity extends AppCompatActivity {

    private ActivityPhoneBinding binding;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPhoneBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(PhoneActivity.this, MainActivity.class));
            finish();
        }

        binding.ccp.registerCarrierNumberEditText(binding.phoneNumberET);

        binding.nextBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.phoneNumberET.getText().toString().trim().isEmpty() || binding.userNameET.getText().toString().trim().isEmpty()) {
                    if (binding.phoneNumberET.getText().toString().trim().isEmpty()) {
                        binding.phoneNumberET.setError("Please Enter Phone NUmber...");
                    } else {
                        binding.userNameET.setError("Please Enter User Name...");
                    }
                } else {
                    Intent intent = new Intent(PhoneActivity.this, OtpVerificationActivity.class);
                    intent.putExtra("phoneNumber", binding.ccp.getFullNumberWithPlus().trim());
                    intent.putExtra("userName", binding.userNameET.getText().toString().trim());
                    startActivity(intent);
                }
            }
        });
    }
}