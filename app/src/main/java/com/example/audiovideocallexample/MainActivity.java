package com.example.audiovideocallexample;

import android.content.Intent;
import android.icu.lang.UScript;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.audiovideocallexample.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationService;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ArrayList<User> usersList;
    private UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        usersList = new ArrayList<>();
        userAdapter = new UserAdapter(MainActivity.this, usersList, getApplication());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setAdapter(userAdapter);

        FirebaseDatabase.getInstance()
                .getReference("users")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        usersList.clear();
                        if (snapshot.exists()) {
                            for (DataSnapshot dataSnapshot :
                                    snapshot.getChildren()) {
                                User user = dataSnapshot.getValue(User.class);
                                if (!user.getuId().equals(FirebaseAuth.getInstance().getUid())) {
                                    usersList.add(user);
                                }
                            }
                            userAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        binding.logoutBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, PhoneActivity.class));
                finish();
            }
        });

        binding.friendBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, FriendActivity.class));
            }
        });
    }
}