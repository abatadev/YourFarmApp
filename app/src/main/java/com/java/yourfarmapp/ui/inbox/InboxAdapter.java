package com.java.yourfarmapp.ui.inbox;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.java.yourfarmapp.Model.ChatModel;
import com.java.yourfarmapp.Model.UserModel;
import com.java.yourfarmapp.R;
import com.java.yourfarmapp.ui.chat.ChatActivity;

import java.util.List;

public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.ViewHolder> {

    private Context mContext;
    private List<UserModel> mUsers;

    private String theLastMessage;
    private boolean ischat;

    String lastMessage;

    public InboxAdapter(Context mContext, List<UserModel> mUsers, boolean ischat) {
        this.mContext = mContext;
        this.mUsers = mUsers;
        this.ischat = ischat;
    }

    public InboxAdapter(Context context, List<UserModel> mUser) {

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.card_view_list_inbox, parent, false);
        return new InboxAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final UserModel userModel = mUsers.get(position);

        holder.inboxUserName.setText(userModel.getFullName());
        if (userModel.getProfilePic().equals("default")){
            holder.inboxImageView.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(mContext).load(userModel.getProfilePic()).into(holder.inboxImageView);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ChatActivity.class);
                intent.putExtra("userid", userModel.getUID());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView inboxUserName, inboxLastMessage;
        public ImageView inboxImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            inboxUserName = itemView.findViewById(R.id.inbox_user_name);
            inboxImageView = itemView.findViewById(R.id.inbox_profile_picture);
            inboxLastMessage = itemView.findViewById(R.id.inbox_last_message);
        }
    }

    private void lastMessage(final String userId, final TextView inboxLastMessage) {
        theLastMessage = "default";

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chat");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ChatModel chat = snapshot.getValue(ChatModel.class);
                    if (firebaseUser != null && chat != null) {
                        if (chat.getReceiverId().equals(firebaseUser.getUid()) && chat.getSenderId().equals(userId) ||
                                chat.getReceiverId().equals(userId) && chat.getSenderId().equals(firebaseUser.getUid())) {

                            theLastMessage = chat.getMessage();
                        }
                    }
                }

                switch (theLastMessage){
                    case  "default":
                        inboxLastMessage.setText("No Message");
                        break;

                    default:
                        inboxLastMessage.setText(theLastMessage);
                        break;
                }

                theLastMessage = "default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

}
