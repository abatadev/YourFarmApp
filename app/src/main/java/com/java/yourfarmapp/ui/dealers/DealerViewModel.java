package com.java.yourfarmapp.ui.dealers;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DealerViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public DealerViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dealer fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
