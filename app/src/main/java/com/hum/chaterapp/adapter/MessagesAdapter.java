package com.hum.chaterapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hum.chaterapp.R;
import com.hum.chaterapp.model.Message;
import com.hum.chaterapp.service.Firebase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

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
        public TextView txtSenderName;

        public ViewHolder(View itemView) {
            super(itemView);
            linHolder = itemView.findViewById(R.id.lin_holder);
            txtTimestamp = itemView.findViewById(R.id.txt_timestamp);
            txtMessage = itemView.findViewById(R.id.txt_message);
            txtSenderName = itemView.findViewById(R.id.txt_sender_name);
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
        holder.txtTimestamp.setText(new SimpleDateFormat(format, Locale.getDefault()).format(c.getTime()));
        holder.txtMessage.setText(mItems.get(position).getText());

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        int leftMargin;
        int topMargin = pixelToDp(5);
        int rightMargin;
        int bottomMargin = pixelToDp(5);

        if (Firebase.use().getUserId().equals(mItems.get(position).getSenderId())) {
            holder.linHolder.setBackgroundResource(R.drawable.right_message_bg);
            holder.txtMessage.setTextColor(Color.parseColor("#FFFFFF"));
            holder.txtTimestamp.setTextColor(Color.parseColor("#FFFFFF"));
            layoutParams.gravity = Gravity.END;
            leftMargin = pixelToDp(50);
            rightMargin = pixelToDp(5);
            holder.txtSenderName.setVisibility(View.GONE);
        } else {
            holder.linHolder.setBackgroundResource(R.drawable.left_message_bg);
            holder.txtMessage.setTextColor(Color.parseColor("#000000"));
            holder.txtTimestamp.setTextColor(Color.parseColor("#000000"));
            layoutParams.gravity = Gravity.START;
            leftMargin = pixelToDp(5);
            rightMargin = pixelToDp(50);
            holder.txtSenderName.setVisibility(View.VISIBLE);
            holder.txtSenderName.setText(Firebase.use().getUser(mItems.get(position).getSenderId()).getName());
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
