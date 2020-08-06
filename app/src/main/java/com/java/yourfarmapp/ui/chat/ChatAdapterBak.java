//package com.java.yourfarmapp.ui.chat;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.firebase.ui.database.FirebaseRecyclerAdapter;
//import com.firebase.ui.database.FirebaseRecyclerOptions;
//import com.java.yourfarmapp.Model.ChatModel;
//import com.java.yourfarmapp.R;
//
//import java.util.List;
//
//public class ChatAdapterBak extends FirebaseRecyclerAdapter<ChatModel, ChatAdapter.ViewHolder> {
//
//    public static final int MSG_TYPE_LEFT = 0;
//    public static final int MSG_TYPE_RIGHT = 0;
//
//    private Context mContext;
//    private List<ChatModel> chatModelList;
//
//    public ChatAdapter(@NonNull FirebaseRecyclerOptions<ChatModel> options) {
//        super(options);
//    }
//
//    @Override
//    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull ChatModel chatModel) {
//
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_activity_show_chat_dialog, parent, false);
//        return new ChatAdapter.ViewHolder(view);
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//        }
//    }
//}
