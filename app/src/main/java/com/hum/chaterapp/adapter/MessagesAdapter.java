package com.hum.chaterapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hum.chaterapp.R;
import com.hum.chaterapp.model.Message;
import com.hum.chaterapp.service.Firebase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder> {
    public ArrayList<Message> mItems;
    Context context;

    public MessagesAdapter(ArrayList<Message> items) {
        mItems = items;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout linHolder;
        public TextView txtTimestamp;
        public TextView txtMessage;

        public ViewHolder(View itemView) {
            super(itemView);
            linHolder = itemView.findViewById(R.id.lin_holder);
            txtTimestamp = itemView.findViewById(R.id.txt_timestamp);
            txtMessage = itemView.findViewById(R.id.txt_message);
        }
    }

    @NonNull
    @Override
    public MessagesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_message, parent, false);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        itemView.setLayoutParams(lp);
        return new MessagesAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MessagesAdapter.ViewHolder holder, int position) {
        Calendar n = Calendar.getInstance();
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(mItems.get(position).getTimestamp());
        String format = n.getTimeInMillis() - c.getTimeInMillis() > 1000 * 60 * 60 * 24 ? "dd/MM/yy" : "hh:mm a";
        holder.txtTimestamp.setText(new SimpleDateFormat(format).format(c.getTime()));

        holder.txtMessage.setText(mItems.get(position).getText());

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, // Width
                LinearLayout.LayoutParams.WRAP_CONTENT // Height
        );
        int leftMargin;
        int topMargin = pixelToDp(8);;
        int rightMargin;
        int bottomMargin = pixelToDp(8);
        if (Firebase.use().getUserId().equals(mItems.get(position).getSenderId())) {
            holder.itemView.setBackgroundColor(Color.parseColor("#FF00FF"));
            leftMargin = pixelToDp(30);
            rightMargin = pixelToDp(8);
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#FF0000"));
            leftMargin = pixelToDp(8);
            rightMargin = pixelToDp(30);
        }
        layoutParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);
        holder.linHolder.setLayoutParams(layoutParams);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public ArrayList<Message> getItems() {
        return mItems;
    }

    private int pixelToDp(int p) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, p, context.getResources().getDisplayMetrics()
        );
    }
}
