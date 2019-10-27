package com.example.chatappclient.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatappclient.Common.Common;
import com.example.chatappclient.MessageActivity;
import com.example.chatappclient.Model.Chat;
import com.example.chatappclient.Model.User;
import com.example.chatappclient.R;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    public  static  final int MSG_TYPE_LEFT = 0;
    public static final  int MSG_TYPE_RIGHT = 1;
    private Context mContext;
    private List<Chat> mChat;

    public MessageAdapter(Context mContext, List<Chat> mChat) {
        this.mContext = mContext;
        this.mChat = mChat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT){
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right,parent,false);
            return new ViewHolder(view);
        }else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
            return new ViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Chat chat = mChat.get(position);
        holder.showMassage.setText(chat.getChat());
        holder.image_user.setImageResource(R.drawable.ic_account_circle_black_24dp);

    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView showMassage;
        public ImageView image_user;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            showMassage = itemView.findViewById(R.id.showMassage);
            image_user = itemView.findViewById(R.id.image_user);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(mChat.get(position).getSender().equals(Common.currentUser.getName())){
            return MSG_TYPE_RIGHT;
        }
        else{
            return MSG_TYPE_LEFT;
        }
    }
}
