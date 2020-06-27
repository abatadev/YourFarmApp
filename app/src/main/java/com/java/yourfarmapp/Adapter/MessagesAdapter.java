package com.java.yourfarmapp.Adapter;

import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.java.yourfarmapp.Model.MessagesModel;
import com.java.yourfarmapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessagesViewHolder> {

    private List<MessagesModel> userMessagesList;
    private FirebaseAuth mAuth;
    private DatabaseReference usersDatabaseRef;

    public MessagesAdapter(List<MessagesModel> userMessagesList) {
        this.userMessagesList = userMessagesList;
    }

    public class MessagesViewHolder extends  RecyclerView.ViewHolder {
        public TextView senderMessageText, receiverMessageText;
        public CircleImageView receiverProfileImage;

        public MessagesViewHolder(View itemView) {
            super(itemView);

            senderMessageText = itemView.findViewById(R.id.sender_message_text);
            receiverMessageText = itemView.findViewById(R.id.receiver_message_text);
            receiverProfileImage = itemView.findViewById(R.id.message_profile_image);
        }
    }

    @NonNull
    @Override
    public MessagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.messages_layout_of_users, parent, false);

        mAuth = FirebaseAuth.getInstance();

        return  new MessagesViewHolder(myView);
     }

    @Override
    public void onBindViewHolder(@NonNull MessagesViewHolder holder, int position) {
        String messageSenderId = mAuth.getCurrentUser().getUid();
        MessagesModel messagesModel = userMessagesList.get(position);

        String fromUserID = messagesModel.getFrom();
        String fromMessageType = messagesModel.getType();

        usersDatabaseRef = FirebaseDatabase.getInstance().getReference().child("User").child(fromUserID);
        usersDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    String image = dataSnapshot.child("profilePic").getValue().toString();
                    Picasso.get().load(image).fit().placeholder(R.drawable.profile_picture)
                            .into(holder.receiverProfileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if(fromMessageType.equals("text")) {
            holder.receiverMessageText.setVisibility(View.INVISIBLE);
            holder.receiverProfileImage.setVisibility(View.INVISIBLE);

            if (fromUserID.equals(messageSenderId)) {

                holder.senderMessageText.setBackgroundResource(R.drawable.sender_message_text_background);
                holder.senderMessageText.setTextColor(Color.BLACK);
                holder.senderMessageText.setGravity(Gravity.LEFT);
                holder.senderMessageText.setText(messagesModel.getMessage());

            } else {
                holder.senderMessageText.setVisibility(View.INVISIBLE);

                holder.receiverMessageText.setVisibility(View.VISIBLE);
                holder.receiverProfileImage.setVisibility(View.VISIBLE);

                holder.receiverMessageText.setBackgroundResource(R.drawable.receiver_message_text_background);
                holder.receiverMessageText.setTextColor(Color.WHITE);
                holder.receiverMessageText.setGravity(Gravity.LEFT);
                holder.receiverMessageText.setText(messagesModel.getMessage());
            }
        }
    }

    @Override
    public int getItemCount() {
        return userMessagesList.size();
    }
}
