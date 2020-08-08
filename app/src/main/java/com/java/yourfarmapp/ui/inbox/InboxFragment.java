package com.java.yourfarmapp.ui.inbox;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.java.yourfarmapp.Model.ChatModel;
import com.java.yourfarmapp.Model.ChatModelList;
import com.java.yourfarmapp.Model.UserModel;
import com.java.yourfarmapp.R;

import java.util.ArrayList;
import java.util.List;

public class InboxFragment extends Fragment {

    private RecyclerView recyclerView;

    private FirebaseAuth mAuth;
    private FirebaseUser myUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference userRef, chatRef;

    private InboxAdapter inboxAdapter;
    private List<UserModel> mUsers;
    private List<ChatModelList> usersList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_inbox, container, false);

        recyclerView = view.findViewById(R.id.inbox_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        myUser = mAuth.getCurrentUser();

        usersList = new ArrayList<>();

        userRef = mDatabase.getReference("User");

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();

                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ChatModelList chatList = snapshot.getValue(ChatModelList.class);
                    usersList.add(chatList);
                }

                chatList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

//    private void readChat() {
//        mUser = new ArrayList<>();
//
//        userRef = mDatabase.getReference("User");
//        userRef.child(myUser.getUid()).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                mUser.clear();
//                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    UserModel userModel = snapshot.getValue(UserModel.class);
//                    userList.add(userModel);
//                }
//
//                chatList();
//            }
//
//
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }

    private void chatList() {
        mUsers = new ArrayList<>();

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    UserModel userModel = snapshot.getValue(UserModel.class);
                    for (ChatModelList chatList : usersList){
                        if (userModel.getUID().equals(chatList.getId())){
                            mUsers.add(userModel);
                        }
                    }
                }

                inboxAdapter = new InboxAdapter(getContext(), mUsers,true);
                recyclerView.setAdapter(inboxAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

}
