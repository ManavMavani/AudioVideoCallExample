package com.example.audiovideocallexample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.audiovideocallexample.databinding.FriendItemBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {

    private Context context;
    private ArrayList<String> keyList;
    private ArrayList<Object> valueList;

    public FriendAdapter(Context context, ArrayList<String> keyList, ArrayList<Object> valueList) {
        this.context = context;
        this.keyList = keyList;
        this.valueList = valueList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.friend_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String key = keyList.get(position);
        Object value = valueList.get(position);

        holder.binding.userNameTV.setText(key);

        holder.binding.acceptBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acceptRequest(key);
            }
        });

        holder.binding.declineBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                declineRequest(key);
            }
        });
    }

    @Override
    public int getItemCount() {
        return keyList.size();
    }

    private void acceptRequest(String uId) {
        Map<String, Object> map = new HashMap<>();
        map.put(uId, "Accepted");

        FirebaseDatabase.getInstance()
                .getReference("users")
                .child(uId)
                .child("friends")
                .updateChildren(map);

        FirebaseDatabase.getInstance()
                .getReference("users")
                .child(FirebaseAuth.getInstance().getUid())
                .child("requests")
                .updateChildren(map);
    }

    private void declineRequest(String uId) {
        FirebaseDatabase.getInstance()
                .getReference("users")
                .child(uId)
                .child("friends")
                .child(uId)
                .removeValue();

        FirebaseDatabase.getInstance()
                .getReference("users")
                .child(FirebaseAuth.getInstance().getUid())
                .child("requests")
                .child(uId)
                .removeValue();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        FriendItemBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = FriendItemBinding.bind(itemView);
        }
    }
}
