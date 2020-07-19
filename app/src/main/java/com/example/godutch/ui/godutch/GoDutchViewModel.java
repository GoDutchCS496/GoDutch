package com.example.godutch.ui.godutch;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GoDutchViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public GoDutchViewModel() {
        mText = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return mText;
    }
}