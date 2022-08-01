package com.zeymur.youtubeclient.viewmodel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import android.content.Context;

import com.zeymur.youtubeclient.YOUTUBE_IDTYPE;

public class ItemViewModelFactory implements ViewModelProvider.Factory {
    private Context context;
    private String id;
    private String key;
    private YOUTUBE_IDTYPE idType;

    public ItemViewModelFactory(Context context, String id, YOUTUBE_IDTYPE idType, String key) {
        this.context = context;
        this.id = id;
        this.key = key;
        this.idType = idType;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new ItemViewModel(context, id, idType, key);
    }

}