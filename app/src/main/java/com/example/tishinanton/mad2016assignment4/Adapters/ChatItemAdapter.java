package com.example.tishinanton.mad2016assignment4.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.tishinanton.mad2016assignment4.Models.MessageModel;
import com.example.tishinanton.mad2016assignment4.R;

import java.util.ArrayList;

/**
 * Created by Tishin Anton on 24.05.2016.
 */
public class ChatItemAdapter extends ArrayAdapter<MessageModel> {

    private Context context;
    private ArrayList<MessageModel> values;

    public ChatItemAdapter(Context context, ArrayList<MessageModel> values) {
        super(context, R.layout.chat_message_item, values);
        this.context = context;
        this.values = values;
    }

    static class ViewHolder {
        public TextView messageFrom;
        public TextView message;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;

        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.chat_message_item, parent, false);
            holder = new ViewHolder();
            holder.messageFrom = (TextView)rowView.findViewById(R.id.chat_message_item_messageFrom);
            holder.message = (TextView)rowView.findViewById(R.id.chat_message_item_message);
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder)rowView.getTag();
        }

        MessageModel message = values.get(position);

        holder.messageFrom.setText(message.fromUser);
        holder.message.setText(message.message);

        return rowView;
    }
}
