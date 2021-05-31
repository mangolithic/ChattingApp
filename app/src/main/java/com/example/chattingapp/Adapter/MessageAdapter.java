package com.example.chattingapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import de.hdodenhof.circleimageview.CircleImageView;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chattingapp.ChatActivity;
import com.example.chattingapp.Model.Chat;
import com.example.chattingapp.Model.Users;
import com.example.chattingapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;



public class MessageAdapter extends RecyclerView.Adapter <MessageAdapter.ViewHolder> {
    private Context context;
    private List<Chat> myChat;
    private String imgURL;

    FirebaseUser fuser;

    public static final int MSG_TYPE_LEFT=0;
    public static final int MSG_TYPE_RIGHT=1;

    //constructor


    public MessageAdapter(Context context, List<Chat> myChat, String imgURL) {
        this.context = context;
        this.myChat = myChat;
        this.imgURL=imgURL;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_right,
                    parent, false);
            return new MessageAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_left,
                    parent, false);
            return new MessageAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {

        Chat chat= myChat.get(position);

        switch(chat.getType()) {
            case "TEXT":
                holder.show_message.setVisibility(View.VISIBLE);
                holder.showImage.setVisibility(View.GONE);

                holder.show_message.setText(chat.getMessage());
                break;
            case "IMAGE":
                holder.show_message.setVisibility(View.GONE);
                holder.showImage.setVisibility(View.VISIBLE);

                Glide.with(context).load(chat.getURL()).into(holder.showImage);
                break;
        }


        if (imgURL.equals("default")){
            holder.profile_image.setImageResource(R.drawable.profile);
        }else{
            Glide.with(context).load(imgURL).into(holder.profile_image);
        }
    }

    @Override
    public int getItemCount() {
        return myChat.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView show_message;
        CircleImageView profile_image;
        ImageView showImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            show_message = itemView.findViewById(R.id.showMsg);
            profile_image = itemView.findViewById(R.id.profile_image);
            showImage = itemView.findViewById(R.id.showImg);
        }
    }

    @Override
    public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if(myChat.get(position).getSender().equals(fuser.getUid())){
            return MSG_TYPE_RIGHT;
        }else{
            return MSG_TYPE_LEFT;
        }
    }
}

