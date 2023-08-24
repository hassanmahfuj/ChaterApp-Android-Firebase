package com.hum.chaterapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.hum.chaterapp.R;
import com.hum.chaterapp.activity.MessagesActivity;
import com.hum.chaterapp.service.Firebase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ViewHolder> {

    public ArrayList<HashMap<String, Object>> mItems;

    public ChatsAdapter(ArrayList<HashMap<String, Object>> items) {
        mItems = items;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgPhoto;
        public TextView txtName;
        public TextView txtTimestamp;
        public TextView txtLastMessage;

        public ViewHolder(View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.img_photo);
            txtName = itemView.findViewById(R.id.txt_name);
            txtTimestamp = itemView.findViewById(R.id.txt_timestamp);
            txtLastMessage = itemView.findViewById(R.id.txt_last_message);
        }
    }

    @NonNull
    @Override
    public ChatsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_chat, parent, false);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        itemView.setLayoutParams(lp);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatsAdapter.ViewHolder holder, int position) {
        // if this is a private message getting the name from user id of recipient
        if(mItems.get(position).get("type").toString().equals("private")) {
//            String[] ids = ((Map<String, Boolean>) mItems.get(position).get("participants")).keySet().toArray(new String[0]);
//            for(String id : ids) {
//                if(!Firebase.use().getUserId().equals(id)) {
//                    holder.txtName.setText(Firebase.use().getUser(id).getName());
//                }
//            }
              holder.txtName.setText(Firebase.use().getRecipientName(mItems.get(position).get("participants")));
        }

        // getting the last message from all messages
        Map<String, Object> messages = ((Map<String, Object>) mItems.get(position).get("messages"));
        Map<String, Object> lastMessage = (Map<String, Object>) messages.get(Collections.max(messages.keySet()));
        // setting last message and time
        holder.txtLastMessage.setText(lastMessage.get("text").toString());
        Calendar n = Calendar.getInstance();
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis((long) lastMessage.get("timestamp"));
        String format = n.getTimeInMillis() - c.getTimeInMillis() > 1000 * 60 * 60 * 24 ? "dd/MM/yy" : "hh:mm a";
        holder.txtTimestamp.setText(new SimpleDateFormat(format).format(c.getTime()));

        holder.itemView.setOnClickListener(view -> {
            Intent i = new Intent(holder.itemView.getContext(), MessagesActivity.class);
            i.putExtra("name", holder.txtName.getText());
            i.putExtra("chatId", mItems.get(position).get("chatId").toString());
            holder.itemView.getContext().startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public ArrayList<HashMap<String, Object>> getItems() {
        return mItems;
    }
}
