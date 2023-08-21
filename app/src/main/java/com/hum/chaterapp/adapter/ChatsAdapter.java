package com.hum.chaterapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hum.chaterapp.R;

import java.util.ArrayList;
import java.util.HashMap;

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
        holder.txtName.setText(mItems.get(position).get("name").toString());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public ArrayList<HashMap<String, Object>> getItems() {
        return mItems;
    }
}
