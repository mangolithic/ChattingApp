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
import com.example.chattingapp.Model.Chat;
import com.example.chattingapp.Model.Users;
import com.example.chattingapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.List;



public class UserAdapter extends RecyclerView.Adapter <UserAdapter.ViewHolder> {
    private Context context;
    private List<Users> myUsers;
    private boolean ischat;

    String latestmsg;
    String about;


    public UserAdapter(Context context, List<Users> myUsers, boolean ischat) {
        this.context = context;
        this.myUsers = myUsers;
        this.ischat = ischat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_items,
                parent, false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Users users = myUsers.get(position);
        holder.username.setText(users.getUsername());

        if (users.getImageURL().equals("default")) {
            holder.imageView.setImageResource(R.drawable.profile);
        } else {
            Glide.with(context).load(users.getImageURL()).into(holder.imageView);
        }

        /*holder.about.setText(users.getAbout());*/

        if (ischat) {
            lastMessage(users.getId(), holder.lastmsg);
        } else {
            holder.lastmsg.setVisibility(View.GONE);
        }

        if (ischat) {
            holder.about.setVisibility(View.GONE);
        }else{
            holder.about.setText(users.getAbout());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("userid", users.getId());
                context.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return myUsers.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView username;
        CircleImageView imageView;
        TextView about;
        TextView lastmsg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.userText);
            imageView = itemView.findViewById(R.id.userImage);
            about = itemView.findViewById(R.id.about);
            lastmsg = itemView.findViewById(R.id.lastmsg);
        }
    }

    //check for last message
    private void lastMessage(final String userid, final TextView lastmsg) {
        latestmsg = "default";
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    if (firebaseUser != null && chat != null) {
                        if (chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userid) ||
                                chat.getReceiver().equals(userid) && chat.getSender().equals(firebaseUser.getUid())) {
                            latestmsg = chat.getMessage();
                        }
                    }
                }

                switch (latestmsg) {
                    case "default":
                        lastmsg.setText("No Message");
                        break;

                    default:
                        lastmsg.setText(latestmsg);
                        break;
                }

                latestmsg = "default";
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

}







