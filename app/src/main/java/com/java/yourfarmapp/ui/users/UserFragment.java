package com.java.yourfarmapp.ui.users;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.java.yourfarmapp.Model.UserModel;
import com.java.yourfarmapp.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserFragment extends Fragment {

    private UserViewModel userViewModel;
    private RecyclerView recyclerView;

    private DatabaseReference userRef;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        userViewModel =
                ViewModelProviders.of(this).get(UserViewModel.class);
        View root = inflater.inflate(R.layout.user_fragment, container, false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(false);
        linearLayoutManager.setStackFromEnd(false);

        recyclerView = (RecyclerView) root.findViewById(R.id.dealer_list);
        recyclerView.setLayoutManager(linearLayoutManager);

        userRef = FirebaseDatabase.getInstance().getReference().child("User");

        displayDealers();

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

    }

    private void displayDealers() {
        Query query = userRef;

        FirebaseRecyclerOptions<UserModel> options = new FirebaseRecyclerOptions.Builder<UserModel>()
                .setQuery(query, UserModel.class)
                .build();

        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<UserModel, DealerViewHolder>(options) {
            @NonNull
            @Override
            public DealerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_dealer, parent, false);
                return new DealerViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull DealerViewHolder dealerViewHolder, int position, @NonNull UserModel userModel) {

                final String userIDs = getRef(position).getKey();
                final String dealer = "dealer";
                final String farmer = "farmer";

                boolean isTrue = true;

                Log.d("Dealer Fragment", "Print Query: " + userRef.child(userIDs).orderByChild(dealer).equalTo(isTrue).toString());

                userRef.child(userIDs).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.d("Dealer Fragment", "Print: " + userRef.child(userIDs).orderByChild(dealer).equalTo(isTrue).toString());
                        if(dataSnapshot.exists()) {
                            final String dealerNameToString =  dataSnapshot.child("fullName").getValue().toString();
                            final String dealerImageToString = dataSnapshot.child("profilePic").getValue().toString();
                            final String contactNumber = dataSnapshot.child("number").getValue().toString();

                            final String dealerToString = dataSnapshot.child("dealer").getValue().toString();
                            final String farmerToString = dataSnapshot.child("farmer").getValue().toString();

                            dealerViewHolder.setDealerName(dealerNameToString);
                            dealerViewHolder.setContactNumber(contactNumber);
                            dealerViewHolder.setDealerProfilePicture(dealerImageToString);

                            if (dealerToString.equals("true")) {
                                dealerViewHolder.setAccountType("Dealer");
                            }

                            else if (dealerToString.equals("false")) {
                                dealerViewHolder.setAccountType("Farmer");
                            }

                        } else {
                            Toast.makeText(getContext(), "Unable to find dealers.", Toast.LENGTH_SHORT).show();
                            Log.d("adapter", "Does not exist");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

        };

        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    public static class DealerViewHolder extends RecyclerView.ViewHolder {
        View mView;
        ImageView categoryImage;

        public DealerViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setDealerName(String name) {
            TextView dealerName = mView.findViewById(R.id.dealer_name_list);
            dealerName.setText(name);
        }

        public void setAccountType(String test) {
            TextView accountType = mView.findViewById(R.id.account_type_text);
            accountType.setText(test);
        }

        public void setContactNumber(String contactNumber) {
            TextView contactNumbers = mView.findViewById(R.id.dealer_contact_number_list);
            contactNumbers.setText(contactNumber);
        }

        public void setDealerProfilePicture(String image) {
            CircleImageView dealerProfilePictureCircle = mView.findViewById(R.id.dealer_profile_picture_circle);
            Picasso.get().load(image).fit().into(dealerProfilePictureCircle);
        }
    }

}
