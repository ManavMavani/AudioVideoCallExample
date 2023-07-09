package com.example.audiovideocallexample;

import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.audiovideocallexample.databinding.UserItemBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context context;
    private ArrayList<User> usersList;

    public UserAdapter(Context context, ArrayList<User> usersList, Application application) {
        this.context = context;
        this.usersList = usersList;
    }

    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.user_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position) {
        User user = usersList.get(position);
        holder.binding.userNameTV.setText(user.getUserName());
        holder.binding.phoneNumberTV.setText(user.getPhoneNumber());

        FirebaseDatabase.getInstance()
                .getReference("users")
                .child(FirebaseAuth.getInstance().getUid())
                .child("friends")
                .child(user.getuId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            holder.binding.requestBTN.setText("Cancel");
                        } else {
                            holder.binding.requestBTN.setText("Request");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        holder.binding.requestBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.binding.requestBTN.getText().toString().equals("Request")) {
                    requestFriend(user.getuId());
                } else {
                    removeFriend(user.getuId());
                }
            }
        });
    }

    private void requestFriend(String uId) {
        Map<String, Object> map = new HashMap<>();
        map.put(uId, "Requested");

        FirebaseDatabase.getInstance()
                .getReference("users")
                .child(FirebaseAuth.getInstance().getUid())
                .child("friends")
                .updateChildren(map);

        FirebaseDatabase.getInstance()
                .getReference("users")
                .child(uId)
                .child("requests")
                .updateChildren(map);
    }

    private void removeFriend(String uId) {
        FirebaseDatabase.getInstance()
                .getReference("users")
                .child(FirebaseAuth.getInstance().getUid())
                .child("friends")
                .child(uId)
                .removeValue();

        FirebaseDatabase.getInstance()
                .getReference("users")
                .child(uId)
                .child("requests")
                .child(uId)
                .removeValue();
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        UserItemBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = UserItemBinding.bind(itemView);
        }
    }
}
