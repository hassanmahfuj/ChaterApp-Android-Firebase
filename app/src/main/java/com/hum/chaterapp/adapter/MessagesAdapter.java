package com.hum.chaterapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hum.chaterapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder> {
    public ArrayList<HashMap<String, Object>> mItems;

    public MessagesAdapter(ArrayList<HashMap<String, Object>> items) {
        mItems = items;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
//        public ImageView imgPhoto;
//        public TextView txtName;
        public TextView txtTimestamp;
        public TextView txtMessage;

        public ViewHolder(View itemView) {
            super(itemView);
//            imgPhoto = itemView.findViewById(R.id.img_photo);
//            txtName = itemView.findViewById(R.id.txt_name);
            txtTimestamp = itemView.findViewById(R.id.txt_timestamp);
            txtMessage = itemView.findViewById(R.id.txt_message);
        }
    }

    @NonNull
    @Override
    public MessagesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_message, parent, false);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        itemView.setLayoutParams(lp);
        return new MessagesAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MessagesAdapter.ViewHolder holder, int position) {
        // getting the last message from all messages
//        Map<String, Object> messages = ((Map<String, Object>) mItems.get(position).get("messages"));
//        Map<String, Object> lastMessage = (Map<String, Object>) messages.get(Collections.max(messages.keySet()));
        // setting last message and time
//        holder.txtMessage.setText(lastMessage.get("text").toString());
//        Calendar n = Calendar.getInstance();
//        Calendar c = Calendar.getInstance();
//        c.setTimeInMillis((long) lastMessage.get("timestamp"));
//        String format = n.getTimeInMillis() - c.getTimeInMillis() > 1000 * 60 * 60 * 24 ? "dd/MM/yy" : "hh:mm a";
//        holder.txtTimestamp.setText(new SimpleDateFormat(format).format(c.getTime()));

        holder.txtMessage.setText(mItems.get(position).get("text").toString());

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public ArrayList<HashMap<String, Object>> getItems() {
        return mItems;
    }
}
