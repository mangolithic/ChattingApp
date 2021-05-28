package com.example.chattingapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import de.hdodenhof.circleimageview.CircleImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chattingapp.ChatActivity;
import com.example.chattingapp.Model.Users;
import com.example.chattingapp.R;

import java.util.List;



public class UserAdapter extends RecyclerView.Adapter <UserAdapter.ViewHolder> {
    private Context context;
    private List<Users> myUsers;

    //constructor


    public UserAdapter(Context context, List<Users> myUsers) {
        this.context = context;
        this.myUsers = myUsers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_items,
                parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Users users = myUsers.get(position);
        holder.username.setText(users.getUsername());

        if(users.getImageURL().equals("default")){
            holder.imageView.setImageResource(R.drawable.profile);
        }else{
            Glide.with(context).load(users.getImageURL()).into(holder.imageView);
        }

        holder.about.setText(users.getAbout());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent (context, ChatActivity.class);
                intent.putExtra("userid",users.getId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return myUsers.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView username;
        CircleImageView imageView;
        TextView about;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.userText);
            imageView = itemView.findViewById(R.id.userImage);
            about = itemView.findViewById(R.id.about);
        }
    }

}







