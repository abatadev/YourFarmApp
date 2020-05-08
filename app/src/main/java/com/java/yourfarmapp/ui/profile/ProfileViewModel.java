package com.java.yourfarmapp.ui.profile;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.java.yourfarmapp.Common.Common;
import com.java.yourfarmapp.Model.UserModel;

public class ProfileViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ProfileViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is asdasd fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

    private void loadUserInformation(String shipperPhone) {
        final Query orderRef = FirebaseDatabase.getInstance().getReference(Common.USER_REF)
                .orderByChild("shipperPhone")
                .equalTo(Common.currentUser.getPhoneNumber());

        orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserModel user = dataSnapshot.getValue(UserModel.class);

                user.getFullName();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                databaseError.getMessage();
            }
        });
    }
}