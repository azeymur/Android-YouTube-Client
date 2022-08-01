package com.zeymur.youtubeclient.datasource;

import com.zeymur.youtubeclient.IService;
import com.zeymur.youtubeclient.RequestState;
import com.zeymur.youtubeclient.ServiceGenerator;
import com.zeymur.youtubeclient.model.Item;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;
import android.content.Context;

public abstract class ItemDataSource extends PageKeyedDataSource<String, Item> {
    public static final int PAGE_SIZE = 20;
    protected IService service;
    protected MutableLiveData<RequestState> requestState;
    protected String key;
    protected Context context;

    public ItemDataSource(Context context, String key) {
        requestState = new MutableLiveData();
        service = new ServiceGenerator(context,"https://www.googleapis.com/youtube/v3/", null).createService(IService.class);
        this.key = key;
        this.context = context;
    }

    public MutableLiveData<RequestState> getRequestState() {
        return requestState;
    }

}
