package com.example.audiovideocallexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.example.audiovideocallexample.databinding.ActivityFriendBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FriendActivity extends AppCompatActivity {

    private ActivityFriendBinding binding;
    private ArrayList<String> keyList;
    private ArrayList<Object> valueList;
    private FriendAdapter friendAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFriendBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        keyList = new ArrayList<>();
        valueList = new ArrayList<>();
        friendAdapter = new FriendAdapter(FriendActivity.this, keyList, valueList);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(FriendActivity.this));
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setAdapter(friendAdapter);

        FirebaseDatabase.getInstance()
                .getReference("users")
                .child(FirebaseAuth.getInstance().getUid())
                .child("requests")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        keyList.clear();
                        valueList.clear();
                        Map<String, Object> map = new HashMap<>();
                        if (snapshot.exists()) {
                            for (DataSnapshot dataSnapshot :
                                    snapshot.getChildren()) {
                                keyList.add(dataSnapshot.getKey());
                                valueList.add(dataSnapshot.getValue(String.class  ));
                            }
                            friendAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
}