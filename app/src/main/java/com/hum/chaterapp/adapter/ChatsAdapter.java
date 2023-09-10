package com.hum.chaterapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hum.chaterapp.R;
import com.hum.chaterapp.activity.MessagesActivity;
import com.hum.chaterapp.model.Chat;
import com.hum.chaterapp.service.Firebase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ViewHolder> {

    public ArrayList<Chat> mItems;

    public ChatsAdapter(ArrayList<Chat> items) {
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
        if (mItems.get(position).getType().equals("private")) {
            holder.txtName.setText(Firebase.use().getRecipientName(mItems.get(position).getParticipants()));
        } else {
            holder.txtName.setText(mItems.get(position).getName());
        }
        String lastMessage = mItems.get(position).getLastMessage().getText();
        if (mItems.get(position).getType().equals("group")) {
            if (mItems.get(position).getLastMessage().getSenderId() != null) {
                if (mItems.get(position).getLastMessage().getSenderId().equals(Firebase.use().getUserId())) {
                    lastMessage = "You: ".concat(lastMessage);
                } else {
                    lastMessage = Firebase.use().getUser(mItems.get(position).getLastMessage().getSenderId()).getName().concat(": ").concat(lastMessage);
                }
            }
        }
        holder.txtLastMessage.setText(lastMessage);
        Calendar n = Calendar.getInstance();
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(mItems.get(position).getLastMessage().getTimestamp());
        String format = n.getTimeInMillis() - c.getTimeInMillis() > 1000 * 60 * 60 * 24 ? "dd/MM/yy" : "hh:mm a";
        holder.txtTimestamp.setText(new SimpleDateFormat(format, Locale.getDefault()).format(c.getTime()));

        holder.itemView.setOnClickListener(view -> {
            Intent i = new Intent(holder.itemView.getContext(), MessagesActivity.class);
            i.putExtra("name", holder.txtName.getText());
            i.putExtra("chatId", mItems.get(position).getChatId());
            holder.itemView.getContext().startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public ArrayList<Chat> getItems() {
        return mItems;
    }
}
