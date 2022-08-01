package com.zeymur.youtubeclient.viewmodel;

import com.zeymur.youtubeclient.RequestState;
import com.zeymur.youtubeclient.YOUTUBE_IDTYPE;
import com.zeymur.youtubeclient.datasource.ItemDataSource;
import com.zeymur.youtubeclient.datasource.ItemDataSourceFactory;
import com.zeymur.youtubeclient.model.Item;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import android.content.Context;

public class ItemViewModel extends ViewModel {
    public LiveData<PagedList<Item>> itemPagedList;
    public LiveData<ItemDataSource> liveDataSource;

    private ItemDataSourceFactory itemDataSourceFactory;
    private LiveData<RequestState> networkState;

    public ItemViewModel(Context context, String id, YOUTUBE_IDTYPE idType, String key) {
        itemDataSourceFactory = new ItemDataSourceFactory(context, id, idType, key);
        liveDataSource = itemDataSourceFactory.getItemLiveDataSource();
        networkState = Transformations.switchMap(liveDataSource, dataSource -> dataSource.getRequestState());

        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(false)
                        .setPageSize(ItemDataSource.PAGE_SIZE).build();

        itemPagedList = (new LivePagedListBuilder(itemDataSourceFactory, pagedListConfig))
                .build();
    }

    public LiveData<RequestState> getNetworkState() { return networkState; }

    public void refresh() {
        itemDataSourceFactory.getItemLiveDataSource().getValue().invalidate();
    }

}
