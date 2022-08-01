package com.zeymur.youtubeclient.datasource;

import com.zeymur.youtubeclient.model.Item;
import com.zeymur.youtubeclient.YOUTUBE_IDTYPE;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import android.content.Context;

public class ItemDataSourceFactory extends DataSource.Factory {
    private String id;
    private YOUTUBE_IDTYPE idType;
    private String key;
    private MutableLiveData<ItemDataSource> itemLiveDataSource = new MutableLiveData<>();
    private Context context;

    public ItemDataSourceFactory(Context context, String id, YOUTUBE_IDTYPE idType, String key) {
        this.id = id;
        this.idType = idType;
        this.key = key;
        this.context = context;
    }

    @Override
    public DataSource<String, Item> create() {
        ItemDataSource postDataSource;

        if (idType == YOUTUBE_IDTYPE.PLAYLIST) postDataSource = new PlaylistItemDataSource(context, id, key);
        else postDataSource = new ChannelItemDataSource(context, id, key);

        itemLiveDataSource.postValue(postDataSource);
        return postDataSource;
    }

    public MutableLiveData<ItemDataSource> getItemLiveDataSource() {
        return itemLiveDataSource;
    }

}
